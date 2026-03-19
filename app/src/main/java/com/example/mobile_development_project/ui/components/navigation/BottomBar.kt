package com.example.mobile_development_project.ui.components.navigation

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.ui.Modifier

@Composable
fun BottomBar(navController: NavHostController) {

    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStackEntry?.destination?.route

    NavigationBar ( modifier = Modifier.navigationBarsPadding() ) {
        // render all bottom navigation items dynamically from "bottomItems" list
        bottomItems.forEach { item ->

            NavigationBarItem(
                selected = currentRoute == item.route,
                onClick = {
                    navController.navigate(item.route) {
                        //popUpTo(NavRoutes.Map) // clean backstack
                        launchSingleTop = true } // prevents multiple copies on the stack of the same destination
                    },
                icon = { Icon(item.icon, contentDescription = item.label) },
                //label = { Text(item.label) } label visibility optional
            )
        }
    }
}
