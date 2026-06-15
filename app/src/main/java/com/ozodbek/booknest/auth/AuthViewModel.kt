package com.ozodbek.booknest.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.ozodbek.booknest.data.AppUser
import com.ozodbek.booknest.util.Validators
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

/** UI state for the authentication screens. */
data class AuthUiState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val isAuthenticated: Boolean = false
)

/**
 * Owns all Firebase Authentication logic (Lab 2 Phase 2): register, login,
 * logout, plus startup session check. On registration it also creates the
 * matching document in the Firestore `users` collection.
 */
class AuthViewModel : ViewModel() {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()

    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    val currentUserId: String? get() = auth.currentUser?.uid
    val currentUserName: String
        get() = auth.currentUser?.displayName
            ?: auth.currentUser?.email?.substringBefore("@")
            ?: "Student"

    init {
        // Startup check — skip the login screen if a session already exists.
        if (auth.currentUser != null) {
            _uiState.value = _uiState.value.copy(isAuthenticated = true)
        }
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(errorMessage = null)
    }

    fun register(email: String, password: String, confirm: String, university: String) {
        // Client-side validation first, so we fail fast with clear messages.
        Validators.validateEmail(email).errorMessage?.let { return fail(it) }
        Validators.validatePassword(password).errorMessage?.let { return fail(it) }
        Validators.validatePasswordsMatch(password, confirm).errorMessage?.let { return fail(it) }

        _uiState.value = AuthUiState(isLoading = true)
        viewModelScope.launch {
            try {
                val result = auth.createUserWithEmailAndPassword(email.trim(), password).await()
                val uid = result.user?.uid ?: error("Missing user id after sign-up")
                val displayName = email.trim().substringBefore("@")
                val user = AppUser(
                    userId = uid,
                    email = email.trim(),
                    displayName = displayName,
                    university = university.ifBlank { "Akademia WSB" },
                    createdAt = Timestamp.now()
                )
                db.collection("users").document(uid).set(user).await()
                _uiState.value = AuthUiState(isAuthenticated = true)
            } catch (e: Exception) {
                fail(friendlyMessage(e))
            }
        }
    }

    fun login(email: String, password: String) {
        Validators.validateEmail(email).errorMessage?.let { return fail(it) }
        if (password.isEmpty()) return fail("Password is required")

        _uiState.value = AuthUiState(isLoading = true)
        viewModelScope.launch {
            try {
                auth.signInWithEmailAndPassword(email.trim(), password).await()
                _uiState.value = AuthUiState(isAuthenticated = true)
            } catch (e: Exception) {
                fail(friendlyMessage(e))
            }
        }
    }

    fun logout() {
        auth.signOut()
        _uiState.value = AuthUiState(isAuthenticated = false)
    }

    private fun fail(message: String) {
        _uiState.value = AuthUiState(isLoading = false, errorMessage = message)
    }

    /** Maps raw Firebase exceptions to user-friendly text (Lab 2 Phase 2.5). */
    private fun friendlyMessage(e: Exception): String {
        val raw = e.message ?: "Something went wrong. Please try again."
        return when {
            raw.contains("password is invalid", true) ||
                raw.contains("INVALID_LOGIN_CREDENTIALS", true) ->
                "Incorrect email or password"
            raw.contains("no user record", true) -> "No account found with that email"
            raw.contains("email address is already in use", true) ->
                "An account with this email already exists"
            raw.contains("network", true) -> "Network error — check your connection"
            else -> raw
        }
    }
}
