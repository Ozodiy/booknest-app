package com.ozodbek.booknest.util

import com.ozodbek.booknest.data.Book

/** Sort orders offered on the book list (Lab 2 "sorting" extension). */
enum class SortOption(val label: String) {
    TITLE_ASC("Title (A–Z)"),
    AUTHOR_ASC("Author (A–Z)"),
    GENRE_ASC("Genre (A–Z)"),
    NEWEST_FIRST("Newest first")
}

/**
 * Pure, side-effect-free list operations for the book list. Kept separate from
 * the ViewModel so they can be unit-tested without Firestore (Lab 3 Phase 3).
 */
object BookFilters {

    /**
     * Case-insensitive search across title, author and genre
     * (Lab 2 "search/filter" extension). A blank query returns the list as-is.
     */
    fun search(books: List<Book>, query: String): List<Book> {
        val q = query.trim().lowercase()
        if (q.isEmpty()) return books
        return books.filter { book ->
            book.title.lowercase().contains(q) ||
                book.author.lowercase().contains(q) ||
                book.genre.lowercase().contains(q)
        }
    }

    fun sort(books: List<Book>, option: SortOption): List<Book> = when (option) {
        SortOption.TITLE_ASC -> books.sortedBy { it.title.lowercase() }
        SortOption.AUTHOR_ASC -> books.sortedBy { it.author.lowercase() }
        SortOption.GENRE_ASC -> books.sortedBy { it.genre.lowercase() }
        SortOption.NEWEST_FIRST -> books.sortedByDescending { it.createdAt?.seconds ?: 0L }
    }

    /** Convenience: search then sort, the way the UI applies both together. */
    fun searchAndSort(books: List<Book>, query: String, option: SortOption): List<Book> =
        sort(search(books, query), option)
}
