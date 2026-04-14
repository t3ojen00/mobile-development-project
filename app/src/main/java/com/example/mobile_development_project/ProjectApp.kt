package com.example.mobile_development_project

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.mobile_development_project.navigation.NavRoutes
import com.example.mobile_development_project.navigation.Navigation
import com.example.mobile_development_project.ui.components.navigation.BottomBar
import com.example.mobile_development_project.ui.components.navigation.TopBar
import com.google.firebase.auth.FirebaseAuth

@Composable
fun ProjectApp() {
    val navController = rememberNavController()
    val auth = remember { FirebaseAuth.getInstance() }

    var isLoggedIn by remember { mutableStateOf(auth.currentUser != null) }

    DisposableEffect(auth) {
        val listener = FirebaseAuth.AuthStateListener { firebaseAuth ->
            isLoggedIn = firebaseAuth.currentUser != null
        }

        auth.addAuthStateListener(listener)

        onDispose {
            auth.removeAuthStateListener(listener)
        }
    }

    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route

    val authRoutes = setOf(
        NavRoutes.Login,
        NavRoutes.Register
    )

    val showTopBar = currentRoute !in authRoutes
    val showBottomBar = currentRoute !in authRoutes

    LaunchedEffect(isLoggedIn, currentRoute) {
        if (currentRoute == null) return@LaunchedEffect

        if (isLoggedIn && currentRoute in authRoutes) {
            navController.navigate(NavRoutes.Map) {
                popUpTo(navController.graph.findStartDestination().id) {
                    inclusive = true
                }
                launchSingleTop = true
            }
        } else if (!isLoggedIn && currentRoute !in authRoutes) {
            navController.navigate(NavRoutes.Login) {
                popUpTo(navController.graph.findStartDestination().id) {
                    inclusive = true
                }
                launchSingleTop = true
            }
        }
    }

    Scaffold(
        topBar = {
            if (showTopBar) {
                TopBar(navController = navController)
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