package com.example.mobile_development_project.data.models

data class User(
    val id: String = "",
    val email: String = "",
    val username: String = "",
    val displayName: String = "",
    val role: String = "user",
    val createdAt: String? = "",
    val updatedAt: String? = "",
    val isActive: Boolean = true
)