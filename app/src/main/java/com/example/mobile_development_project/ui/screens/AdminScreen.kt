package com.example.mobile_development_project.ui.screens


import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.mobile_development_project.ui.components.reusable.PendingLocations

import com.example.mobile_development_project.viewModels.AdminViewModel
import com.example.mobile_development_project.viewModels.ProfileViewModel


@Composable
fun AdminScreen(
    viewModel: AdminViewModel = viewModel(),
    navController: NavHostController,
    role: String
) {
    LaunchedEffect(Unit) {
        viewModel.fetchPendingLocations()
        viewModel.fetchAllUsers()
    }

    val allUsers = viewModel.allUsers

    if (role == "admin") {
        Column() {
            // Admin actions
            allUsers.forEach { user ->
                Text(text = "Name: ${user.username} - email: ${user.email} - role: ${user.role})"
            )
            Spacer(modifier = Modifier.height(16.dp))

            }
            Spacer(modifier = Modifier.height(16.dp))

            PendingLocations(viewModel, navController)

        }

    }

}
