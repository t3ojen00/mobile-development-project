package com.example.mobile_development_project.viewModels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.mobile_development_project.data.models.Location
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
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

        val currentUserId = auth.currentUser?.uid

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
                    latitude = document.getDouble("latitude")
                        ?: document.getDouble("lat")
                        ?: 0.0,
                    longitude = document.getDouble("longitude")
                        ?: document.getDouble("lng")
                        ?: 0.0,
                    previewImageUrl = document.getString("previewImageUrl") ?: "",
                    status = document.getString("status") ?: "pending",
                    createdAt = document.getString("createdAt")?: "",
                    updatedAt = document.getLong("updatedAt")?.toString(),
                    favoritesCount = document.getLong("favoritesCount")?.toInt() ?: 0
                )

                val isOwner = currentUserId == loadedLocation.ownerId
                val currentUserId = auth.currentUser?.uid


                if (currentUserId == null) {
                    uiState = uiState.copy(
                        isLoading = false,
                        location = loadedLocation,
                        errorMessage = null,
                        isOwner = isOwner,
                        isFavorite = false
                    )
                    return@addOnSuccessListener
                }

                db.collection("users")
                    .document(currentUserId)
                    .collection("favorites")
                    .document(locationId)
                    .get()
                    .addOnSuccessListener { favoriteDoc ->
                        uiState = uiState.copy(
                            isLoading = false,
                            location = loadedLocation,
                            errorMessage = null,
                            isOwner = isOwner,
                            isFavorite = favoriteDoc.exists()
                        )
                    }
                    .addOnFailureListener {
                        uiState = uiState.copy(
                            isLoading = false,
                            location = loadedLocation,
                            errorMessage = null,
                            isOwner = isOwner,
                            isFavorite = false
                        )
                    }
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
        val currentUserId = auth.currentUser?.uid ?: return
        val location = uiState.location ?: return
        val locationId = location.id

        val favoriteRef = db.collection("users")
            .document(currentUserId)
            .collection("favorites")
            .document(locationId)

        val locationRef = db.collection("locations").document(locationId)

        val currentlyFavorite = uiState.isFavorite

        if (currentlyFavorite) {
            favoriteRef.delete()
                .addOnSuccessListener {
                    locationRef.update("favoritesCount", FieldValue.increment(-1))
                    uiState = uiState.copy(
                        isFavorite = false,
                        location = uiState.location?.copy(
                            favoritesCount = (uiState.location?.favoritesCount ?: 1) - 1
                        )
                    )
                }
        } else {
            val favoriteData = hashMapOf(
                "locationId" to location.id,
                "name" to location.name,
                "previewImageUrl" to location.previewImageUrl,
                "ownerId" to location.ownerId,
                "ownerUsername" to location.ownerUsername,
                "createdAt" to FieldValue.serverTimestamp()
            )

            favoriteRef.set(favoriteData)
                .addOnSuccessListener {
                    locationRef.update("favoritesCount", FieldValue.increment(1))
                    uiState = uiState.copy(
                        isFavorite = true,
                        location = uiState.location?.copy(
                            favoritesCount = (uiState.location?.favoritesCount ?: 0) + 1
                        )
                    )
                }
        }
    }
}