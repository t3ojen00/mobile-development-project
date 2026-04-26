package com.example.mobile_development_project.ui.components.navigation

import com.example.mobile_development_project.R
import com.example.mobile_development_project.navigation.NavRoutes

// centralizing all navigation items definition for the BottomBar navigation
// "bottomItems" is list of all navigation bar items used by the BottomBar composable
data class BottomNavItem(
    val route: String,
    val label: String, // label optional
    val icon: Int
)

// define all bottom navigation items
val bottomItems = listOf(
    BottomNavItem(
        route = NavRoutes.Map,
        label = "Map",
        icon = R.drawable.map_icon
    ),
    BottomNavItem(
        route = NavRoutes.Favorites,
        label = "Favorites",
        icon = R.drawable.favorites_icon
    ),
    BottomNavItem(
        route = NavRoutes.AddLocation,
        label = "Add location",
        icon = R.drawable.add_icon
    ),
    BottomNavItem(
        route = NavRoutes.Search,
        label = "Users",
        icon = R.drawable.users_icon
    ),
    BottomNavItem(
        route = NavRoutes.UserProfile,
        label = "Profile",
        icon = R.drawable.user_icon
    ),
)