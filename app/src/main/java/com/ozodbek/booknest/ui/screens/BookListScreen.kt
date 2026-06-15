package com.ozodbek.booknest.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Sort
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
import com.ozodbek.booknest.util.SortOption
import com.ozodbek.booknest.viewmodel.BookListUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookListScreen(
    state: BookListUiState,
    isSaved: (String) -> Boolean,
    onQueryChange: (String) -> Unit,
    onSortChange: (SortOption) -> Unit,
    onToggleSaved: (Book) -> Unit,
    onOpenBook: (String) -> Unit,
    onAddBook: () -> Unit,
    onOpenSaved: () -> Unit,
    onLogout: () -> Unit
) {
    var sortMenuOpen by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("BookNest") },
                actions = {
                    IconButton(onClick = onOpenSaved) {
                        Icon(Icons.Filled.Bookmark, contentDescription = "Saved books")
                    }
                    Box {
                        IconButton(onClick = { sortMenuOpen = true }) {
                            Icon(Icons.Filled.Sort, contentDescription = "Sort")
                        }
                        DropdownMenu(
                            expanded = sortMenuOpen,
                            onDismissRequest = { sortMenuOpen = false }
                        ) {
                            SortOption.values().forEach { option ->
                                DropdownMenuItem(
                                    text = { Text(option.label) },
                                    onClick = {
                                        onSortChange(option)
                                        sortMenuOpen = false
                                    }
                                )
                            }
                        }
                    }
                    TextButton(onClick = onLogout) { Text("Log out") }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onAddBook) {
                Icon(Icons.Filled.Add, contentDescription = "Recommend a book")
            }
        }
    ) { padding ->
        Column(Modifier.padding(padding)) {
            OutlinedTextField(
                value = state.query,
                onValueChange = onQueryChange,
                label = { Text("Search title, author or genre") },
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            )

            when {
                state.isLoading -> Box(
                    Modifier.fillMaxSize(), contentAlignment = Alignment.Center
                ) { CircularProgressIndicator() }

                state.visibleBooks.isEmpty() -> EmptyState(hasQuery = state.query.isNotBlank())

                else -> LazyColumn(
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(state.visibleBooks, key = { it.bookId }) { book ->
                        BookRow(
                            book = book,
                            saved = isSaved(book.bookId),
                            onClick = { onOpenBook(book.bookId) },
                            onToggleSaved = { onToggleSaved(book) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun EmptyState(hasQuery: Boolean) {
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(
            if (hasQuery) "No books match your search."
            else "No recommendations yet — tap + to add the first one!",
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun BookRow(
    book: Book,
    saved: Boolean,
    onClick: () -> Unit,
    onToggleSaved: () -> Unit
) {
    ElevatedCard(onClick = onClick, modifier = Modifier.fillMaxWidth()) {
        Row(Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
            if (book.coverImageUrl.isNotBlank()) {
                AsyncImage(
                    model = book.coverImageUrl,
                    contentDescription = "${book.title} cover",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(56.dp, 80.dp)
                        .clip(RoundedCornerShape(6.dp))
                )
                Spacer(Modifier.width(12.dp))
            }
            Column(Modifier.weight(1f)) {
                Text(book.title, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
                Text(book.author, style = MaterialTheme.typography.bodyMedium)
                if (book.genre.isNotBlank()) {
                    AssistChip(onClick = {}, label = { Text(book.genre) })
                }
            }
            IconButton(onClick = onToggleSaved) {
                Icon(
                    if (saved) Icons.Filled.Bookmark else Icons.Filled.BookmarkBorder,
                    contentDescription = "Save to Want to Read"
                )
            }
        }
    }
}
