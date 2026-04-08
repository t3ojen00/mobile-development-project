package com.example.mobile_development_project.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.mobile_development_project.navigation.NavRoutes
import com.example.mobile_development_project.ui.components.reusable.PrimaryButton
import com.example.mobile_development_project.ui.theme.Burgundy
import com.example.mobile_development_project.ui.theme.OrangeAccent
import com.example.mobile_development_project.ui.theme.ScreenBackground
import com.example.mobile_development_project.viewModels.ProfileViewModel

@Composable
fun UserProfileScreen(
    userId: String? = null,
    navController: NavHostController,
    viewModel: ProfileViewModel = viewModel()
) {
    LaunchedEffect(Unit) {
        viewModel.loadUser()
    }

    val user = viewModel.user

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
        user == null -> "user XXXX"
        user.username.isNotBlank() -> user.username
        else -> user.email
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
                onClick = { },
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
        ){
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

        HorizontalDivider(color = Color.Gray)

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 14.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Text(
                text = "My locations",
                color = Color.DarkGray,
                modifier = Modifier
                    .clickable { navController.navigate(NavRoutes.UserLocations) }
                    .padding(4.dp)
            )

            Text(
                text = "My images",
                color = Color.DarkGray,
                modifier = Modifier
                    .clickable { navController.navigate(NavRoutes.UserImages) }
                    .padding(4.dp)
            )

            Text(
                text = "Following",
                color = Color.DarkGray,
                modifier = Modifier
                    .clickable { navController.navigate(NavRoutes.Favorites) }
                    .padding(4.dp)
            )
        }

        HorizontalDivider(color = Color.Gray)
    }
}