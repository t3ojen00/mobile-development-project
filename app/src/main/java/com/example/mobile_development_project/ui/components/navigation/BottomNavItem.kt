package com.example.mobile_development_project.ui.components.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.mobile_development_project.navigation.NavRoutes

// centralizing all navigation items definition for the BottomBar navigation
// "bottomItems" is list of all navigation bar items used by the BottomBar composable
data class BottomNavItem(
    val route: String,
    val label: String, // label optional
    val icon: ImageVector
)

// current items, will be updated
val bottomItems = listOf(
    BottomNavItem(
        route = NavRoutes.Map,
        label = "Map",
        icon = Icons.Default.Home
    ),
    BottomNavItem(
        route = NavRoutes.Favorites,
        label = "Favorites",
        icon = Icons.Default.Favorite
    ),
    BottomNavItem(
        route = NavRoutes.Search,
        label = "Search",
        icon = Icons.Default.Search
    )
)