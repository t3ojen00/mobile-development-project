package com.example.mobile_development_project.data.models

data class DailyMetrics(
    val activeUsers: Int,
    val avgSessionDuration: Double,
    val totalSessions: Int,
    val totalUsers: Int
)