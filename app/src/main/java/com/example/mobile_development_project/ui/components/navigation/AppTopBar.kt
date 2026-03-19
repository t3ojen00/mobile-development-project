package com.example.mobile_development_project.ui.components.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.*
import androidx.navigation.NavHostController
import com.example.mobile_development_project.navigation.NavRoutes

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(navController: NavHostController) {
    var expanded by remember { mutableStateOf(false) }

    Box {
        TopAppBar(
            title = { Text("Location scout") },
            navigationIcon = {
                IconButton(onClick = { expanded = true }) {
                    Icon(Icons.Default.Menu, contentDescription = "Menu")
                }
            }
        )

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            DropdownMenuItem(
                text = { Text("Map") },
                onClick = {
                    navController.navigate(NavRoutes.Map)
                    expanded = false
                }
            )
            DropdownMenuItem(
                text = { Text("Login") },
                onClick = {
                    navController.navigate(NavRoutes.Login)
                    expanded = false
                }
            )
            DropdownMenuItem(
                text = { Text("Register") },
                onClick = {
                    navController.navigate(NavRoutes.Register)
                    expanded = false
                }
            )
            DropdownMenuItem(
                text = { Text("Favorites") },
                onClick = {
                    navController.navigate(NavRoutes.Favorites)
                    expanded = false
                }
            )
            DropdownMenuItem(
                text = { Text("Admin") },
                onClick = {
                    navController.navigate(NavRoutes.Admin)
                    expanded = false
                }
            )
            DropdownMenuItem(
                text = { Text("Search") },
                onClick = {
                    navController.navigate(NavRoutes.Search)
                    expanded = false
                }
            )
        }
    }
}