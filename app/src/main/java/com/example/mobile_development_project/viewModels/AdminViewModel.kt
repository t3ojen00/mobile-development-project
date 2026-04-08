package com.example.mobile_development_project.viewModels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.mobile_development_project.data.models.ErrorCause
import com.example.mobile_development_project.data.models.Location
import com.example.mobile_development_project.data.models.MsgType
import com.google.firebase.firestore.FirebaseFirestore

class AdminViewModel : ViewModel() {
    private val db = FirebaseFirestore.getInstance()
    var uiMessage by mutableStateOf<Triple<String, MsgType, ErrorCause?>?>(null)
        private set
    var pendingLocations by mutableStateOf<List<Location>>(emptyList())
        private set

    fun fetchPendingLocations() {
        db.collection("locations")
            .whereEqualTo("status", "pending")
            .get()
            .addOnSuccessListener { result ->
                pendingLocations = result.documents.map {
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
                        status = it.getString("status") ?: "pending",
                        createdAt = it.getString("createdAt") ?: "",
                        updatedAt = it.getLong("updatedAt")?.toString() ?: "",
                        favoritesCount = it.getLong("favoritesCount")?.toInt() ?: 0
                        )
                }
            }
            .addOnFailureListener { exception ->
                uiMessage = Triple(
                    "Error fetching pending locations",
                    MsgType.ERROR,
                    ErrorCause.GENERAL_FETCH_FAIL
                )
            }
    }
    fun approveLocation(locationId: String) {
        db.collection("locations")
            .document(locationId)
            .update("status", "approved")
            .addOnSuccessListener {
                uiMessage = Triple(
                    "Location approved",
                    MsgType.SUCCESS,
                    null
                )
            }
    }
}