package com.example.mobile_development_project.ui.components.reusable

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.mobile_development_project.data.models.Location
import com.example.mobile_development_project.navigation.NavRoutes
import com.example.mobile_development_project.ui.theme.AuthCardGray

@Composable
fun UserLocationContent(
    locations: List<Location>,
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {

        if (locations.isEmpty()) {
            Text(
                text = "No locations yet",
                color = Color.DarkGray,
                style = MaterialTheme.typography.bodyMedium
            )
            return
        }

        locations.forEach { location ->

            Box {
                // MAIN CARD
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp)
                        .background(
                            color = AuthCardGray,
                            shape = RoundedCornerShape(16.dp)
                        )
                        .clickable {
                            navController.navigate(
                                NavRoutes.LocationDetail.replace("{id}", location.id)
                            )
                        }
                        .padding(16.dp)
                ) {

                    Text(
                        text = location.name,
                        color = Color.White,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = location.description,
                        color = Color.LightGray,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }

                // 🏷 STATUS LABEL
                StatusBadge(
                    status = location.status,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(16.dp)
                )
            }
        }
    }
}
@Composable
fun StatusBadge(
    status: String,
    modifier: Modifier = Modifier
) {
    val (text, color) = when (status.lowercase()) {
        "pending" -> "Pending" to Color(0xFFE29750)   // orange
        "approved" -> "Approved" to Color(0xFF709176) // green
        "rejected" -> "Rejected" to Color(0xFF709176) // red
        else -> status to Color.Gray
    }

    Box(
        modifier = modifier
            .background(
                color = color,
                shape = RoundedCornerShape(12.dp)
            )
            .padding(horizontal = 10.dp, vertical = 4.dp)
    ) {
        Text(
            text = text,
            color = Color.White,
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Bold
        )
    }
}