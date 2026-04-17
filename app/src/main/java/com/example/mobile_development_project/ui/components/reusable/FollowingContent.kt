package com.example.mobile_development_project.ui.components.reusable

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.background
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.mobile_development_project.data.models.FollowedUser
import com.example.mobile_development_project.navigation.NavRoutes
import com.example.mobile_development_project.ui.theme.AuthCardGray

@Composable
fun FollowingContent(
    followedUsers: List<FollowedUser>,
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {

        if (followedUsers.isEmpty()) {
            Text(
                text = "You are not following anyone yet",
                color = Color.DarkGray,
                style = MaterialTheme.typography.bodyMedium
            )
            return
        }

        followedUsers.forEach { user ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp)
                    .clickable {
                        navController.navigate(
                            NavRoutes.UserProfileWithId.replace("{id}", user.id)
                        )
                    },
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = AuthCardGray)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "User",
                        tint = Color.White,
                        modifier = Modifier.size(32.dp)
                    )

                    Spacer(modifier = Modifier.size(10.dp))

                    Column {
                        Text(
                            text = if (user.displayName.isNotBlank())
                                user.displayName
                            else user.username,
                            color = Color.White,
                            style = MaterialTheme.typography.titleMedium
                        )

                        if (user.username.isNotBlank()) {
                            Text(
                                text = "@${user.username}",
                                color = Color.LightGray,
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    }
                }
            }
        }
    }
}