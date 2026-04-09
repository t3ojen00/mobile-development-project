package com.example.mobile_development_project.ui.components.reusable

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.mobile_development_project.data.models.Location

@Composable
fun UserImagesContent(
    locations: List<Location>,
    modifier: Modifier = Modifier
) {
    val imageLocations = locations.filter { it.previewImageUrl.isNotBlank() }

    if (imageLocations.isEmpty()) {
        Text(
            text = "No images yet",
            style = MaterialTheme.typography.bodyLarge,
            modifier = modifier
        )
        return
    }

    FlowRow(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        imageLocations.forEach {
            ImagePlaceholder(
                modifier = Modifier.width(160.dp)
            )
        }
    }
}