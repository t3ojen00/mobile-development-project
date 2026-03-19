package com.example.mobile_development_project.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.mobile_development_project.ui.screens.LoginScreen
import com.example.mobile_development_project.ui.screens.RegisterScreen
import com.example.mobile_development_project.ui.screens.MapScreen
import com.example.mobile_development_project.ui.screens.AddLocationScreen
import com.example.mobile_development_project.ui.screens.EditLocationScreen
import com.example.mobile_development_project.ui.screens.LocationDetailScreen
import com.example.mobile_development_project.ui.screens.UserProfileScreen
import com.example.mobile_development_project.ui.screens.FavoritesScreen
import com.example.mobile_development_project.ui.screens.AdminScreen
import com.example.mobile_development_project.ui.screens.SearchLocationScreen


@Composable
fun Navigation(navController: NavHostController) {
    // define navhost container and start destination
    NavHost(
        navController,
        startDestination = NavRoutes.Map
    )
    {
        // all routes go here

        // Authentication
        composable(NavRoutes.Login) { LoginScreen() }
        composable(NavRoutes.Register) { RegisterScreen() }

        // Map
        composable(NavRoutes.Map) { MapScreen() }

        // Location details
        // id-parameter passed in the route (e.g. "location/123") is retrieved with backStackEntry
        // backStackEntry represents the current screen in the navigation back stack
        // and holds all necessary info related to the screen (arguments, route, saved state)
        composable(NavRoutes.LocationDetail) { backStackEntry ->
            val locationId = backStackEntry.arguments?.getString("id")
            LocationDetailScreen(locationId)
        }

        // Location management
        composable(NavRoutes.AddLocation) { AddLocationScreen() }
        composable(NavRoutes.EditLocation) { backStackEntry ->
            val locationId = backStackEntry.arguments?.getString("id")
            EditLocationScreen(locationId)
        }

        // User profile
        composable(NavRoutes.UserProfile) { backStackEntry ->
            val userId = backStackEntry.arguments?.getString("id")
            UserProfileScreen(userId)
        }

        // Favorites
        composable(NavRoutes.Favorites) { FavoritesScreen() }

        // Admin / moderation
        composable(NavRoutes.Admin) { AdminScreen() }

        // Search / filter
        composable(NavRoutes.Search) { SearchLocationScreen() }
    }
}

