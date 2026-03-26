package com.example.mobile_development_project.viewModels

import androidx.lifecycle.ViewModel
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.google.firebase.firestore.FirebaseFirestore

class LocationViewModel : ViewModel() {

    val db = FirebaseFirestore.getInstance()

    var locationName by mutableStateOf("")
        private set
    var description by mutableStateOf("")
        private set
    var tags by mutableStateOf(listOf<String>())
        private set
    var images by mutableStateOf(listOf<String>()) // if images are stored as uri's
        private set
    var gpsCoordinates by mutableStateOf<Pair<Double, Double>?>(null) // long & lat
        private set

    // setters for variables above
    fun onLocationNameChange(value: String) {
        locationName = value
    }
    fun onDescriptionChange(value: String) {
        description = value
    }
    // saves location to firebase
    fun saveLocation() {
        val location = hashMapOf(
            "name" to locationName,
            "description" to description,
            "tags" to tags,
            "lat" to gpsCoordinates?.first,
            "lng" to gpsCoordinates?.second
        )

        db.collection("locations")
            .add(location)
            .addOnSuccessListener {
                println("Saved successfully")
            }
            .addOnFailureListener {
                println("Error: ${it.message}")
            }
    }

    // logic for tags
    fun addTag(tag: String) {
        tags = tags + tag
    }
    fun removeTag(tag: String) {
        tags = tags - tag
    }
}