package com.ozodbek.booknest

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.google.firebase.FirebaseApp
import com.ozodbek.booknest.ui.nav.BookNestNavigation
import com.ozodbek.booknest.ui.theme.BookNestTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Initialize Firebase explicitly (safe even though it auto-initializes).
        FirebaseApp.initializeApp(this)
        enableEdgeToEdge()
        setContent {
            BookNestTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    BookNestNavigation()
                }
            }
        }
    }
}
