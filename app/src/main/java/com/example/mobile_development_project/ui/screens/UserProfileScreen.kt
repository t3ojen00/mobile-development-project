package com.example.mobile_development_project.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Text
import androidx.compose.material3.Tab
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.mobile_development_project.navigation.NavRoutes
import com.example.mobile_development_project.ui.components.reusable.FollowingContent
import com.example.mobile_development_project.ui.components.reusable.PrimaryButton
import com.example.mobile_development_project.ui.components.reusable.UserImagesContent
import com.example.mobile_development_project.ui.components.reusable.UserLocationContent
import com.example.mobile_development_project.ui.theme.Burgundy
import com.example.mobile_development_project.ui.theme.OrangeAccent
import com.example.mobile_development_project.ui.theme.ScreenBackground
import com.example.mobile_development_project.viewModels.AuthViewModel
import com.example.mobile_development_project.viewModels.ProfileViewModel

@Composable
fun UserProfileScreen(
    userId: String? = null,
    navController: NavHostController,
    viewModel: ProfileViewModel = viewModel(),
    authViewModel: AuthViewModel = viewModel()
) {
    LaunchedEffect(Unit) {
        android.util.Log.d("UserProfileScreen", "loadProfileData called")
        viewModel.loadProfileData()
    }

    val user = viewModel.user
    val userLocations = viewModel.userLocations
    var selectedTab by remember { mutableStateOf(0) }

    if (viewModel.isLoading) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(ScreenBackground),
            contentAlignment = Alignment.Center
        ) {
            Text("Loading profile...")
        }
        return
    }

    val displayName = when {
        user == null -> "User"
        user.displayName.isNotBlank() -> user.displayName
        user.username.isNotBlank() -> user.username
        user.email.isNotBlank() -> user.email
        else -> "User"
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ScreenBackground)
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.Top
        ) {
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = "Profile icon",
                tint = Color.DarkGray,
                modifier = Modifier.size(56.dp)
            )

            Spacer(modifier = Modifier.weight(1f))

            PrimaryButton(
                label = "Log out",
                onClick = {
                    authViewModel.logoutUser()
                },
                modifier = Modifier.height(40.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Burgundy,
                    contentColor = Color.White
                )
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Hello, $displayName",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .clickable { navController.navigate(NavRoutes.Settings) }
                .background(
                    color = OrangeAccent,
                    shape = RoundedCornerShape(20.dp)
                )
                .padding(horizontal = 10.dp, vertical = 6.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Menu,
                contentDescription = "Settings",
                tint = Color.White,
                modifier = Modifier.size(16.dp)
            )

            Spacer(modifier = Modifier.size(4.dp))

            Text(
                text = "Settings",
                color = Color.White,
                style = MaterialTheme.typography.labelMedium
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (user?.role == "admin" || user?.role == "moderator") {
            PrimaryButton(
                label = when (user.role) {
                    "admin" -> "Admin actions"
                    "moderator" -> "Moderator actions"
                    else -> ""
                },
                onClick = {
                    navController.navigate("admin/${user.role}")
                }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        HorizontalDivider(color = Color.Gray)

        PrimaryTabRow(
            selectedTabIndex = selectedTab,
            containerColor = ScreenBackground,
            contentColor = Burgundy
        ) {
            Tab(
                selected = selectedTab == 0,
                onClick = { selectedTab = 0 },
                text = { Text("My locations") },
                selectedContentColor = Burgundy,
                unselectedContentColor = Color.DarkGray
            )
            Tab(
                selected = selectedTab == 1,
                onClick = { selectedTab = 1 },
                text = { Text("My images") },
                selectedContentColor = Burgundy,
                unselectedContentColor = Color.DarkGray
            )
            Tab(
                selected = selectedTab == 2,
                onClick = { selectedTab = 2 },
                text = { Text("Following") },
                selectedContentColor = Burgundy,
                unselectedContentColor = Color.DarkGray
            )
        }

        HorizontalDivider(color = Color.Gray)

        Spacer(modifier = Modifier.height(16.dp))

        when (selectedTab) {
            0 -> UserLocationContent(
                locations = userLocations,
                navController = navController,
                modifier = Modifier.fillMaxWidth()
            )

            1 -> UserImagesContent(
                locations = userLocations,
                modifier = Modifier.fillMaxWidth()
            )

            2 -> FollowingContent(
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}