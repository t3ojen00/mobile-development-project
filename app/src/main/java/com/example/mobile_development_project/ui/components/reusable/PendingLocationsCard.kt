package com.example.mobile_development_project.ui.components.reusable

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mobile_development_project.ui.theme.cardColor
import com.example.mobile_development_project.viewModels.AdminViewModel
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ButtonDefaults
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavHostController
import com.example.mobile_development_project.navigation.NavRoutes
import java.util.Locale


@Composable
fun PendingLocations(
    viewModel: AdminViewModel = viewModel(),
    navController: NavHostController
) {

    val locations = viewModel.pendingLocations
    val uiMessage = viewModel.uiMessage

    LazyColumn {
        items(locations) { location ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                elevation = CardDefaults.cardElevation(4.dp),
                colors = CardDefaults.cardColors(containerColor = cardColor)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {

                    Row(modifier = Modifier
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
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = location.description,
                        maxLines = 2,
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Row(modifier = Modifier
                        .fillMaxWidth(),
                        horizontalArrangement = Arrangement.Absolute.Left,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "By ${location.ownerUsername} ",
                            style = MaterialTheme.typography.bodySmall
                        )

                        Text(
                            text = "at ${location.createdAt}",
                            style = MaterialTheme.typography.bodySmall
                        )
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    // coordinates and navigation-btn to map view
                    Row(modifier = Modifier
                        .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = String.format(
                                Locale.US,
                                "lat: %.3f long: %.3f",
                                location.latitude, location.longitude),
                            style = MaterialTheme.typography.bodySmall,
                        )
                        Spacer(modifier = Modifier.weight(1f))
                        SecondaryButton(
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
                            label = "View on map",
                            contentPadding = PaddingValues( horizontal = 12.dp, vertical = 2.dp),
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    PrimaryButton(
                        onClick = {
                            viewModel.approveLocation(location.id)
                            viewModel.fetchPendingLocations() }, // refresh list
                        modifier = Modifier.fillMaxWidth(),
                        label = "Approve"
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    PrimaryButton(
                        onClick = {
                            viewModel.rejectLocation(location.id)
                            viewModel.fetchPendingLocations()
                                  },
                        modifier = Modifier
                            .fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF030303),
                            contentColor = Color.White
                        ),
                        label = "Reject"
                    )
                }
            }
        }
    }
}
