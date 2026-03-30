package com.example.mobile_development_project.ui.components.reusable

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.carousel.HorizontalMultiBrowseCarousel
import androidx.compose.material3.carousel.rememberCarouselState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import com.example.mobile_development_project.data.models.Image

// documentation from here: https://developer.android.com/develop/ui/compose/components/carousel
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ImageCarousel(
    items: List<Image>,
) {
    val state = rememberCarouselState { items.size}
    val screenWidthDp = LocalConfiguration.current.screenWidthDp.dp
    val itemWidth = (screenWidthDp - 6.dp) / 1.5f // one and a half image visible
    val roundedEdges = Modifier
        .width(itemWidth)
        .aspectRatio(1f)
        .clip(RoundedCornerShape(12.dp))

        HorizontalMultiBrowseCarousel(
            state = state,
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp)),
            preferredItemWidth = itemWidth,
            itemSpacing = 6.dp,

        ) { index ->

            val image = items[index]

            // if image is null, show placeholder
            if (image.localBitmap == null) {
                ImagePlaceholder()
            } else if (image.databaseUrl == null) {
                // if image has been added, show that image
                Image(
                    bitmap = image.localBitmap,
                    contentDescription = null,
                    modifier = roundedEdges
                )
            } else {
            // download from database, e.g. using AsyncImage() and Coil library
            }
        }

}

