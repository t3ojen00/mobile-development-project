package com.example.mobile_development_project.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
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
import com.example.mobile_development_project.ui.components.reusable.FollowingContent
import com.example.mobile_development_project.ui.components.reusable.UserLocationContent
import com.example.mobile_development_project.ui.theme.Burgundy
import com.example.mobile_development_project.ui.theme.ScreenBackground

import com.example.mobile_development_project.viewModels.AdminViewModel
import androidx.compose.foundation.lazy.items
import com.example.mobile_development_project.ui.components.reusable.PendingLocations


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
    var selectedTab by remember { mutableStateOf(0) }



    if (role == "admin") {
        Column() {
            HorizontalDivider(color = Color.Gray)

            PrimaryTabRow(
                selectedTabIndex = selectedTab,
                containerColor = ScreenBackground,
                contentColor = Burgundy
            ) {
                Tab(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    text = { Text("Pending locations") },
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
                    text = { Text("Rejected locations") },
                    selectedContentColor = Burgundy,
                    unselectedContentColor = Color.DarkGray
                )
            }

            HorizontalDivider(color = Color.Gray)

            Spacer(modifier = Modifier.height(16.dp))

            when (selectedTab) {
                0 -> PendingLocations(
                    viewModel,
                    navController,
                )

                1 -> DisplayUsersCard(
                    viewModel = viewModel,
                    navController = navController,
                    onClick = { },
                    modifier = Modifier.fillMaxWidth()
                )
                /*
                2 -> RejectedLocations(
                    modifier = Modifier.fillMaxWidth()
                )*/
            }
        }
    }
}
        /*Column() {
                // Admin actions

                Spacer(modifier = Modifier.height(16.dp))

                }
                Spacer(modifier = Modifier.height(16.dp))

                PendingLocations(viewModel, navController)

            }*/

        @Composable
        fun DisplayUsersCard(
            viewModel: AdminViewModel = viewModel(),
            onClick: () -> Unit,
            navController: NavHostController,
            modifier: Modifier
        ) {
            val allUsers = viewModel.allUsers

            LazyColumn(modifier = modifier.fillMaxSize()) {
                items(allUsers) { user ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text("Name: ${user.username} (${user.role})")
                            Text("Email: ${user.email}")
                            Text("Created at: ${user.createdAt}")
                            Text("Is active: ${user.isActive}")
                        }
                    }
                }
            }
        }




