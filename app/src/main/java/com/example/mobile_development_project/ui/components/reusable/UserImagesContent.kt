package com.example.mobile_development_project.ui.components.reusable

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.mobile_development_project.data.models.Location
import com.example.mobile_development_project.ui.theme.AuthCardGray
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight

data class UserImageItem(
    val locationId: String,
    val locationName: String,
    val locationDescription: String,
    val imageUrl: String
)
@Composable
fun UserImagesContent(
    locations: List<Location>,
    modifier: Modifier = Modifier
) {
    val imageItems = locations.flatMap { location ->
        val urls = when {
            location.imageUrls.isNotEmpty() -> location.imageUrls
            location.previewImageUrl.isNotBlank() -> listOf(location.previewImageUrl)
            else -> emptyList()
        }

        urls.map { url ->
            UserImageItem(
                locationId = location.id,
                locationName = location.name,
                locationDescription = location.description,
                imageUrl = url
            )
        }
    }

    if (imageItems.isEmpty()) {
        Box(
            modifier = modifier
                .fillMaxWidth()
                .padding(top = 24.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "No images yet",
                style = MaterialTheme.typography.bodyLarge
            )
        }
        return
    }

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        imageItems.forEach { item ->
            Spacer(modifier = Modifier.height(12.dp))

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
                colors = CardDefaults.cardColors(containerColor = AuthCardGray),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    AsyncImage(
                        model = item.imageUrl,
                        contentDescription = item.locationName,
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(1f),
                        contentScale = ContentScale.Crop
                    )

                    Column(
                        modifier = Modifier.padding(12.dp)
                    ) {
                        Text(
                            text = item.locationName.ifBlank { "Unnamed location" },
                            color = Color.White,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = item.locationDescription.ifBlank { "No description available." },
                            color = Color.White,
                            style = MaterialTheme.typography.bodyMedium,
                            maxLines = 2
                        )
                    }
                }
            }
        }
    }
}