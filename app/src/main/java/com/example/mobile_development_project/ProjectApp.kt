package com.example.mobile_development_project

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.mobile_development_project.navigation.NavRoutes
import com.example.mobile_development_project.navigation.Navigation
import com.example.mobile_development_project.ui.components.navigation.BottomBar
import com.example.mobile_development_project.ui.components.navigation.TopBar

@Composable
fun ProjectApp() {
    val navController = rememberNavController()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route

    val authRoutes = setOf(
        NavRoutes.Login,
        NavRoutes.Register
    )

    val role = backStackEntry?.arguments?.getString("role") ?: ""

    val showTopBar = currentRoute !in authRoutes
    val showBottomBar = currentRoute !in authRoutes

    Scaffold(
        topBar = {
            if (showTopBar) {
                TopBar(navController = navController, role = role)
            }
        },
        bottomBar = {
            if (showBottomBar) {
                BottomBar(navController = navController)
            }
        }
    ) { innerPadding ->
        Navigation(
            navController = navController,
            modifier = Modifier.padding(innerPadding)
        )
    }
}
