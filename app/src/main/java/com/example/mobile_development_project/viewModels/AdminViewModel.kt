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

class AdminViewModel : ViewModel() {
    private val db = FirebaseFirestore.getInstance()
    var uiMessage by mutableStateOf<Triple<String, MsgType, ErrorCause?>?>(null)
        private set
    var pendingLocations by mutableStateOf<List<Location>>(emptyList())
        private set
    var allUsers by mutableStateOf<List<User>>(emptyList())
        private set

    var userRole by mutableStateOf<String?>(null)
        private set

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
    //
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
            }
            .addOnFailureListener { exception ->
                setError(ErrorCause.GENERAL_UPDATE_FAIL, exception)
            }
    }

    fun fetchAllUsers()
    {
        db.collection("users")
            .get()
            .addOnSuccessListener { result ->
                val users = result.documents.map {
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
                allUsers = users
            }
            .addOnFailureListener { exception ->
                setError(ErrorCause.GENERAL_FETCH_FAIL, exception)
            }
    }
}
