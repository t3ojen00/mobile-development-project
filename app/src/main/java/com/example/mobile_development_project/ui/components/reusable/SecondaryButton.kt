package com.example.mobile_development_project.ui.components.reusable

import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable

fun SecondaryButton(
    label: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    colors: ButtonColors = ButtonDefaults.buttonColors(
        containerColor = MaterialTheme.colorScheme.secondary,
        contentColor = MaterialTheme.colorScheme.onSecondary
    ),
    enabled: Boolean = true, // initially enabled and clickable
)
{
    Button(
        onClick = onClick,
        modifier = modifier, // initially just small btn, but make bigger with modifier or use the PrimaryButton
        colors = colors,
        enabled = enabled,
    ) {
        Text(label)
    }
}