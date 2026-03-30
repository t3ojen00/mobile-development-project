package com.example.mobile_development_project.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.mobile_development_project.feature_map.MapScreen
import com.example.mobile_development_project.ui.screens.AddLocationScreen
import com.example.mobile_development_project.ui.screens.AdminScreen
import com.example.mobile_development_project.ui.screens.EditLocationScreen
import com.example.mobile_development_project.ui.screens.FavoritesScreen
import com.example.mobile_development_project.ui.screens.LocationDetailScreen
import com.example.mobile_development_project.ui.screens.LoginScreen
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
            MapScreen()
        }

        composable(NavRoutes.LocationDetail) { backStackEntry ->
            val locationId = backStackEntry.arguments?.getString("id")
            LocationDetailScreen(locationId)
        }

        composable(NavRoutes.AddLocation) {
            AddLocationScreen()
        }

        composable(NavRoutes.EditLocation) { backStackEntry ->
            val locationId = backStackEntry.arguments?.getString("id")
            EditLocationScreen(locationId)
        }

        composable(NavRoutes.UserProfile) {
            UserProfileScreen("current-user")
        }

        composable(NavRoutes.UserProfileWithId) { backStackEntry ->
            val userId = backStackEntry.arguments?.getString("id")
            UserProfileScreen(userId)
        }

        composable(NavRoutes.Favorites) {
            FavoritesScreen()
        }

        composable(NavRoutes.Admin) {
            AdminScreen()
        }

        composable(NavRoutes.Search) {
            SearchLocationScreen()
        }
    }
}