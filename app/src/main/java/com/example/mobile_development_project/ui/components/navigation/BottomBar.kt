package com.example.mobile_development_project.ui.components.navigation

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.mobile_development_project.ui.theme.navIcon
import com.example.mobile_development_project.ui.theme.navbar

@Composable
fun BottomBar(navController: NavHostController) {

    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStackEntry?.destination?.route

    NavigationBar (
        modifier = Modifier
            .navigationBarsPadding(),
            containerColor = navbar
    ) {
        // render all bottom navigation items dynamically from "bottomItems" list
        bottomItems.forEach { item ->

            NavigationBarItem(
                selected = currentRoute == item.route,
                onClick = {
                    navController.navigate(item.route) {
                        //popUpTo(NavRoutes.Map) // clean backstack
                        launchSingleTop = true } // prevents multiple copies on the stack of the same destination
                    },
                icon = {
                    Icon(
                        painter = painterResource(id = item.icon),
                        contentDescription = item.label,
                        modifier = Modifier.size
                        // larger when active, can be same size as well?
                            (if (currentRoute == item.route) 28.dp else 22.dp),
                        tint = navIcon
                      //label = { Text(item.label) } label visibility optional
                    )
                },
                alwaysShowLabel = false,
                colors = NavigationBarItemDefaults.colors(
                    indicatorColor = Color.Transparent // no active background color
                )
            )
        }
    }
}
