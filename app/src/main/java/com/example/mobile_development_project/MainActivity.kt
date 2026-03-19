package com.example.mobile_development_project

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import org.osmdroid.config.Configuration
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.example.mobile_development_project.navigation.Navigation
import com.example.mobile_development_project.ui.components.navigation.BottomBar
import com.example.mobile_development_project.ui.components.navigation.TopBar
import com.example.mobile_development_project.ui.theme.MobiledevelopmentprojectTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        Configuration.getInstance().userAgentValue = packageName

        setContent {
            MobiledevelopmentprojectTheme {
                val navController = rememberNavController()
                Scaffold(
                    topBar = { TopBar(navController = navController) },
                    bottomBar = { BottomBar(navController)},
                    modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Navigation(
                        navController = navController,
                        modifier = Modifier.padding(innerPadding))

                }
            }
        }
    }
}
