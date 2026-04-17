package com.example.mobile_development_project.viewModels

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.mobile_development_project.data.models.FollowedUser
import com.example.mobile_development_project.data.models.Location
import com.example.mobile_development_project.data.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore

class ProfileViewModel : ViewModel() {

    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()

    var user by mutableStateOf<User?>(null)
        private set

    var isLoading by mutableStateOf(false)
        private set

    var userLocations by mutableStateOf<List<Location>>(emptyList())
        private set

    var followedUsers by mutableStateOf<List<FollowedUser>>(emptyList())
        private set

    var isFollowingUser by mutableStateOf(false)
        private set

    var isFollowLoading by mutableStateOf(false)
        private set

    fun loadProfileData(targetUserId: String? = null) {
        val resolvedUserId = when {
            targetUserId.isNullOrBlank() || targetUserId == "current-user" -> auth.currentUser?.uid
            else -> targetUserId
        }

        if (resolvedUserId.isNullOrBlank()) {
            user = null
            userLocations = emptyList()
            followedUsers = emptyList()
            isFollowingUser = false
            isLoading = false
            return
        }

        isLoading = true

        loadUser(resolvedUserId)
        loadUserLocations(resolvedUserId)

        if (isCurrentUserProfile(targetUserId)) {
            loadFollowedUsers()
            isFollowingUser = false
        } else {
            followedUsers = emptyList()
            checkIfFollowing(resolvedUserId)
        }
    }

    private fun loadUser(uid: String) {
        db.collection("users")
            .document(uid)
            .get()
            .addOnSuccessListener { doc ->
                user = if (doc.exists()) {
                    User(
                        id = uid,
                        email = doc.getString("email") ?: "",
                        username = doc.getString("username") ?: "",
                        displayName = doc.getString("displayName") ?: "",
                        role = doc.getString("role") ?: "user",
                        createdAt = doc.getString("createdAt"),
                        updatedAt = doc.getString("updatedAt"),
                        isActive = doc.getBoolean("isActive") ?: true
                    )
                } else {
                    null
                }

                isLoading = false
            }
            .addOnFailureListener { error ->
                Log.e("UserProfileVM", "loadUser failed", error)
                user = null
                isLoading = false
            }
    }

    private fun loadUserLocations(uid: String) {
        Log.d("UserProfileVM", "Loading locations for UID: $uid")

        db.collection("locations")
            .whereEqualTo("ownerId", uid)
            .get()
            .addOnSuccessListener { result ->
                Log.d("UserProfileVM", "Found locations: ${result.documents.size}")

                userLocations = result.documents.map { doc ->
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
            .addOnFailureListener { error ->
                Log.e("UserProfileVM", "loadUserLocations failed", error)
                userLocations = emptyList()
            }
    }

    private fun loadFollowedUsers() {
        val uid = auth.currentUser?.uid ?: return

        db.collection("follows")
            .whereEqualTo("followerId", uid)
            .get()
            .addOnSuccessListener { result ->
                followedUsers = result.documents.map { doc ->
                    FollowedUser(
                        id = doc.getString("followingId") ?: "",
                        username = doc.getString("followingUsername") ?: "",
                        displayName = doc.getString("followingDisplayName") ?: "",
                        email = doc.getString("followingEmail") ?: ""
                    )
                }.sortedBy {
                    if (it.displayName.isNotBlank()) {
                        it.displayName.lowercase()
                    } else {
                        it.username.lowercase()
                    }
                }
            }
            .addOnFailureListener { error ->
                Log.e("UserProfileVM", "loadFollowedUsers failed", error)
                followedUsers = emptyList()
            }
    }

    private fun checkIfFollowing(targetUserId: String) {
        val currentUserId = auth.currentUser?.uid ?: return

        db.collection("follows")
            .whereEqualTo("followerId", currentUserId)
            .whereEqualTo("followingId", targetUserId)
            .limit(1)
            .get()
            .addOnSuccessListener { result ->
                isFollowingUser = !result.isEmpty
            }
            .addOnFailureListener { error ->
                Log.e("UserProfileVM", "checkIfFollowing failed", error)
                isFollowingUser = false
            }
    }

    fun toggleFollowUser(targetUserId: String) {
        val currentUserId = auth.currentUser?.uid ?: return
        val targetUser = user ?: return

        if (currentUserId == targetUserId) return
        if (isFollowLoading) return

        isFollowLoading = true

        db.collection("follows")
            .whereEqualTo("followerId", currentUserId)
            .whereEqualTo("followingId", targetUserId)
            .limit(1)
            .get()
            .addOnSuccessListener { existingDocs ->
                if (!existingDocs.isEmpty) {
                    val docId = existingDocs.documents.first().id

                    db.collection("follows")
                        .document(docId)
                        .delete()
                        .addOnSuccessListener {
                            isFollowingUser = false
                            isFollowLoading = false
                        }
                        .addOnFailureListener { error ->
                            Log.e("UserProfileVM", "unfollow failed", error)
                            isFollowLoading = false
                        }
                } else {
                    val followData = hashMapOf(
                        "followerId" to currentUserId,
                        "followingId" to targetUserId,
                        "followingUsername" to targetUser.username,
                        "followingDisplayName" to targetUser.displayName,
                        "followingEmail" to targetUser.email,
                        "createdAt" to FieldValue.serverTimestamp()
                    )

                    db.collection("follows")
                        .add(followData)
                        .addOnSuccessListener {
                            isFollowingUser = true
                            isFollowLoading = false
                        }
                        .addOnFailureListener { error ->
                            Log.e("UserProfileVM", "follow failed", error)
                            isFollowLoading = false
                        }
                }
            }
            .addOnFailureListener { error ->
                Log.e("UserProfileVM", "toggleFollowUser failed", error)
                isFollowLoading = false
            }
    }

    fun isCurrentUserProfile(targetUserId: String?): Boolean {
        val currentUserId = auth.currentUser?.uid
        val resolvedTargetId = when {
            targetUserId.isNullOrBlank() || targetUserId == "current-user" -> currentUserId
            else -> targetUserId
        }

        return currentUserId != null && currentUserId == resolvedTargetId
    }
}