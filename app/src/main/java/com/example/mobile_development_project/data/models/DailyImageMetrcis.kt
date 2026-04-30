package com.example.mobile_development_project.data.models

data class DailyImageMetrics(
    val uploadedImages: Int = 0,
    val activeUsers: Int = 0,
    val avgImagesPerUser: Double = 0.0,
    val totalSizeMb: Double = 0.0
)