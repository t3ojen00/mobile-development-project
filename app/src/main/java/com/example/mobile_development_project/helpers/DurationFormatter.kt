package com.example.mobile_development_project.helpers

fun formatDuration(seconds: Double): String {
    val total = seconds.toLong()

    val hours = total / 3600
    val minutes = (total % 3600) / 60
    val secs = total % 60

    return when {
        hours > 0 -> "${hours}h ${minutes}m"
        minutes > 0 -> "${minutes}m ${secs}s"
        else -> "${secs}s"
    }
}