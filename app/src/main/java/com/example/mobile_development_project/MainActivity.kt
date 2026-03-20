package com.example.mobile_development_project

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import org.osmdroid.config.Configuration
import androidx.activity.enableEdgeToEdge
import com.example.mobile_development_project.ui.theme.MobiledevelopmentprojectTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        Configuration.getInstance().userAgentValue = packageName

        setContent {
            MobiledevelopmentprojectTheme {
                ProjectApp()
            }
        }
    }
}
