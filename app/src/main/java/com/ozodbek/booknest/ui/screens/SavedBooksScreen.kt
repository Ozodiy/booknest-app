package com.ozodbek.booknest.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.ozodbek.booknest.data.SaveStatus
import com.ozodbek.booknest.data.SavedBook

/** "Want to Read" list with a status toggle (Lab 2 status-toggle extension). */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SavedBooksScreen(
    saved: List<SavedBook>,
    onToggleStatus: (SavedBook) -> Unit,
    onBack: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("My Want-to-Read list") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        if (saved.isEmpty()) {
            Box(
                Modifier
                    .padding(padding)
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) { Text("No saved books yet. Tap the bookmark on any book.") }
        } else {
            LazyColumn(
                modifier = Modifier.padding(padding),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(saved, key = { it.savedId }) { item ->
                    val finished = item.status == SaveStatus.FINISHED
                    ElevatedCard(Modifier.fillMaxWidth()) {
                        Row(
                            Modifier.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(Modifier.weight(1f)) {
                                Text(
                                    item.title,
                                    fontWeight = FontWeight.Bold,
                                    textDecoration = if (finished) TextDecoration.LineThrough else null
                                )
                                Text(item.author, style = MaterialTheme.typography.bodyMedium)
                            }
                            FilterChip(
                                selected = finished,
                                onClick = { onToggleStatus(item) },
                                label = { Text(if (finished) "Finished" else "Want to read") }
                            )
                        }
                    }
                }
            }
        }
    }
}
