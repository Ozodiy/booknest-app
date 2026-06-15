package com.ozodbek.booknest.ui.nav

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.ozodbek.booknest.auth.AuthViewModel
import com.ozodbek.booknest.ui.screens.*
import com.ozodbek.booknest.viewmodel.BookViewModel

/** Route names kept in one place to avoid stringly-typed typos. */
object Routes {
    const val LOGIN = "login"
    const val REGISTER = "register"
    const val LIST = "list"
    const val ADD = "add"
    const val SAVED = "saved"
    const val DETAIL = "detail/{bookId}"
    fun detail(bookId: String) = "detail/$bookId"
}

@Composable
fun BookNestNavigation(
    authViewModel: AuthViewModel = viewModel(),
    bookViewModel: BookViewModel = viewModel()
) {
    val navController = rememberNavController()
    val authState by authViewModel.uiState.collectAsState()

    // Whenever auth status flips, jump to the right top-level screen and clear
    // the back stack so the system Back button can't return to login/list.
    LaunchedEffect(authState.isAuthenticated) {
        if (authState.isAuthenticated) {
            authViewModel.currentUserId?.let { bookViewModel.observeSavedBooks(it) }
            navController.navigate(Routes.LIST) {
                popUpTo(0) { inclusive = true }
            }
        } else {
            navController.navigate(Routes.LOGIN) {
                popUpTo(0) { inclusive = true }
            }
        }
    }

    val start = if (authState.isAuthenticated) Routes.LIST else Routes.LOGIN

    NavHost(navController = navController, startDestination = start) {

        composable(Routes.LOGIN) {
            LoginScreen(
                state = authState,
                onLogin = { e, p -> authViewModel.login(e, p) },
                onNavigateToRegister = {
                    authViewModel.clearError()
                    navController.navigate(Routes.REGISTER)
                }
            )
        }

        composable(Routes.REGISTER) {
            RegisterScreen(
                state = authState,
                onRegister = { e, p, c, u -> authViewModel.register(e, p, c, u) },
                onBackToLogin = {
                    authViewModel.clearError()
                    navController.popBackStack()
                }
            )
        }

        composable(Routes.LIST) {
            val listState by bookViewModel.uiState.collectAsState()
            BookListScreen(
                state = listState,
                isSaved = { bookViewModel.isSaved(it) },
                onQueryChange = bookViewModel::onQueryChange,
                onSortChange = bookViewModel::onSortChange,
                onToggleSaved = { book ->
                    authViewModel.currentUserId?.let { bookViewModel.toggleSaved(it, book) }
                },
                onOpenBook = { id -> navController.navigate(Routes.detail(id)) },
                onAddBook = { navController.navigate(Routes.ADD) },
                onOpenSaved = { navController.navigate(Routes.SAVED) },
                onLogout = { authViewModel.logout() }
            )
        }

        composable(Routes.ADD) {
            val progress by bookViewModel.uploadProgress.collectAsState()
            AddBookScreen(
                uploadProgress = progress,
                onSave = { title, author, desc, genre, coverUri ->
                    bookViewModel.addBook(
                        title = title,
                        author = author,
                        description = desc,
                        genre = genre,
                        recommendedBy = authViewModel.currentUserId.orEmpty(),
                        recommenderName = authViewModel.currentUserName,
                        coverUri = coverUri
                    ) { ok -> if (ok) navController.popBackStack() }
                },
                onBack = { navController.popBackStack() }
            )
        }

        composable(Routes.SAVED) {
            val saved by bookViewModel.savedBooks.collectAsState()
            SavedBooksScreen(
                saved = saved,
                onToggleStatus = bookViewModel::toggleSavedStatus,
                onBack = { navController.popBackStack() }
            )
        }

        composable(Routes.DETAIL) { entry ->
            val bookId = entry.arguments?.getString("bookId").orEmpty()
            val book = bookViewModel.getBook(bookId)
            if (book == null) {
                navController.popBackStack()
            } else {
                BookDetailScreen(
                    book = book,
                    currentUserId = authViewModel.currentUserId.orEmpty(),
                    onSave = { t, a, d, g ->
                        bookViewModel.updateBook(bookId, t, a, d, g) { ok ->
                            if (ok) navController.popBackStack()
                        }
                    },
                    onDelete = {
                        bookViewModel.deleteBook(book) { ok ->
                            if (ok) navController.popBackStack()
                        }
                    },
                    onBack = { navController.popBackStack() }
                )
            }
        }
    }
}
