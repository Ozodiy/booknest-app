package com.ozodbek.booknest.util

/**
 * Pure validation logic with no Android/Firebase dependencies, so it can be
 * unit-tested directly on the JVM (Lab 3, Phase 3).
 *
 * Each function returns a [ValidationResult] rather than throwing, so the UI
 * layer can decide how to display the message.
 */
data class ValidationResult(val isValid: Boolean, val errorMessage: String? = null) {
    companion object {
        val VALID = ValidationResult(true)
        fun invalid(message: String) = ValidationResult(false, message)
    }
}

object Validators {

    private val EMAIL_REGEX =
        Regex("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")

    /** Firebase requires a minimum password length of 6 characters. */
    const val MIN_PASSWORD_LENGTH = 6

    fun validateEmail(rawEmail: String): ValidationResult {
        val email = rawEmail.trim()
        return when {
            email.isEmpty() -> ValidationResult.invalid("Email is required")
            !EMAIL_REGEX.matches(email) -> ValidationResult.invalid("Email address is badly formatted")
            else -> ValidationResult.VALID
        }
    }

    fun validatePassword(password: String): ValidationResult {
        return when {
            password.isEmpty() -> ValidationResult.invalid("Password is required")
            password.length < MIN_PASSWORD_LENGTH ->
                ValidationResult.invalid("Password must be at least $MIN_PASSWORD_LENGTH characters")
            else -> ValidationResult.VALID
        }
    }

    /** Used on the Register screen to confirm both password fields match. */
    fun validatePasswordsMatch(password: String, confirm: String): ValidationResult {
        return if (password != confirm) {
            ValidationResult.invalid("Passwords do not match")
        } else {
            ValidationResult.VALID
        }
    }

    /** Validates the "Add book" form (Lab 2 input-validation extension). */
    fun validateBookInput(
        title: String,
        author: String,
        description: String
    ): ValidationResult {
        return when {
            title.trim().isEmpty() -> ValidationResult.invalid("Title is required")
            title.trim().length < 2 -> ValidationResult.invalid("Title is too short")
            author.trim().isEmpty() -> ValidationResult.invalid("Author is required")
            description.trim().length > 500 ->
                ValidationResult.invalid("Description must be 500 characters or fewer")
            else -> ValidationResult.VALID
        }
    }
}
