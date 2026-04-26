package com.example.mobile_development_project.ui.components.reusable

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.carousel.HorizontalMultiBrowseCarousel
import androidx.compose.material3.carousel.rememberCarouselState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.mobile_development_project.ui.theme.Attention
import com.example.mobile_development_project.ui.theme.OrangeAccent

// documentation from here: https://developer.android.com/develop/ui/compose/components/carousel
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ImageCarousel(
    items: List<String?>,
    onRemoveImage: ((Int) -> Unit)? = null,
    editMode: Boolean,
    onAddFromGallery: ((Int) -> Unit)?,
    onAddFromCamera: ((Int) -> Unit)?,
) {
    val state = rememberCarouselState { items.size }
    val screenWidthDp = LocalConfiguration.current.screenWidthDp.dp
    val itemWidth = (screenWidthDp - 6.dp) / 1.5f // one and a half image visible
    val roundedEdges = Modifier
        .width(itemWidth)
        .aspectRatio(1f)
        .clip(RoundedCornerShape(12.dp))

    var menuExpanded by remember { mutableStateOf<Int?>(null) }


    HorizontalMultiBrowseCarousel(
        state = state,
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp)),
        preferredItemWidth = itemWidth,
        itemSpacing = 6.dp,

        ) { index ->

        val imageUrl = items[index]

        Box(
            modifier = Modifier
                .width(itemWidth)
                .aspectRatio(1f)
        ) {

            val hasImage = imageUrl != null

            AsyncImage(
                model = imageUrl ?: "",
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )

            if (!hasImage) {
                ImagePlaceholder(
                    modifier = Modifier.fillMaxSize()
                )
            }

            // ADD MODE: just delete
            if (!editMode && hasImage && onRemoveImage != null) {
                IconButton(
                    modifier = Modifier
                        .align(Alignment.TopEnd),
                        //.padding(4.dp),
                    onClick = { onRemoveImage.invoke(index) }
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete",
                        tint = Attention,
                        modifier = Modifier.size(32.dp)
                    )
                }
            }

            // EDIT MODE: dropdown menu with all possible options
            if (editMode) {
                Box(modifier = Modifier.align(Alignment.TopEnd)) {
                    IconButton(onClick = { menuExpanded = index }) {
                        Icon(
                            Icons.Default.MoreVert,
                            contentDescription = "Edit",
                            tint = Color.White,
                            modifier = Modifier
                                .background(
                                    color = OrangeAccent.copy(0.8f),
                                    shape = RoundedCornerShape(4.dp),
                                )
                                .padding(4.dp)
                        )
                    }

                    DropdownMenu(
                        expanded = menuExpanded == index,
                        onDismissRequest = { menuExpanded = null },
                        offset = DpOffset((-40).dp, (-4).dp)

                    ) {

                        if (hasImage) {
                            DropdownMenuItem(
                                text = { Text("Remove image") },
                                onClick = {
                                    menuExpanded = null
                                    onRemoveImage?.invoke(index)
                                }
                            )

                            HorizontalDivider(modifier = Modifier.padding(horizontal = 6.dp))
                            DropdownMenuItem(
                                text = { Text("Replace from gallery") },
                                onClick = {
                                    menuExpanded = null
                                    onAddFromGallery?.invoke(index)
                                }
                            )
                            HorizontalDivider(modifier = Modifier.padding(horizontal = 6.dp))
                            DropdownMenuItem(
                                text = { Text("Replace by camera") },
                                onClick = {
                                    menuExpanded = null
                                    onAddFromCamera?.invoke(index)
                                }
                            )
                        }
                        else {
                            DropdownMenuItem(
                                text = { Text("Add from gallery") },
                                onClick = {
                                    menuExpanded = null
                                    onAddFromGallery?.invoke(index)
                                }
                            )
                            HorizontalDivider(modifier = Modifier.padding(horizontal = 6.dp))
                            DropdownMenuItem(
                                text = { Text("Add by camera") },
                                onClick = {
                                    menuExpanded = null
                                    onAddFromCamera?.invoke(index)
                                }
                            )

                        }
                    }
                }
            }
        }
    }
}
