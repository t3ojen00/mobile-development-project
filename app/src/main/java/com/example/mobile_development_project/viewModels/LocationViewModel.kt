package com.example.mobile_development_project.viewModels

import androidx.lifecycle.ViewModel
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.example.mobile_development_project.data.models.MsgType
import com.example.mobile_development_project.data.models.Location
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.Locale


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

    // ui messages
    var uiMessage by mutableStateOf<Pair<String, MsgType>?>(null)
        private set

    // setters for variables above
    fun onLocationNameChange(value: String) {
        locationName = value
    }
    fun onDescriptionChange(value: String) {
        description = value
    }
    // method to save location to firebase
    fun saveLocation(): Boolean {
        // validate fields not being empty
        if (locationName.isBlank() || description.isBlank()) {
            uiMessage = "Fields can't be empty" to MsgType.ERROR
            return false
        }

        val formatter = SimpleDateFormat(
            "dd.MM.yyyy HH:mm", Locale("fi", "FI")
        )
        val createdAtStr = formatter.format(java.util.Date())
        val updatedAtStr = formatter.format(java.util.Date())


        val location = Location(
            id = "", // Firebase generates
            ownerId = "currentUserId",
            ownerUsername = "currentUsername",
            name = locationName,
            description = description,
            tags = tags,
            latitude = gpsCoordinates?.first ?: 0.0,
            longitude = gpsCoordinates?.second ?: 0.0,
            createdAt = createdAtStr,
            updatedAt = null
        )

        db.collection("locations")
            .add(location)
            .addOnSuccessListener {
                //uiMessage = "Location saved successfully" to MsgType.SUCCESS -
                // > this will be used on edit location method, if we use overlay instead with first time add
                println("Saved successfully")
            }
            .addOnFailureListener {
                uiMessage = "Error saving location" to MsgType.ERROR
                println("Error: ${it.message}")
            }
        return true
    }
    // clear message from ui display
    fun clearMessage() {
        uiMessage = null
    }

    // logic for tags, tag can be added only once per location and can't be empty
    fun addTag(tag: String) {
        if (tag.isNotBlank() && !tags.contains(tag)) {
            tags = tags + tag
        } else {
            if (tags.contains(tag)) {
                uiMessage = "Tag already exists" to MsgType.ERROR
            }
        }
    }
    fun removeTag(tag: String) {
        tags = tags - tag
    }

    // clear form
    fun clearForm() {
        locationName = ""
        description = ""
        tags = listOf()
        gpsCoordinates = null
    }
}