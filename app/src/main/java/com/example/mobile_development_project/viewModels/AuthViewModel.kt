package com.example.mobile_development_project.viewModels

import androidx.lifecycle.ViewModel
import com.example.mobile_development_project.data.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.userProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.text.SimpleDateFormat
import java.util.Locale
import kotlin.String

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

    fun registerUser(email: String, password: String, nickname: String) {
        _registerState.value = AuthUiState(isLoading = true)

        auth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener { result ->
                val uid = result.user?.uid
                if (uid == null) {
                    _registerState.value = AuthUiState(
                        isLoading = false,
                        errorMessage = "User ID is null"
                    )
                    return@addOnSuccessListener
                }

                val profileUpdates = userProfileChangeRequest {
                    displayName = nickname
                }
                result.user?.updateProfile(profileUpdates)

                val formatter = SimpleDateFormat(
                    "dd.MM.yyyy HH:mm", Locale("fi", "FI")
                )
                val createdAtStr = formatter.format(java.util.Date())

                val userData = User (
                    id = uid,
                    email = email,
                    username = nickname,
                    displayName = nickname,
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
                        _registerState.value = AuthUiState(
                            isLoading = false,
                            errorMessage = error.message
                        )
                    }
            }
            .addOnFailureListener { error ->
                _registerState.value = AuthUiState(
                    isLoading = false,
                    errorMessage = error.message
                )
            }
    }

    fun loginUser(email: String, password: String) {
        _loginState.value = AuthUiState(isLoading = true)

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
                    errorMessage = error.message
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