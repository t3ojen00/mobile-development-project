package com.example.mobile_development_project.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.mobile_development_project.navigation.NavRoutes
import com.example.mobile_development_project.ui.components.reusable.FavoriteLocationListItem
import com.example.mobile_development_project.ui.components.reusable.ScrollToTopButton
import com.example.mobile_development_project.viewModels.FavoritesViewModel
import kotlinx.coroutines.launch

@Composable
fun FavoritesScreen(
    navController: NavHostController,
    viewModel: FavoritesViewModel = viewModel()
) {
    LaunchedEffect(Unit) {
        viewModel.loadFavorites()
    }

    val favorites = viewModel.favoriteLocations
    val scope = rememberCoroutineScope()
    val listState = rememberLazyListState()

    if (viewModel.isLoading) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
        return
    }

    if (favorites.isEmpty()) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text("No favorites yet")
        }
        return
    }
    Box(modifier = Modifier.fillMaxSize())
    {
        LazyColumn(
            state = listState,
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(favorites) { location ->
                FavoriteLocationListItem(
                    location = location,
                    onClick = {
                        navController.navigate(
                            NavRoutes.LocationDetail.replace("{id}", location.id)
                        )
                    }
                )
            }
        }
        ScrollToTopButton(
            listState = listState,
            onClick = {
                scope.launch {
                    listState.animateScrollToItem(0)
                }
            },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(12.dp)
        )
    }
}