package com.example.mobile_development_project.data.models

data class Location(
    val name: String,
    val description: String,
    val tags: List<String>,
    //val images: List<String>,
    val gpsCoordinates: Pair<Latitude, Longitude>,
    val ownerId: String
)

val locations = listOf<Location>
val latitude: Double
val longitude : Double