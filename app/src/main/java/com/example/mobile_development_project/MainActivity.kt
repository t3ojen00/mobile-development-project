package com.example.mobile_development_project

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import org.osmdroid.config.Configuration
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import com.example.mobile_development_project.ui.theme.MobiledevelopmentprojectTheme
import com.example.mobile_development_project.viewModels.AnalyticsViewModel
import com.google.firebase.auth.FirebaseAuth

class MainActivity : ComponentActivity() {

    private val analyticsViewModel: AnalyticsViewModel by viewModels()

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
    override fun onStart() {
        super.onStart()
        analyticsViewModel.startSession()
    }
    override fun onPause() {
        super.onPause()

        val uid = FirebaseAuth.getInstance().currentUser?.uid ?: return
        analyticsViewModel.endSession(uid)
    }

    override fun onStop() {
        super.onStop()

        val uid = FirebaseAuth.getInstance().currentUser?.uid ?: return
        analyticsViewModel.endSession(uid)
    }
}
