package com.ozodbek.booknest.viewmodel

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.storage.FirebaseStorage
import com.ozodbek.booknest.data.Book
import com.ozodbek.booknest.data.SaveStatus
import com.ozodbek.booknest.data.SavedBook
import com.ozodbek.booknest.util.BookFilters
import com.ozodbek.booknest.util.SortOption
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.util.UUID

data class BookListUiState(
    val isLoading: Boolean = true,
    val allBooks: List<Book> = emptyList(),
    val query: String = "",
    val sortOption: SortOption = SortOption.NEWEST_FIRST,
    val errorMessage: String? = null
) {
    /** The list actually shown: search + sort applied to the raw snapshot. */
    val visibleBooks: List<Book>
        get() = BookFilters.searchAndSort(allBooks, query, sortOption)
}

/**
 * Handles Firestore CRUD for the `books` collection (Lab 2 Phase 3), the
 * personal `savedBooks` list, and — new in Lab 3 — uploading cover images to
 * Firebase Storage before a book is written.
 *
 * Real-time updates come from `addSnapshotListener`, so the list refreshes
 * the instant any document changes.
 */
class BookViewModel : ViewModel() {

    private val db = FirebaseFirestore.getInstance()
    private val storage = FirebaseStorage.getInstance()

    private val _uiState = MutableStateFlow(BookListUiState())
    val uiState: StateFlow<BookListUiState> = _uiState.asStateFlow()

    private val _savedBooks = MutableStateFlow<List<SavedBook>>(emptyList())
    val savedBooks: StateFlow<List<SavedBook>> = _savedBooks.asStateFlow()

    private val _uploadProgress = MutableStateFlow<Int?>(null)
    val uploadProgress: StateFlow<Int?> = _uploadProgress.asStateFlow()

    init {
        observeBooks()
    }

    // ---- READ (real-time) -------------------------------------------------

    private fun observeBooks() {
        db.collection("books")
            .orderBy("createdAt", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = error.localizedMessage
                    )
                    return@addSnapshotListener
                }
                val books = snapshot?.documents?.mapNotNull { doc ->
                    doc.toObject(Book::class.java)?.copy(bookId = doc.id)
                } ?: emptyList()
                _uiState.value = _uiState.value.copy(isLoading = false, allBooks = books)
            }
    }

    fun observeSavedBooks(userId: String) {
        db.collection("savedBooks")
            .whereEqualTo("userId", userId)
            .addSnapshotListener { snapshot, _ ->
                _savedBooks.value = snapshot?.documents?.mapNotNull { doc ->
                    doc.toObject(SavedBook::class.java)?.copy(savedId = doc.id)
                } ?: emptyList()
            }
    }

    fun getBook(bookId: String): Book? =
        _uiState.value.allBooks.firstOrNull { it.bookId == bookId }

    // ---- Search & sort (Lab 2 extensions) ---------------------------------

    fun onQueryChange(query: String) {
        _uiState.value = _uiState.value.copy(query = query)
    }

    fun onSortChange(option: SortOption) {
        _uiState.value = _uiState.value.copy(sortOption = option)
    }

    // ---- CREATE (with optional cover upload — Lab 3) ----------------------

    fun addBook(
        title: String,
        author: String,
        description: String,
        genre: String,
        recommendedBy: String,
        recommenderName: String,
        coverUri: Uri?,
        onDone: (Boolean) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val coverUrl = if (coverUri != null) uploadCover(coverUri) else ""
                val ref = db.collection("books").document()
                val book = Book(
                    bookId = ref.id,
                    title = title.trim(),
                    author = author.trim(),
                    description = description.trim(),
                    genre = genre.trim(),
                    coverImageUrl = coverUrl,
                    recommendedBy = recommendedBy,
                    recommenderName = recommenderName,
                    createdAt = Timestamp.now()
                )
                ref.set(book).await()
                onDone(true)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(errorMessage = e.localizedMessage)
                onDone(false)
            } finally {
                _uploadProgress.value = null
            }
        }
    }

    // ---- UPDATE -----------------------------------------------------------

    fun updateBook(
        bookId: String,
        title: String,
        author: String,
        description: String,
        genre: String,
        onDone: (Boolean) -> Unit
    ) {
        viewModelScope.launch {
            try {
                db.collection("books").document(bookId).update(
                    mapOf(
                        "title" to title.trim(),
                        "author" to author.trim(),
                        "description" to description.trim(),
                        "genre" to genre.trim()
                    )
                ).await()
                onDone(true)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(errorMessage = e.localizedMessage)
                onDone(false)
            }
        }
    }

    // ---- DELETE -----------------------------------------------------------

    fun deleteBook(book: Book, onDone: (Boolean) -> Unit) {
        viewModelScope.launch {
            try {
                db.collection("books").document(book.bookId).delete().await()
                // Best-effort: also remove the cover from Storage if present.
                if (book.coverImageUrl.isNotBlank()) {
                    runCatching {
                        storage.getReferenceFromUrl(book.coverImageUrl).delete().await()
                    }
                }
                onDone(true)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(errorMessage = e.localizedMessage)
                onDone(false)
            }
        }
    }

    // ---- Saved / Want-to-Read list ---------------------------------------

    fun toggleSaved(userId: String, book: Book) {
        val existing = _savedBooks.value.firstOrNull { it.bookId == book.bookId }
        if (existing != null) {
            db.collection("savedBooks").document(existing.savedId).delete()
        } else {
            val ref = db.collection("savedBooks").document()
            ref.set(
                SavedBook(
                    savedId = ref.id,
                    userId = userId,
                    bookId = book.bookId,
                    title = book.title,
                    author = book.author,
                    status = SaveStatus.WANT_TO_READ,
                    savedAt = Timestamp.now()
                )
            )
        }
    }

    /** Status toggle extension: want-to-read <-> finished. */
    fun toggleSavedStatus(saved: SavedBook) {
        val next = if (saved.status == SaveStatus.FINISHED) {
            SaveStatus.WANT_TO_READ
        } else {
            SaveStatus.FINISHED
        }
        db.collection("savedBooks").document(saved.savedId).update("status", next)
    }

    fun isSaved(bookId: String): Boolean =
        _savedBooks.value.any { it.bookId == bookId }

    // ---- Firebase Storage upload (Lab 3 advanced feature) -----------------

    private suspend fun uploadCover(uri: Uri): String {
        _uploadProgress.value = 0
        val ref = storage.reference.child("book_covers/${UUID.randomUUID()}.jpg")
        val task = ref.putFile(uri)
        task.addOnProgressListener { snap ->
            val pct = (100.0 * snap.bytesTransferred / snap.totalByteCount).toInt()
            _uploadProgress.value = pct
        }
        task.await()
        return ref.downloadUrl.await().toString()
    }
}
