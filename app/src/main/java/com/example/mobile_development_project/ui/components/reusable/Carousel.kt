package com.example.mobile_development_project.ui.components.reusable

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalFoundationApi::class)
@Composable
// this is generic carousel and allows any data type
fun <T> PagerCarousel(
    items: List<T>,
    modifier: Modifier = Modifier,
    itemContent: @Composable (T) -> Unit
) {
    val carouselPage = items.chunked(2) // two items per carousel page
    val pagerState = rememberPagerState(pageCount = { carouselPage.size })

    Column(modifier = modifier) {

        HorizontalPager(
            state = pagerState,
            pageSpacing = 8.dp,
            modifier = Modifier.fillMaxWidth()
        ) { page ->

            val currentImages = carouselPage[page]

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {

                currentImages.forEach { item ->
                    Box(
                        modifier = Modifier.weight(1f)
                    ) {
                        itemContent(item)
                    }
                    // if there's only 1 item on carouselPage, fill remaining empty space
                    if (currentImages.size == 1) {
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // dots as indicator of carousel pages
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            repeat(carouselPage.size) { index ->
                val isSelected = pagerState.currentPage == index // current page is selected index
                Box(
                    modifier = Modifier
                        .padding(4.dp)
                        .size(if (isSelected) 9.dp else 8.dp) // slightly larger when selected
                        .clip(CircleShape) // turn into circle
                        .background(
                            // also dynamic color indication of selected/not selected page
                            if (isSelected) Color.Black else Color.Gray
                        )
                )
            }
        }
    }
}