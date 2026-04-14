package com.example.mobile_development_project.ui.components.admin_view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.mobile_development_project.viewModels.AdminViewModel
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.ui.draw.clip
import com.example.mobile_development_project.R
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.navigation.NavHostController
import com.example.mobile_development_project.navigation.NavRoutes
import com.example.mobile_development_project.ui.components.reusable.PrimaryButton
import com.example.mobile_development_project.ui.theme.adminCards
import com.example.mobile_development_project.ui.theme.rejectButton
import java.util.Locale

@Composable
fun PendingLocations(
    viewModel: AdminViewModel,
    navController: NavHostController,
    role: String?
) {

    val locations = viewModel.pendingLocations
    val uiMessage = viewModel.uiMessage
    val locationAmount = locations.size

    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        item {
            if (role == "moderator") {
                Spacer(modifier = Modifier.height(14.dp))
            }
            Text(
                "Locations waiting for approval ($locationAmount)",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 4.dp)
                    .fillMaxWidth(),
                textAlign = TextAlign.Center
            )
        }
        item {
            Spacer(modifier = Modifier.height(6.dp))
        }

        items(locations) { location ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                shape = RoundedCornerShape(8.dp),
                colors = CardDefaults.cardColors(
                    containerColor = adminCards
                ),
                elevation = CardDefaults.cardElevation(4.dp),
            ) {
                Column(
                    modifier = Modifier
                        .padding(16.dp),
                ) {

                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = location.name,
                            style = MaterialTheme.typography.titleMedium
                        )
                        Text(
                            text = location.status,
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier
                                    .background(
                                    MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                            shape = RoundedCornerShape(8.dp)
                        )
                            .padding(horizontal = 8.dp, vertical = 2.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = location.description,
                        maxLines = 2,
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        location.tags.forEach { tag ->
                            Text(
                                text = "$tag ",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onPrimary,
                                modifier = Modifier
                                    .background(
                                        MaterialTheme.colorScheme.secondary.copy(alpha = 0.8f),
                                        shape = RoundedCornerShape(8.dp)
                                    )
                                    .padding(horizontal = 8.dp, vertical = 2.dp)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = buildAnnotatedString {
                            append("By ")

                            withStyle(SpanStyle(
                                color = MaterialTheme.colorScheme.primary,
                                fontWeight = FontWeight.Bold )
                            ) {
                                append(location.ownerUsername)
                            }

                            append(" at ")

                            withStyle(SpanStyle(
                                color = MaterialTheme.colorScheme.primary,
                                fontWeight = FontWeight.Bold )
                            ) {
                                append(location.createdAt)
                            }
                        },
                        style = MaterialTheme.typography.bodySmall
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    // coordinates and navigation-btn to map view
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = String.format(
                                Locale.US,
                                "lat: %.3f long: %.3f",
                                location.latitude, location.longitude
                            ),
                            style = MaterialTheme.typography.bodySmall,
                        )
                        Spacer(modifier = Modifier.weight(1f))

                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(modifier = Modifier
                        .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,

                    ) {
                        IconButton(
                            onClick = {
                                // navigate to map view with given coordinates using savedStateHandle
                                navController.currentBackStackEntry
                                    ?.savedStateHandle
                                    ?.set("lat", location.latitude)

                                navController.currentBackStackEntry
                                    ?.savedStateHandle
                                    ?.set("lng", location.longitude)

                                navController.navigate(NavRoutes.Map)
                            },
                            modifier = Modifier
                                .size(44.dp)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.secondary)
                                .padding(2.dp),
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.location_marker),
                                contentDescription = "View on map",
                                tint = Color.White,
                                modifier = Modifier
                                    .size(28.dp)
                            )
                        }
                        Spacer(modifier = Modifier.weight(0.5f))

                        PrimaryButton(
                            onClick = {
                                viewModel.rejectLocation(location.id)
                            },
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = rejectButton,
                                contentColor = Color.White
                            ),
                            label = "Reject"
                        )

                        Spacer(modifier = Modifier.weight(0.1f))

                        PrimaryButton(
                            onClick = {
                                viewModel.approveLocation(location.id)
                            },
                            modifier = Modifier.weight(1f),
                            label = "Approve"
                        )
                    }
                }
            }
        }
    }
}
