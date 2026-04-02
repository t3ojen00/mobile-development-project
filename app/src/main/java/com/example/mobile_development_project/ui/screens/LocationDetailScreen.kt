package com.example.mobile_development_project.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.mobile_development_project.data.models.Location
import com.example.mobile_development_project.navigation.NavRoutes
import com.example.mobile_development_project.ui.components.reusable.LocationDetailCard
import com.example.mobile_development_project.viewModels.LocationDetailViewModel

@Composable
fun LocationDetailScreen(
    navController: NavHostController,
    locationId: String?,
    modifier: Modifier = Modifier,
    viewModel: LocationDetailViewModel = viewModel()
) {
    LaunchedEffect(locationId) {
        if (!locationId.isNullOrBlank()) {
            viewModel.loadLocation(locationId)
        }
    }

    val state = viewModel.uiState

    when {
        locationId.isNullOrBlank() -> {
            Box(
                modifier = modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text("Location ID missing")
            }
        }

        state.isLoading -> {
            Box(
                modifier = modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }

        state.errorMessage != null -> {
            Box(
                modifier = modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(state.errorMessage ?: "Unknown error")
            }
        }

        state.location != null -> {
            LocationDetailCard(
                location = state.location,
                isOwner = state.isOwner,
                isFavorite = state.isFavorite,
                onFavoriteClick = { viewModel.toggleFavorite() },
                onEditClick = {
                    navController.navigate(
                        NavRoutes.EditLocation.replace("{id}", state.location.id)
                    )
                },
                modifier = modifier
            )
        }

        else -> {
            Box(
                modifier = modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text("No location data")
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun LocationDetailCardPreview() {
    val mockLocation = Location(
        id = "1",
        ownerId = "user123",
        ownerUsername = "TestUser",
        name = "Kuivasjärvi",
        description = "Oulun luonto maisemaa",
        tags = listOf("nature", "lake", "oulu"),
        latitude = 65.0654,
        longitude = 25.4881,
        previewImageUrl = "",
        status = "approved",
        createdAt = System.currentTimeMillis().toString(),
        favoritesCount = 12
    )

    LocationDetailCard(
        location = mockLocation,
        isOwner = true,
        isFavorite = false,
        onFavoriteClick = {},
        onEditClick = {}
    )
}