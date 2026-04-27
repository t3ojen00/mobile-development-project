package com.example.mobile_development_project.viewModels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.mobile_development_project.data.models.Location
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.FirebaseFirestore

class FavoritesViewModel : ViewModel() {

    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()

    var favoriteLocations by mutableStateOf<List<Location>>(emptyList())
        private set

    var isLoading by mutableStateOf(false)
        private set

    fun loadFavorites() {
        val uid = auth.currentUser?.uid ?: return

        isLoading = true

        db.collection("users")
            .document(uid)
            .collection("favorites")
            .get()
            .addOnSuccessListener { result ->

                val locationIds = result.documents.mapNotNull {
                    it.getString("locationId")
                }
                if (locationIds.isEmpty()) {
                    isLoading = false
                    return@addOnSuccessListener
                }

                db.collection("locations")
                    .whereIn(FieldPath.documentId(), locationIds)
                    .get()
                    .addOnSuccessListener { locationsResult ->

                        favoriteLocations = locationsResult.documents.map { doc ->
                            Location(
                                id = doc.getString("locationId") ?: doc.id,
                                ownerId = doc.getString("ownerId") ?: "",
                                ownerUsername = doc.getString("ownerUsername") ?: "",
                                name = doc.getString("name") ?: "",
                                description = "",
                                tags = emptyList(),
                                latitude = 0.0,
                                longitude = 0.0,
                                previewImageUrl = doc.getString("previewImageUrl") ?: "",
                                status = "approved",
                                createdAt = null,
                                updatedAt = null,
                                favoritesCount = 0
                            )
                        }

                        isLoading = false
                    }
                    .addOnFailureListener {
                        isLoading = false
                    }
            }
    }
}