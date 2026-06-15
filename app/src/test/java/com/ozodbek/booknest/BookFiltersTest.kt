package com.ozodbek.booknest

import com.google.firebase.Timestamp
import com.ozodbek.booknest.data.Book
import com.ozodbek.booknest.util.BookFilters
import com.ozodbek.booknest.util.SortOption
import org.junit.Assert.assertEquals
import org.junit.Test
import java.util.Date

/**
 * Unit tests for the search/sort business logic that powers the Lab 2
 * extensions (Lab 3, Phase 3).
 */
class BookFiltersTest {

    private fun book(
        title: String,
        author: String,
        genre: String,
        epochSeconds: Long = 0L
    ) = Book(
        bookId = title,
        title = title,
        author = author,
        genre = genre,
        createdAt = Timestamp(Date(epochSeconds * 1000))
    )

    private val sample = listOf(
        book("Dune", "Frank Herbert", "Science Fiction", epochSeconds = 100),
        book("Clean Code", "Robert Martin", "Programming", epochSeconds = 300),
        book("Sapiens", "Yuval Harari", "History", epochSeconds = 200)
    )

    @Test
    fun search_byTitle_isCaseInsensitive() {
        val result = BookFilters.search(sample, "dune")
        assertEquals(1, result.size)
        assertEquals("Dune", result.first().title)
    }

    @Test
    fun search_byAuthor_matches() {
        val result = BookFilters.search(sample, "harari")
        assertEquals(listOf("Sapiens"), result.map { it.title })
    }

    @Test
    fun search_byGenre_matches() {
        val result = BookFilters.search(sample, "programming")
        assertEquals(listOf("Clean Code"), result.map { it.title })
    }

    @Test
    fun blankQuery_returnsAll() {
        assertEquals(sample.size, BookFilters.search(sample, "   ").size)
    }

    @Test
    fun sort_byTitleAscending() {
        val result = BookFilters.sort(sample, SortOption.TITLE_ASC)
        assertEquals(listOf("Clean Code", "Dune", "Sapiens"), result.map { it.title })
    }

    @Test
    fun sort_newestFirst_usesTimestamp() {
        val result = BookFilters.sort(sample, SortOption.NEWEST_FIRST)
        assertEquals(listOf("Clean Code", "Sapiens", "Dune"), result.map { it.title })
    }

    @Test
    fun searchAndSort_combineCorrectly() {
        // "s" matches Dune (genre "Science Fiction") and Sapiens (title), but
        // not Clean Code. The result is then sorted by title ascending.
        val result = BookFilters.searchAndSort(sample, "s", SortOption.TITLE_ASC)
        assertEquals(listOf("Dune", "Sapiens"), result.map { it.title })
    }
}
