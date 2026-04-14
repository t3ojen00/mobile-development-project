package com.example.mobile_development_project.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.mobile_development_project.ui.theme.Burgundy
import com.example.mobile_development_project.viewModels.AdminViewModel
import com.example.mobile_development_project.ui.components.admin_view.PendingLocations
import com.example.mobile_development_project.ui.components.admin_view.RejectedLocations
import com.example.mobile_development_project.ui.components.admin_view.UsersList


@Composable
fun AdminScreen(
    viewModel: AdminViewModel = viewModel(),
    navController: NavHostController,
    role: String?
) {
    LaunchedEffect(Unit) {
        viewModel.fetchPendingLocations()
        viewModel.fetchAllUsers()
        viewModel.fetchRejectedLocations()
    }
    var selectedTab by remember { mutableStateOf(0) }

    if (role == "admin") {
        Column(modifier = Modifier.fillMaxSize())
        {
            PrimaryTabRow(
                selectedTabIndex = selectedTab,
                containerColor = Color.White,
                contentColor = Burgundy
            ) {
                Tab(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    text = { Text("Pending") },
                    selectedContentColor = Burgundy,
                    unselectedContentColor = Color.DarkGray
                )
                Tab(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    text = { Text("Users") },
                    selectedContentColor = Burgundy,
                    unselectedContentColor = Color.DarkGray
                )
                Tab(
                    selected = selectedTab == 2,
                    onClick = { selectedTab = 2 },
                    text = { Text("Rejected") },
                    selectedContentColor = Burgundy,
                    unselectedContentColor = Color.DarkGray
                )
            }

            HorizontalDivider(color = Color.Gray)

            Spacer(modifier = Modifier.height(14.dp))

            when (selectedTab) {
                0 -> PendingLocations(
                    viewModel,
                    navController,
                    role
                )

                1 -> UsersList(
                    users = viewModel.allUsers,
                    viewModel = viewModel,
                    onDelete = { user ->
                        viewModel.deleteUser(user.id)
                    },
                    onPromote = { user, role ->
                        viewModel.updateUserRole(user.id, role)
                    },
                    modifier = Modifier.fillMaxWidth()
                )

                2 -> RejectedLocations(
                    viewModel,
                    navController,
                )
                /*
                3 -> Statistics()
              */
            }
        }
    }
    else if(role == "moderator"){
        PendingLocations(viewModel, navController, role)
    }
}



