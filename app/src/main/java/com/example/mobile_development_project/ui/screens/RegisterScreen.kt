package com.example.mobile_development_project.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mobile_development_project.ui.theme.AuthCardGray
import com.example.mobile_development_project.ui.theme.Burgundy
import com.example.mobile_development_project.ui.theme.OrangeAccent
import com.example.mobile_development_project.ui.theme.ScreenBackground
import com.example.mobile_development_project.viewModels.AuthViewModel

@Composable
fun RegisterScreen(
    onBackClick: () -> Unit = {},
    onRegisterSuccess: () -> Unit = {},
    authViewModel: AuthViewModel = viewModel()
) {
    var nickname by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var localError by remember { mutableStateOf<String?>(null) }
    var showSuccessDialog by remember { mutableStateOf(false) }

    val registerState by authViewModel.registerState.collectAsState()

    LaunchedEffect(registerState.isSuccess) {
        if (registerState.isSuccess) {
            showSuccessDialog = true
        }
    }

    if (showSuccessDialog) {
        AlertDialog(
            onDismissRequest = {
                showSuccessDialog = false
                authViewModel.resetRegisterState()
                onRegisterSuccess()
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        showSuccessDialog = false
                        authViewModel.resetRegisterState()
                        onRegisterSuccess()
                    }
                ) {
                    Text("OK", color = OrangeAccent)
                }
            },
            title = { Text("Success") },
            text = { Text("Account created successfully. Now you can sign in.") }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ScreenBackground)
            .statusBarsPadding()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Burgundy)
                .padding(horizontal = 8.dp, vertical = 12.dp)
        ) {
            IconButton(
                onClick = onBackClick,
                modifier = Modifier.align(Alignment.CenterStart)
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    tint = Color.White
                )
            }

            Text(
                text = "Register",
                color = Color.White,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.align(Alignment.Center)
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                color = AuthCardGray,
                shadowElevation = 6.dp
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Register",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "\uD83D\uDCF8",
                        style = MaterialTheme.typography.headlineSmall
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    AuthField(
                        value = nickname,
                        onValueChange = {
                            nickname = it
                            localError = null
                        },
                        label = "Username / nickname"
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    AuthField(
                        value = email,
                        onValueChange = {
                            email = it
                            localError = null
                        },
                        label = "Email"
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    AuthField(
                        value = password,
                        onValueChange = {
                            password = it
                            localError = null
                        },
                        label = "Password",
                        isPassword = true
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    val errorText = localError ?: registerState.errorMessage
                    if (!errorText.isNullOrBlank()) {
                        Text(
                            text = errorText,
                            color = Color(0xFFFFD6D6),
                            style = MaterialTheme.typography.bodySmall
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                    }

                    Button(
                        onClick = {
                            val trimmedNickname = nickname.trim()
                            val trimmedEmail = email.trim()
                            val trimmedPassword = password.trim()

                            localError = when {
                                trimmedNickname.isBlank() -> "Username is required"
                                trimmedNickname.length < 3 -> "Username must be at least 3 characters"
                                trimmedEmail.isBlank() -> "Email is required"
                                trimmedPassword.isBlank() -> "Password is required"
                                trimmedPassword.length < 6 -> "Password must be at least 6 characters"
                                else -> null
                            }

                            if (localError == null) {
                                authViewModel.registerUser(
                                    email = trimmedEmail,
                                    password = trimmedPassword,
                                    nickname = trimmedNickname
                                )
                            }
                        },
                        enabled = !registerState.isLoading,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp),
                        shape = RoundedCornerShape(24.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Burgundy,
                            contentColor = Color.White
                        )
                    ) {
                        if (registerState.isLoading) {
                            CircularProgressIndicator(
                                color = Color.White,
                                strokeWidth = 2.dp
                            )
                        } else {
                            Text("Register")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun AuthField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    isPassword: Boolean = false
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = {
            Text(
                text = label,
                color = Color.Gray
            )
        },
        singleLine = true,
        visualTransformation = if (isPassword) {
            PasswordVisualTransformation()
        } else {
            androidx.compose.ui.text.input.VisualTransformation.None
        },
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(10.dp),
        colors = OutlinedTextFieldDefaults.colors(
            unfocusedContainerColor = Color.White,
            focusedContainerColor = Color.White,
            unfocusedBorderColor = Color.Transparent,
            focusedBorderColor = OrangeAccent,
            cursorColor = Burgundy
        )
    )
}