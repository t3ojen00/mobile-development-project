package com.example.mobile_development_project.viewModels

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.mobile_development_project.data.models.User
import com.example.mobile_development_project.data.models.Location
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import android.util.Log

class ProfileViewModel : ViewModel() {

    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()

    var user by mutableStateOf<User?>(null)
        private set

    var isLoading by mutableStateOf(false)
        private set

    var userLocations by mutableStateOf<List<Location>>(emptyList())
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
                        createdAt = doc.getString("createdAt"),
                        updatedAt = doc.getString("updatedAt"),
                        isActive = doc.getBoolean("isActive") ?: true
                    )
                }
                isLoading = false
            }
            .addOnFailureListener {
                isLoading = false
            }
    }

    fun loadUserLocations() {
        val uid = auth.currentUser?.uid
        Log.d("UserProfileVM", "Current UID: $uid")

        if (uid == null) return

        db.collection("locations")
            .whereEqualTo("ownerId", uid)
            .get()
            .addOnSuccessListener { result ->
                Log.d("UserProfileVM", "Found locations: ${result.documents.size}")

                userLocations = result.documents.map { doc ->
                    Log.d("UserProfileVM", "Doc id: ${doc.id}, name: ${doc.getString("name")}")

                    Location(
                        id = doc.id,
                        ownerId = doc.getString("ownerId") ?: "",
                        ownerUsername = doc.getString("ownerUsername") ?: "",
                        name = doc.getString("name") ?: "",
                        description = doc.getString("description") ?: "",
                        tags = doc.get("tags") as? List<String> ?: emptyList(),
                        latitude = doc.getDouble("latitude") ?: 0.0,
                        longitude = doc.getDouble("longitude") ?: 0.0,
                        previewImageUrl = doc.getString("previewImageUrl") ?: "",
                        status = doc.getString("status") ?: "pending",
                        createdAt = doc.getString("createdAt"),
                        updatedAt = doc.getString("updatedAt"),
                        favoritesCount = doc.getLong("favoritesCount")?.toInt() ?: 0
                    )
                }
            }
            .addOnFailureListener { e ->
                Log.e("UserProfileVM", "loadUserLocations failed", e)
            }
    }

    fun loadProfileData() {
        loadUser()
        loadUserLocations()
    }
}