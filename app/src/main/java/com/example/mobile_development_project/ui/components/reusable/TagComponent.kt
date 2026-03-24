package com.example.mobile_development_project.ui.components.reusable

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.material3.Text
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp


@Composable
// this is tag-component with delete button in it
// tag = label text, onDeleteTag = callback when delete is clicked
fun TagComponent(
    tag: String,
    onDeleteTag: () -> Unit
) {
    // rounded container for tag and delete button
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(50))
            .background(MaterialTheme.colorScheme.secondary)
            .padding(horizontal = 12.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = tag,
            color = MaterialTheme.colorScheme.onSecondary,
        )
        Spacer(modifier = Modifier.size(8.dp)) // little space between delete-btn and tag label
        // delete "button" as box element
        Box(
            modifier = Modifier
                .size(18.dp)
                .clip(CircleShape)
                .background(Color.Black)
                .clickable { onDeleteTag() },
            contentAlignment = Alignment.Center,

        ) {
            Text(
                text = "X",
                color = Color.White,
                fontSize = 12.sp,
                lineHeight = 12.sp,
            )
        }
    }
}