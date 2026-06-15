package com.ozodbek.booknest.data

import com.google.firebase.Timestamp

/**
 * A registered student, stored in the Firestore `users` collection.
 * Named `AppUser` to avoid clashing with FirebaseAuth's `FirebaseUser`.
 */
data class AppUser(
    val userId: String = "",
    val email: String = "",
    val displayName: String = "",
    val university: String = "",
    val createdAt: Timestamp? = null
)
