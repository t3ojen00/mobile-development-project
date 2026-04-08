package com.example.mobile_development_project.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.mobile_development_project.ui.components.reusable.PrimaryButton
import com.example.mobile_development_project.ui.components.reusable.SecondaryButton
import com.example.mobile_development_project.ui.theme.cardColor
import com.example.mobile_development_project.viewModels.AdminViewModel

@Composable
fun AdminScreen(
    viewModel: AdminViewModel = viewModel(),
    navController: NavHostController
) {
    val locations = viewModel.pendingLocations

    DisposableEffect(Unit) {
        viewModel.fetchPendingLocations()
        onDispose { }
    }

    LazyColumn {
        items(locations) { location ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                elevation = CardDefaults.cardElevation(4.dp),
                colors = CardDefaults.cardColors(containerColor = cardColor)
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    // first row with title and status
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

                    Text(
                        text = "By ${location.ownerUsername}",
                        style = MaterialTheme.typography.bodySmall
                    )
                    Text(
                        text = "Created at ${location.createdAt}",
                        style = MaterialTheme.typography.bodySmall
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = location.description,
                        maxLines = 2
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    // coordinates and navigation-btn to map view (not impelemnted yet)
                    Row(modifier = Modifier
                        .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "${location.latitude}, ${location.longitude}",
                            style = MaterialTheme.typography.bodySmall,
                        )
                        Spacer(modifier = Modifier.weight(1f))
                        SecondaryButton(
                            onClick = { },
                            label = "View on map",
                            contentPadding = PaddingValues( horizontal = 12.dp, vertical = 2.dp)
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
                }
            }
        }

    }
}
