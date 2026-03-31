package com.example.mobile_development_project.viewModels

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

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

    fun registerUser(email: String, password: String) {
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

                val userData = hashMapOf(
                    "uid" to uid,
                    "email" to email
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