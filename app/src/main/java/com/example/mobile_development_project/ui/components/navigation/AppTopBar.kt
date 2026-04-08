package com.example.mobile_development_project.ui.components.navigation

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.mobile_development_project.navigation.NavRoutes
import com.example.mobile_development_project.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(navController: NavHostController) {

    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route
    // back-navigation on other routes except map
    val showBackArrow = currentRoute != NavRoutes.Map
    // location marker icon only on map screen
    val showMarkerIcon = currentRoute == NavRoutes.Map

    // define title based on route
    val currentTitle = when {
        currentRoute == NavRoutes.Map -> "Photo location scout"
        currentRoute == NavRoutes.Login -> "Login"
        currentRoute == NavRoutes.Register -> "Register"
        currentRoute == NavRoutes.AddLocation -> "Add location"
        currentRoute == NavRoutes.Favorites -> "Favorites"
        currentRoute == NavRoutes.Admin -> "Admin"
        currentRoute == NavRoutes.Search -> "Search location"

        currentRoute?.startsWith("location/") == true -> "Location details"
        currentRoute?.startsWith("edit-location/") == true -> "Edit location"
        currentRoute?.startsWith("user/") == true -> "Profile"

        else -> "Photo location scout"
    }
    // set the title on topbar dynamically based on the current screen
    CenterAlignedTopAppBar(
        modifier = Modifier.fillMaxWidth(),
        navigationIcon = {
            if (showBackArrow) {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Navigate back"
                    )
                }
            }
        },
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(vertical = 10.dp)
                    .padding(end = if (showMarkerIcon) 14.dp else 0.dp) // centralize icon+title on map screen
            ) {
                if (showMarkerIcon) {
                    Icon(
                        painter = painterResource(id = R.drawable.location_marker),
                        contentDescription = "Location marker",
                        modifier = Modifier.size(50.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
                Text(text = currentTitle)
            }
        }
    )
}