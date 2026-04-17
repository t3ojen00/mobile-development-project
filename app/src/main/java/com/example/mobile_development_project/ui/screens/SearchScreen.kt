package com.example.mobile_development_project.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.mobile_development_project.data.models.User
import com.example.mobile_development_project.navigation.NavRoutes
import com.example.mobile_development_project.ui.theme.AuthCardGray
import com.example.mobile_development_project.ui.theme.Burgundy
import com.example.mobile_development_project.ui.theme.OrangeAccent
import com.example.mobile_development_project.ui.theme.ScreenBackground
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun SearchLocationScreen(
    navController: NavHostController
) {
    val db = remember { FirebaseFirestore.getInstance() }
    val auth = remember { FirebaseAuth.getInstance() }

    var searchQuery by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    val allUsers = remember { mutableStateListOf<User>() }

    LaunchedEffect(Unit) {
        db.collection("users")
            .get()
            .addOnSuccessListener { result ->
                val currentUserId = auth.currentUser?.uid.orEmpty()

                val users = result.documents.mapNotNull { doc ->
                    val id = doc.id

                    if (id == currentUserId) {
                        null
                    } else {
                        User(
                            id = id,
                            email = doc.getString("email") ?: "",
                            username = doc.getString("username") ?: "",
                            displayName = doc.getString("displayName") ?: "",
                            role = doc.getString("role") ?: "user",
                            createdAt = doc.getString("createdAt"),
                            updatedAt = doc.getString("updatedAt"),
                            isActive = doc.getBoolean("isActive") ?: true
                        )
                    }
                }.sortedBy { user ->
                    when {
                        user.displayName.isNotBlank() -> user.displayName.lowercase()
                        user.username.isNotBlank() -> user.username.lowercase()
                        else -> user.email.lowercase()
                    }
                }

                allUsers.clear()
                allUsers.addAll(users)
                isLoading = false
            }
            .addOnFailureListener { error ->
                errorMessage = error.message ?: "Failed to load users"
                isLoading = false
            }
    }

    val filteredUsers = allUsers.filter { user ->
        val query = searchQuery.trim().lowercase()

        if (query.isBlank()) {
            true
        } else {
            user.username.lowercase().contains(query)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ScreenBackground)
    ) {


        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            placeholder = {
                Text("Search by username")
            },
            singleLine = true,
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                focusedBorderColor = OrangeAccent,
                unfocusedBorderColor = Color.LightGray,
                cursorColor = Burgundy
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        when {
            isLoading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = Burgundy)
                }
            }

            errorMessage != null -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = errorMessage ?: "Unknown error",
                        color = Color.DarkGray
                    )
                }
            }

            filteredUsers.isEmpty() -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    contentAlignment = Alignment.TopStart
                ) {
                    Text(
                        text = if (searchQuery.isBlank()) {
                            "No users found"
                        } else {
                            "No matching users"
                        },
                        color = Color.DarkGray
                    )
                }
            }

            else -> {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(filteredUsers) { user ->
                        UserSearchItem(
                            user = user,
                            onClick = {
                                navController.navigate(
                                    NavRoutes.UserProfileWithId.replace("{id}", user.id)
                                )
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun UserSearchItem(
    user: User,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = AuthCardGray)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = "User",
                tint = Color.White,
                modifier = Modifier.size(36.dp)
            )

            Spacer(modifier = Modifier.size(12.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = when {
                        user.displayName.isNotBlank() -> user.displayName
                        user.username.isNotBlank() -> user.username
                        else -> user.email
                    },
                    color = Color.White,
                    style = MaterialTheme.typography.titleMedium
                )

                if (user.username.isNotBlank()) {
                    Text(
                        text = "@${user.username}",
                        color = Color.LightGray,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }

                if (user.email.isNotBlank()) {
                    Text(
                        text = user.email,
                        color = Color.LightGray,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        }
    }
}