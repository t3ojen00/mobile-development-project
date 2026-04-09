package com.example.mobile_development_project.viewModels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.mobile_development_project.data.models.Location
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

data class LocationDetailUiState(
    val isLoading: Boolean = false,
    val location: Location? = null,
    val errorMessage: String? = null,
    val isOwner: Boolean = false,
    val isFavorite: Boolean = false
)

class LocationDetailViewModel : ViewModel() {

    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    var uiState by mutableStateOf(LocationDetailUiState())
        private set

    fun loadLocation(locationId: String) {
        if (locationId.isBlank()) {
            uiState = uiState.copy(
                isLoading = false,
                location = null,
                errorMessage = "Location ID missing",
                isOwner = false
            )
            return
        }

        uiState = uiState.copy(
            isLoading = true,
            errorMessage = null
        )

        db.collection("locations")
            .document(locationId)
            .get()
            .addOnSuccessListener { document ->
                if (!document.exists()) {
                    uiState = uiState.copy(
                        isLoading = false,
                        location = null,
                        errorMessage = "Location not found",
                        isOwner = false
                    )
                    return@addOnSuccessListener
                }

                val loadedLocation = Location(
                    id = document.id,
                    ownerId = document.getString("ownerId") ?: "",
                    ownerUsername = document.getString("ownerUsername") ?: "",
                    name = document.getString("name") ?: "",
                    description = document.getString("description") ?: "",
                    tags = document.get("tags") as? List<String> ?: emptyList(),
                    latitude = document.getDouble("latitude") ?: 0.0,
                    longitude = document.getDouble("longitude") ?: 0.0,
                    previewImageUrl = document.getString("previewImageUrl") ?: "",
                    status = document.getString("status") ?: "pending",
                    createdAt = document.getString("createdAt")?: "",
                    updatedAt = document.getLong("updatedAt")?.toString(),
                    favoritesCount = document.getLong("favoritesCount")?.toInt() ?: 0
                )

                val currentUserId = auth.currentUser?.uid
                val isOwner = currentUserId == loadedLocation.ownerId

                uiState = uiState.copy(
                    isLoading = false,
                    location = loadedLocation,
                    errorMessage = null,
                    isOwner = isOwner
                )
            }
            .addOnFailureListener { e ->
                uiState = uiState.copy(
                    isLoading = false,
                    location = null,
                    errorMessage = e.message ?: "Failed to load location",
                    isOwner = false
                )
            }
    }

    fun toggleFavorite() {
        uiState = uiState.copy(
            isFavorite = !uiState.isFavorite
        )
    }
}