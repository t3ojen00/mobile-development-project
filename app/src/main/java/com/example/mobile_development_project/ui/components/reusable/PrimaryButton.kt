package com.example.mobile_development_project.ui.components.reusable

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp

@Composable

fun PrimaryButton(
    label: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    fontWeight: FontWeight? = null,
    fontSize: TextUnit? = null,
    colors: ButtonColors = ButtonDefaults.buttonColors(
        containerColor = MaterialTheme.colorScheme.primary,
        contentColor = MaterialTheme.colorScheme.onPrimary
    ),
    enabled: Boolean = true, // initially enabled and clickable
)
{
    Button(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(), // full width button
        colors = colors,
        enabled = enabled,
    ) {
        Text(
            label,
            fontWeight = fontWeight,
            // default 14.sp but can be changed
            fontSize = fontSize ?: 14.sp
        )
    }
}