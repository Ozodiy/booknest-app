package com.ozodbek.booknest

import com.ozodbek.booknest.util.Validators
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * Unit tests for the pure validation logic (Lab 3, Phase 3).
 * These run on the local JVM — no emulator or Firebase required.
 */
class ValidatorsTest {

    // ---- Email -------------------------------------------------------------

    @Test
    fun validEmail_passes() {
        assertTrue(Validators.validateEmail("ozodbek@wsb.edu.pl").isValid)
    }

    @Test
    fun emailWithoutAt_fails() {
        val result = Validators.validateEmail("ozodbek.wsb.edu.pl")
        assertFalse(result.isValid)
        assertEquals("Email address is badly formatted", result.errorMessage)
    }

    @Test
    fun emptyEmail_failsWithRequiredMessage() {
        val result = Validators.validateEmail("   ")
        assertFalse(result.isValid)
        assertEquals("Email is required", result.errorMessage)
    }

    @Test
    fun emailWithSurroundingSpaces_isTrimmedAndPasses() {
        assertTrue(Validators.validateEmail("  student@uni.com  ").isValid)
    }

    // ---- Password ----------------------------------------------------------

    @Test
    fun shortPassword_fails() {
        val result = Validators.validatePassword("123")
        assertFalse(result.isValid)
        assertEquals("Password must be at least 6 characters", result.errorMessage)
    }

    @Test
    fun sixCharPassword_passes() {
        assertTrue(Validators.validatePassword("secret").isValid)
    }

    @Test
    fun mismatchedPasswords_fail() {
        val result = Validators.validatePasswordsMatch("secret1", "secret2")
        assertFalse(result.isValid)
        assertEquals("Passwords do not match", result.errorMessage)
    }

    // ---- Book input --------------------------------------------------------

    @Test
    fun validBookInput_passes() {
        val result = Validators.validateBookInput(
            title = "Clean Code",
            author = "Robert C. Martin",
            description = "A handbook of agile software craftsmanship."
        )
        assertTrue(result.isValid)
    }

    @Test
    fun blankTitle_failsBookInput() {
        val result = Validators.validateBookInput("  ", "Author", "desc")
        assertFalse(result.isValid)
        assertEquals("Title is required", result.errorMessage)
    }

    @Test
    fun overlongDescription_failsBookInput() {
        val longText = "x".repeat(501)
        val result = Validators.validateBookInput("Title", "Author", longText)
        assertFalse(result.isValid)
        assertEquals("Description must be 500 characters or fewer", result.errorMessage)
    }
}
