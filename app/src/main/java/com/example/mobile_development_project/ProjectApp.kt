package com.example.mobile_development_project

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
//import com.example.mobile_development_project.feature_map.MapScreen
import com.example.mobile_development_project.navigation.Navigation
import com.example.mobile_development_project.ui.components.navigation.BottomBar
import com.example.mobile_development_project.ui.components.navigation.TopBar

@Composable
fun ProjectApp() {

    val navController = rememberNavController()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = { TopBar(navController = navController) },
        bottomBar = { BottomBar(navController = navController) }
    ) { innerPadding ->

        Navigation(
            navController = navController,
            modifier = Modifier.padding(innerPadding)
        )
    }
}
