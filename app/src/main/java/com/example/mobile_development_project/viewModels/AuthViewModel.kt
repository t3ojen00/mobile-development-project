package com.example.mobile_development_project.viewModels

import androidx.lifecycle.ViewModel
import com.example.mobile_development_project.data.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.userProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class AuthUiState(
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val errorMessage: String? = null
)

class AuthViewModel : ViewModel() {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()

    private val _registerState = MutableStateFlow(AuthUiState())
    val registerState: StateFlow<AuthUiState> = _registerState

    private val _loginState = MutableStateFlow(AuthUiState())
    val loginState: StateFlow<AuthUiState> = _loginState

    fun isUserLoggedIn(): Boolean {
        return auth.currentUser != null
    }

    fun logoutUser() {
        auth.signOut()
        resetLoginState()
        resetRegisterState()
    }

    fun registerUser(email: String, password: String, nickname: String) {
        val trimmedEmail = email.trim()
        val trimmedNickname = nickname.trim()
        val normalizedUsername = trimmedNickname.lowercase(Locale.ROOT)

        _registerState.value = AuthUiState(isLoading = true)

        firestore.collection("users")
            .whereEqualTo("username", normalizedUsername)
            .get()
            .addOnSuccessListener { documents ->
                if (!documents.isEmpty) {
                    _registerState.value = AuthUiState(
                        isLoading = false,
                        errorMessage = "This username is already taken"
                    )
                    return@addOnSuccessListener
                }

                auth.createUserWithEmailAndPassword(trimmedEmail, password)
                    .addOnSuccessListener { result ->
                        val firebaseUser = result.user
                        val uid = firebaseUser?.uid

                        if (uid == null) {
                            _registerState.value = AuthUiState(
                                isLoading = false,
                                errorMessage = "User ID is null"
                            )
                            return@addOnSuccessListener
                        }

                        val profileUpdates = userProfileChangeRequest {
                            displayName = trimmedNickname
                        }

                        firebaseUser.updateProfile(profileUpdates)
                            .addOnCompleteListener {
                                val formatter = SimpleDateFormat(
                                    "dd.MM.yyyy HH:mm",
                                    Locale("fi", "FI")
                                )
                                val createdAtStr = formatter.format(Date())

                                val userData = User(
                                    id = uid,
                                    email = trimmedEmail,
                                    username = normalizedUsername,
                                    displayName = trimmedNickname,
                                    role = "user",
                                    createdAt = createdAtStr,
                                    isActive = true
                                )

                                firestore.collection("users")
                                    .document(uid)
                                    .set(userData)
                                    .addOnSuccessListener {
                                        _registerState.value = AuthUiState(
                                            isLoading = false,
                                            isSuccess = true
                                        )
                                    }
                                    .addOnFailureListener { error ->
                                        firebaseUser.delete()

                                        _registerState.value = AuthUiState(
                                            isLoading = false,
                                            errorMessage = error.message
                                                ?: "Failed to save user data"
                                        )
                                    }
                            }
                    }
                    .addOnFailureListener { error ->
                        _registerState.value = AuthUiState(
                            isLoading = false,
                            errorMessage = error.message ?: "Registration failed"
                        )
                    }
            }
            .addOnFailureListener { error ->
                _registerState.value = AuthUiState(
                    isLoading = false,
                    errorMessage = error.message ?: "Failed to validate username"
                )
            }
    }

    fun loginUser(loginInput: String, password: String) {
        val trimmedInput = loginInput.trim()
        val normalizedInput = trimmedInput.lowercase(Locale.ROOT)

        _loginState.value = AuthUiState(isLoading = true)

        if (trimmedInput.contains("@")) {
            signInWithEmail(trimmedInput, password)
            return
        }

        firestore.collection("users")
            .whereEqualTo("username", normalizedInput)
            .limit(1)
            .get()
            .addOnSuccessListener { documents ->
                if (documents.isEmpty) {
                    _loginState.value = AuthUiState(
                        isLoading = false,
                        errorMessage = "User with this username was not found"
                    )
                    return@addOnSuccessListener
                }

                val email = documents.documents.firstOrNull()?.getString("email")
                if (email.isNullOrBlank()) {
                    _loginState.value = AuthUiState(
                        isLoading = false,
                        errorMessage = "Email for this username was not found"
                    )
                    return@addOnSuccessListener
                }

                signInWithEmail(email, password)
            }
            .addOnFailureListener { error ->
                _loginState.value = AuthUiState(
                    isLoading = false,
                    errorMessage = error.message ?: "Login failed"
                )
            }
    }

    private fun signInWithEmail(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                _loginState.value = AuthUiState(
                    isLoading = false,
                    isSuccess = true
                )
            }
            .addOnFailureListener { error ->
                _loginState.value = AuthUiState(
                    isLoading = false,
                    errorMessage = error.message ?: "Invalid email/username or password"
                )
            }
    }

    fun resetRegisterState() {
        _registerState.value = AuthUiState()
    }

    fun resetLoginState() {
        _loginState.value = AuthUiState()
    }
}