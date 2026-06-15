package com.ozodbek.booknest.ui.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Image
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.ozodbek.booknest.util.Validators

/**
 * Form to add a new book recommendation. Includes the Lab 3 Firebase Storage
 * flow: pick a cover image from the gallery, preview it, then upload it (with a
 * progress bar) when the book is saved.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddBookScreen(
    uploadProgress: Int?,
    onSave: (title: String, author: String, description: String, genre: String, coverUri: Uri?) -> Unit,
    onBack: () -> Unit
) {
    var title by remember { mutableStateOf("") }
    var author by remember { mutableStateOf("") }
    var genre by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var coverUri by remember { mutableStateOf<Uri?>(null) }
    var error by remember { mutableStateOf<String?>(null) }
    var saving by remember { mutableStateOf(false) }

    val picker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri -> coverUri = uri }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Recommend a book") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
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
            if (coverUri != null) {
                AsyncImage(
                    model = coverUri,
                    contentDescription = "Selected cover preview",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .clip(RoundedCornerShape(12.dp))
                )
                Spacer(Modifier.height(8.dp))
            }
            OutlinedButton(
                onClick = { picker.launch("image/*") },
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(Icons.Filled.Image, contentDescription = null)
                Spacer(Modifier.width(8.dp))
                Text(if (coverUri == null) "Pick a cover image (optional)" else "Change cover")
            }

            Spacer(Modifier.height(16.dp))
            OutlinedTextField(title, { title = it }, label = { Text("Title") },
                modifier = Modifier.fillMaxWidth())
            Spacer(Modifier.height(8.dp))
            OutlinedTextField(author, { author = it }, label = { Text("Author") },
                modifier = Modifier.fillMaxWidth())
            Spacer(Modifier.height(8.dp))
            OutlinedTextField(genre, { genre = it }, label = { Text("Genre") },
                modifier = Modifier.fillMaxWidth())
            Spacer(Modifier.height(8.dp))
            OutlinedTextField(description, { description = it },
                label = { Text("Why do you recommend it?") },
                minLines = 3, modifier = Modifier.fillMaxWidth())

            error?.let {
                Spacer(Modifier.height(8.dp))
                Text(it, color = MaterialTheme.colorScheme.error)
            }

            if (uploadProgress != null) {
                Spacer(Modifier.height(16.dp))
                Text("Uploading cover… $uploadProgress%")
                LinearProgressIndicator(
                    progress = { uploadProgress / 100f },
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Spacer(Modifier.height(16.dp))
            Button(
                onClick = {
                    val result = Validators.validateBookInput(title, author, description)
                    if (result.isValid) {
                        saving = true
                        onSave(title, author, description, genre, coverUri)
                    } else error = result.errorMessage
                },
                enabled = !saving,
                modifier = Modifier.fillMaxWidth()
            ) { Text("Save recommendation") }
        }
    }
}
