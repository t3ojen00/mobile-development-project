package com.example.mobile_development_project.viewModels

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.mobile_development_project.data.models.ErrorCause
import com.example.mobile_development_project.data.models.Location
import com.example.mobile_development_project.data.models.MsgType
import com.example.mobile_development_project.helpers.ErrorMapper
import com.google.firebase.firestore.FirebaseFirestore
import com.example.mobile_development_project.data.models.User
import com.google.firebase.auth.FirebaseAuth
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class AdminViewModel : ViewModel() {
    private val db = FirebaseFirestore.getInstance()
    var uiMessage by mutableStateOf<Triple<String, MsgType, ErrorCause?>?>(null)
        private set
    var pendingLocations by mutableStateOf<List<Location>>(emptyList())
        private set
    var rejectedLocations by mutableStateOf<List<Location>>(emptyList())
        private set
    var allUsers by mutableStateOf<List<User>>(emptyList())
        private set
    val currentUserId = FirebaseAuth.getInstance().currentUser?.uid
    fun isCurrentUser(userId: String): Boolean {
        return userId == currentUserId
    }
    private val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")

    fun setError(
        cause: ErrorCause? = null,
        exception: Exception? = null) {
        Log.e("AdminViewModel", "ERROR", exception)

        uiMessage = Triple(
            ErrorMapper.getMessage(cause),
            MsgType.ERROR,
            cause
        )
    }
    fun fetchPendingLocations() {
        db.collection("locations")
            .whereEqualTo("status", "pending")
            .get()
            .addOnSuccessListener { result ->
                pendingLocations = result.documents
                    .map { doc ->
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
                            createdAt = doc.getString("createdAt") ?: "",
                            updatedAt = doc.getLong("updatedAt")?.toString() ?: "",
                            favoritesCount = doc.getLong("favoritesCount")?.toInt() ?: 0
                            )
                    }
                    .sortedBy { location ->
                        LocalDateTime.parse(location.createdAt, formatter)
                    }
            }
            .addOnFailureListener { exception ->
                setError(ErrorCause.GENERAL_FETCH_FAIL, exception)
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
    fun rejectLocation(locationId: String) {
        db.collection("locations")
            .document(locationId)
            .update("status", "rejected")
            .addOnSuccessListener {
                uiMessage = Triple(
                    "Location rejected",
                    MsgType.SUCCESS,
                    null
                )
            }
    }
    fun fetchAllUsers()
    {
        db.collection("users")
            .get()
            .addOnSuccessListener { result ->
                allUsers = result.documents.map {
                    User(
                        id = it.id,
                        email = it.getString("email") ?: "",
                        username = it.getString("username") ?: "",
                        displayName = it.getString("displayName") ?: "",
                        role = it.getString("role") ?: "user",
                        createdAt = it.getString("createdAt"),
                        updatedAt = it.getString("updatedAt"),
                        isActive = it.getBoolean("isActive") ?: true
                    )
                }
            }
            .addOnFailureListener { exception ->
                setError(ErrorCause.GENERAL_FETCH_FAIL, exception)
            }
    }
    fun updateUserRole(userId: String, newRole: String) {
        db.collection("users")
            .document(userId)
            .update("role", newRole)
            .addOnSuccessListener {
                uiMessage = Triple(
                    "User role updated",
                    MsgType.SUCCESS,
                    null
                )
                fetchAllUsers()
            }
            .addOnFailureListener { exception ->
                setError(ErrorCause.GENERAL_UPDATE_FAIL, exception)
            }
    }

    fun fetchRejectedLocations() {
        db.collection("locations")
            .whereEqualTo("status", "rejected")
            .get()
            .addOnSuccessListener { result ->
                rejectedLocations = result.documents
                    .map { doc ->
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
                            createdAt = doc.getString("createdAt") ?: "",
                        )
                    }
                    .sortedBy { location ->
                        LocalDateTime.parse(location.createdAt, formatter)
                    }
            }
            .addOnFailureListener { exception ->
                setError(ErrorCause.GENERAL_FETCH_FAIL, exception)
            }
    }

    fun deleteLocation(locationId: String) {
        db.collection("locations")
            .document(locationId)
            .delete()
            .addOnSuccessListener {
                uiMessage = Triple(
                    "Location deleted",
                    MsgType.SUCCESS,
                    null
                )
                fetchRejectedLocations()
            }
    }
    fun deleteUser(userId: String) {
        db.collection("users")
            .document(userId)
            .delete()
            .addOnSuccessListener {
                uiMessage = Triple(
                    "User deleted",
                    MsgType.SUCCESS,
                    null
                )
                fetchAllUsers()
            }
    }
}
