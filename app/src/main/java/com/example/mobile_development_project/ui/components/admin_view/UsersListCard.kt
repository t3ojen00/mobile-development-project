package com.example.mobile_development_project.ui.components.admin_view

import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.mobile_development_project.data.models.User
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import com.example.mobile_development_project.ui.components.reusable.PrimaryButton
import com.example.mobile_development_project.ui.theme.adminCards
import com.example.mobile_development_project.ui.theme.rejectButton
import com.example.mobile_development_project.viewModels.AdminViewModel

@Composable
fun UsersList(
    users: List<User>,
    modifier: Modifier = Modifier,
    viewModel: AdminViewModel,
    onDelete: (User) -> Unit,
    onPromote: (User, String) -> Unit,
    listState: LazyListState
) {

    Text(
        "Current users (${users.size})",
        style = MaterialTheme.typography.titleMedium,
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 4.dp)
            .fillMaxWidth(),
        textAlign = TextAlign.Center
    )

    LazyColumn(
        state = listState,
        modifier = modifier.fillMaxSize())
    {
        items(users) { user ->

            val isCurrentUser = viewModel.isCurrentUser(user.id)

            UserCard(
                user = user,
                isCurrentUser = isCurrentUser,
                onDelete = { onDelete(user) },
                onPromote = { role -> onPromote(user, role) }
            )
        }
    }
}

@Composable
fun UserCard(
        user: User,
        onDelete: () -> Unit,
        onPromote: (String) -> Unit,
        isCurrentUser: Boolean = false,
) {
    var menuExpanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = adminCards
        ),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {

                val usernameOrEmail =
                    when {
                        user.username.isNotBlank() -> user.username
                        user.email.isNotBlank() -> user.email
                        else -> "Unknown"
                    }

                Text(
                    text = usernameOrEmail,
                    style = MaterialTheme.typography.titleMedium
                )

                if (user.role.isNotBlank()) {
                    Text(
                        text = user.role,
                        color = MaterialTheme.colorScheme.primary,
                        style = MaterialTheme.typography.labelMedium,
                        modifier = Modifier
                            .background(
                                MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                                shape = RoundedCornerShape(8.dp)
                            )
                            .padding(horizontal = 8.dp, vertical = 2.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.height(6.dp))

            val hasUsername = user.username.isNotBlank()
            val hasEmail = user.email.isNotBlank()
            if (hasUsername && hasEmail) {
                Text(
                    text = user.email,
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            if (user.createdAt?.isNotBlank() ?: false) {
                Text(
                    "Created at: ${user.createdAt}",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            if (user.updatedAt?.isNotBlank() ?: false) {
                Text(
                    "Updated at: ${user.updatedAt}",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            Text(
                "Is active: ${user.isActive}",
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(modifier = Modifier.height(12.dp))


            if (!isCurrentUser) {
             Row(
                 verticalAlignment = Alignment.CenterVertically,

             ) {
                 Spacer(modifier = Modifier.weight(1f))

                 IconButton(
                     onClick = onDelete,
                     modifier = Modifier
                         .size(44.dp)
                         .clip(CircleShape)
                         .background(rejectButton)
                         .padding(2.dp)
                 ) {
                     Icon(
                         imageVector = Icons.Default.Delete,
                         contentDescription = "Delete user",
                         tint = Color.White,
                         modifier = Modifier.size(24.dp)
                     )
                 }

                 Spacer(modifier = Modifier.weight(0.1f))

                    Box() {
                        PrimaryButton(
                            onClick = { menuExpanded = true },
                            modifier = Modifier.width(130.dp),
                            label = "Promote"
                        )

                        DropdownMenu(
                            expanded = menuExpanded,
                            onDismissRequest = { menuExpanded = false }
                        ) {

                            DropdownMenuItem(
                                text = { Text("Moderator") },
                                onClick = {
                                    menuExpanded = false
                                    onPromote("moderator")
                                }
                            )

                            DropdownMenuItem(
                                text = { Text("Admin") },
                                onClick = {
                                    menuExpanded = false
                                    onPromote("admin")
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

