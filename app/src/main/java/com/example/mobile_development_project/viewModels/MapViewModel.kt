package com.example.mobile_development_project.viewModels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.mobile_development_project.data.models.Location
import com.google.firebase.firestore.FirebaseFirestore

class MapViewModel : ViewModel() {

    private val db = FirebaseFirestore.getInstance()
    var approvedLocations by mutableStateOf<List<Location>>(emptyList())
        private set

    fun fetchApprovedLocations() {
        db.collection("locations")
            .whereEqualTo("status", "approved")
            .get()
            .addOnSuccessListener { result ->
                approvedLocations = result.documents.map {
                    Location(
                        id = it.id,
                        ownerId = it.getString("ownerId") ?: "",
                        ownerUsername = it.getString("ownerUsername") ?: "",
                        name = it.getString("name") ?: "",
                        description = it.getString("description") ?: "",
                        tags = it.get("tags") as? List<String> ?: emptyList(),
                        latitude = it.getDouble("latitude") ?: 0.0,
                        longitude = it.getDouble("longitude") ?: 0.0,
                        previewImageUrl = it.getString("previewImageUrl") ?: "",
                        status = it.getString("status") ?: "approved",
                        createdAt = it.getString("createdAt") ?: "",
                        updatedAt = it.getString("updatedAt") ?: "",
                        favoritesCount = it.getLong("favoritesCount")?.toInt() ?: 0
                    )
                }
            }
    }
}