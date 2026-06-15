package com.ozodbek.booknest.data

import com.google.firebase.Timestamp

/** Status values for an entry in a user's "Want to Read" list. */
object SaveStatus {
    const val WANT_TO_READ = "want-to-read"
    const val FINISHED = "finished"
}

/**
 * A join document in the Firestore `savedBooks` collection linking a user to a
 * book they bookmarked. The status can be toggled between want-to-read and
 * finished (Lab 2 "status toggle" extension).
 */
data class SavedBook(
    val savedId: String = "",
    val userId: String = "",
    val bookId: String = "",
    val title: String = "",
    val author: String = "",
    val status: String = SaveStatus.WANT_TO_READ,
    val savedAt: Timestamp? = null
)
