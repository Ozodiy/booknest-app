package com.ozodbek.booknest.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.ozodbek.booknest.data.Book
import com.ozodbek.booknest.util.Validators

/**
 * Book detail + edit screen. The recommender (owner) sees editable fields and
 * a delete button; other users see a read-only view. This enforces the
 * "only creator can edit/delete" permission rule (Lab 2 Phase 3).
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookDetailScreen(
    book: Book,
    currentUserId: String,
    onSave: (title: String, author: String, description: String, genre: String) -> Unit,
    onDelete: () -> Unit,
    onBack: () -> Unit
) {
    val isOwner = book.recommendedBy == currentUserId
    var title by remember { mutableStateOf(book.title) }
    var author by remember { mutableStateOf(book.author) }
    var description by remember { mutableStateOf(book.description) }
    var genre by remember { mutableStateOf(book.genre) }
    var error by remember { mutableStateOf<String?>(null) }
    var showDeleteDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (isOwner) "Edit recommendation" else "Book details") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    if (isOwner) {
                        IconButton(onClick = { showDeleteDialog = true }) {
                            Icon(Icons.Filled.Delete, contentDescription = "Delete")
                        }
                    }
                }
            )
        }
    ) { padding ->
        Column(
            Modifier
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            if (book.coverImageUrl.isNotBlank()) {
                AsyncImage(
                    model = book.coverImageUrl,
                    contentDescription = "${book.title} cover",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(220.dp)
                        .clip(RoundedCornerShape(12.dp))
                )
                Spacer(Modifier.height(16.dp))
            }

            if (isOwner) {
                OutlinedTextField(title, { title = it }, label = { Text("Title") },
                    modifier = Modifier.fillMaxWidth())
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(author, { author = it }, label = { Text("Author") },
                    modifier = Modifier.fillMaxWidth())
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(genre, { genre = it }, label = { Text("Genre") },
                    modifier = Modifier.fillMaxWidth())
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(description, { description = it }, label = { Text("Description") },
                    minLines = 3, modifier = Modifier.fillMaxWidth())

                error?.let {
                    Spacer(Modifier.height(8.dp))
                    Text(it, color = MaterialTheme.colorScheme.error)
                }
                Spacer(Modifier.height(16.dp))
                Button(
                    onClick = {
                        val result = Validators.validateBookInput(title, author, description)
                        if (result.isValid) onSave(title, author, description, genre)
                        else error = result.errorMessage
                    },
                    modifier = Modifier.fillMaxWidth()
                ) { Text("Save changes") }
            } else {
                Text(book.title, fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.headlineSmall)
                Text("by ${book.author}", style = MaterialTheme.typography.titleMedium)
                Spacer(Modifier.height(8.dp))
                if (book.genre.isNotBlank()) AssistChip(onClick = {}, label = { Text(book.genre) })
                Spacer(Modifier.height(16.dp))
                Text(book.description, style = MaterialTheme.typography.bodyLarge)
                Spacer(Modifier.height(16.dp))
                Text("Recommended by ${book.recommenderName}",
                    style = MaterialTheme.typography.labelLarge)
            }
        }
    }

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Delete recommendation?") },
            text = { Text("This will permanently remove \"${book.title}\".") },
            confirmButton = {
                TextButton(onClick = { showDeleteDialog = false; onDelete() }) {
                    Text("Delete")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) { Text("Cancel") }
            }
        )
    }
}
