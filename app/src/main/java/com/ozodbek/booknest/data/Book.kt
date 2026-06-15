package com.ozodbek.booknest.data

import com.google.firebase.Timestamp

/**
 * A book recommendation stored in the Firestore `books` collection.
 *
 * All fields have default values and there is an implicit no-arg constructor,
 * which Firestore requires to deserialize documents with `toObject<Book>()`.
 *
 * `coverImageUrl` was added in Lab 3 — it holds the Firebase Storage download
 * URL for the uploaded cover image (empty when no cover was provided).
 */
data class Book(
    val bookId: String = "",
    val title: String = "",
    val author: String = "",
    val description: String = "",
    val genre: String = "",
    val coverImageUrl: String = "",
    val recommendedBy: String = "",      // userId of the recommender (owner)
    val recommenderName: String = "",    // display name shown in the UI
    val createdAt: Timestamp? = null
)
