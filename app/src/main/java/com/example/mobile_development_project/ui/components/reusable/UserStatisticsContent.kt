package com.example.mobile_development_project.ui.components.reusable

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun UserStatisticsContent(
    followingCount: Int,
    followersCount: Int,
    totalFavoritesCount: Int,
    modifier: Modifier = Modifier
) {
    val values = listOf(
        "Following" to followingCount,
        "Followers" to followersCount,
        "Favorites" to totalFavoritesCount
    )

    val maxValue = values.maxOfOrNull { it.second }?.coerceAtLeast(1) ?: 1

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = Color(0xFF3F3F3F),
                shape = RoundedCornerShape(16.dp)
            )
            .padding(16.dp)
    ) {
        Text(
            text = "User engagement statistics",
            color = Color.White,
            style = MaterialTheme.typography.titleMedium
        )

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = "This chart compares social activity metrics for the current user.",
            color = Color.LightGray,
            style = MaterialTheme.typography.bodySmall
        )

        Spacer(modifier = Modifier.height(20.dp))

        values.forEach { (label, value) ->
            StatisticBarRow(
                label = label,
                value = value,
                maxValue = maxValue
            )

            Spacer(modifier = Modifier.height(14.dp))
        }
    }
}

@Composable
private fun StatisticBarRow(
    label: String,
    value: Int,
    maxValue: Int
) {
    val barFraction = value.toFloat() / maxValue.toFloat()

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = label,
                color = Color.White,
                style = MaterialTheme.typography.bodyMedium
            )

            Text(
                text = value.toString(),
                color = Color.White,
                style = MaterialTheme.typography.bodyMedium
            )
        }

        Spacer(modifier = Modifier.height(6.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(18.dp)
                .background(
                    color = Color(0xFFE0E0E0),
                    shape = RoundedCornerShape(9.dp)
                )
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(barFraction)
                    .height(18.dp)
                    .background(
                        color = Color(0xFFE29750),
                        shape = RoundedCornerShape(9.dp)
                    )
                    .align(Alignment.CenterStart)
            )
        }
    }
}