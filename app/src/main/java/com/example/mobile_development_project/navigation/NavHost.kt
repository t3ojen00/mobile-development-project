package com.example.mobile_development_project.navigation

import SettingsScreen
import UserImagesScreen
import UserLocationsScreen
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.mobile_development_project.ui.screens.AddLocationScreen
import com.example.mobile_development_project.ui.screens.AdminScreen
import com.example.mobile_development_project.ui.screens.EditLocationScreen
import com.example.mobile_development_project.ui.screens.FavoritesScreen
import com.example.mobile_development_project.ui.screens.LocationDetailScreen
import com.example.mobile_development_project.ui.screens.LoginScreen
import com.example.mobile_development_project.ui.screens.MapScreen
import com.example.mobile_development_project.ui.screens.MapSelectionScreen
import com.example.mobile_development_project.ui.screens.RegisterScreen
import com.example.mobile_development_project.ui.screens.SearchLocationScreen
import com.example.mobile_development_project.ui.screens.UserProfileScreen

@Composable
fun Navigation(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = NavRoutes.Login,
        modifier = modifier.fillMaxSize()
    ) {
        composable(NavRoutes.Login) {
            LoginScreen(
                onBackClick = {
                    navController.popBackStack()
                },
                onNavigateToRegister = {
                    navController.navigate(NavRoutes.Register)
                },
                onLoginSuccess = {
                    navController.navigate(NavRoutes.Map) {
                        popUpTo(NavRoutes.Login) { inclusive = true }
                        launchSingleTop = true
                    }
                }
            )
        }

        composable(NavRoutes.Register) {
            RegisterScreen(
                onBackClick = {
                    navController.popBackStack()
                },
                onRegisterSuccess = {
                    navController.popBackStack()
                }
            )
        }

        composable(NavRoutes.Map) {
            MapScreen(navController = navController)
        }

        composable(NavRoutes.LocationDetail) { backStackEntry ->
            val locationId = backStackEntry.arguments?.getString("id")

            LocationDetailScreen(
                navController = navController,
                locationId = locationId
            )
        }

        composable(NavRoutes.AddLocation) {
            AddLocationScreen(navController = navController)
        }

        composable(NavRoutes.SelectFromMap) {
            MapSelectionScreen(navController = navController)
        }

        composable(NavRoutes.EditLocation) { backStackEntry ->
            val locationId = backStackEntry.arguments?.getString("id")
            EditLocationScreen(
                navController = navController,
                locationId = locationId
            )
        }

        composable(NavRoutes.UserProfile) {
            UserProfileScreen(
                userId = "current-user",
                navController = navController
            )
        }

        composable(NavRoutes.UserProfileWithId) { backStackEntry ->
            val userId = backStackEntry.arguments?.getString("id")
            UserProfileScreen(
                userId = userId,
                navController = navController
            )
        }

        composable(NavRoutes.Favorites) {
            FavoritesScreen(navController = navController)
        }

        composable(NavRoutes.UserImages) {
            UserImagesScreen()
        }

        composable(NavRoutes.Settings) {
            SettingsScreen()
        }

        composable(NavRoutes.UserLocations) {
            UserLocationsScreen()
        }

        composable(NavRoutes.Admin) { backStackEntry ->
            val role = backStackEntry.arguments?.getString("role")
            AdminScreen(navController = navController, role = role)
        }

        composable(NavRoutes.Search) {
            SearchLocationScreen(navController = navController)
        }
    }
}