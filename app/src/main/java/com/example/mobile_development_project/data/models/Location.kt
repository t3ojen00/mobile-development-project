package com.example.mobile_development_project.data.models

data class Location(
    val id: String = "",
    val ownerId: String = "",
    val ownerUsername: String = "",
    val name: String = "",
    val description: String = "",
    val tags: List<String> = emptyList(),
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val previewImageUrl: String = "",
    val status: String = "pending",
    val createdAt: String? = null, // date and time
    val updatedAt: String? = null,
    val favoritesCount: Int = 0
)