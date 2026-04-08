package com.example.mobile_development_project.viewModels

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.mobile_development_project.data.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

class ProfileViewModel : ViewModel() {

    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()

    var user by mutableStateOf<User?>(null)
        private set

    var isLoading by mutableStateOf(false)
        private set

    fun loadUser() {
        val uid = auth.currentUser?.uid ?: return

        isLoading = true

        db.collection("users")
            .document(uid)
            .get()
            .addOnSuccessListener { doc ->
                if (doc.exists()) {
                    user = User(
                        id = uid,
                        email = doc.getString("email") ?: "",
                        username = doc.getString("username") ?: "",
                        displayName = doc.getString("displayName") ?: "",
                        role = doc.getString("role") ?: "user",
                        createdAt = doc.getLong("createdAt"),
                        updatedAt = doc.getLong("updatedAt"),
                        isActive = doc.getBoolean("isActive") ?: true
                    )
                }
                isLoading = false
            }
            .addOnFailureListener {
                isLoading = false
            }
    }
}