package com.example.mobile_development_project.ui.components.reusable

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.mobile_development_project.ui.theme.OrangeAccent

@Composable
fun TagComponent(
    tag: String
) {
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(50))
            .background(OrangeAccent)
            .padding(horizontal = 12.dp, vertical = 6.dp)
    ) {
        Text(
            text = tag,
            color = Color.White,
        )
    }
}