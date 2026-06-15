package com.ozodbek.booknest.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ozodbek.booknest.auth.AuthUiState

@Composable
fun RegisterScreen(
    state: AuthUiState,
    onRegister: (email: String, password: String, confirm: String, university: String) -> Unit,
    onBackToLogin: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirm by remember { mutableStateOf("") }
    var university by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Create account", fontSize = 28.sp, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(24.dp))

        OutlinedTextField(
            value = email, onValueChange = { email = it },
            label = { Text("Email") }, singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(12.dp))
        OutlinedTextField(
            value = password, onValueChange = { password = it },
            label = { Text("Password (min 6 chars)") }, singleLine = true,
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(12.dp))
        OutlinedTextField(
            value = confirm, onValueChange = { confirm = it },
            label = { Text("Confirm password") }, singleLine = true,
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(12.dp))
        OutlinedTextField(
            value = university, onValueChange = { university = it },
            label = { Text("University (optional)") }, singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        if (state.errorMessage != null) {
            Spacer(Modifier.height(12.dp))
            Text(state.errorMessage, color = MaterialTheme.colorScheme.error)
        }

        Spacer(Modifier.height(24.dp))
        Button(
            onClick = { onRegister(email, password, confirm, university) },
            enabled = !state.isLoading,
            modifier = Modifier.fillMaxWidth()
        ) {
            if (state.isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(20.dp), strokeWidth = 2.dp,
                    color = MaterialTheme.colorScheme.onPrimary
                )
            } else Text("Create account")
        }
        TextButton(onClick = onBackToLogin) { Text("Back to login") }
    }
}
