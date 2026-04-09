package com.example.mobile_development_project.viewModels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.example.mobile_development_project.data.models.ErrorCause
import com.example.mobile_development_project.data.models.MsgType
import com.example.mobile_development_project.data.models.Location
import com.example.mobile_development_project.helpers.ErrorMapper
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.Locale


class AddLocationViewModel : ViewModel() {

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
    var selectedLocation by mutableStateOf<Pair<Double, Double>?>(null)
        private set

    // ui messages
    var uiMessage by mutableStateOf<Triple<String, MsgType, ErrorCause?>?>(null)
        private set

    // setters for variables above
    fun onLocationNameChange(value: String) {
        locationName = value
    }
    fun onDescriptionChange(value: String) {
        description = value
    }

    fun formatCoordinates(value: Double): String {
        return String.format(Locale.US, "%.2f", value) // decimals separated by dot (.) instead of comma
    }
    fun setGpsCoordinates(lat: Double, lng: Double) {
        gpsCoordinates = Pair(lat, lng)
        uiMessage = Triple(
            "GPS coordinates set:\n ${formatCoordinates(lat)}, ${formatCoordinates(lng)}",
            MsgType.SUCCESS,
            null
        )
    }
    fun setSelectedLocation(lat: Double, lon: Double) {
        selectedLocation = Pair(lat, lon)
        uiMessage = Triple(
            "Selected location:\n${formatCoordinates(lat)}, ${formatCoordinates(lon)}",
            MsgType.SUCCESS,
            null
        )
    }
    fun setError(
        cause: ErrorCause? = null,
        exception: Exception? = null) {
        Log.e("AddLocationViewModel", "ERROR", exception)

        uiMessage = Triple(
            ErrorMapper.getMessage(cause),
            MsgType.ERROR,
            cause
        )
    }

    // method to save location to firebase
    fun saveLocation(onSuccess: () -> Unit) {
        // validate fields not being empty
        if (locationName.isBlank() || description.isBlank()) {
            setError(ErrorCause.BLANK_FIELDS)
            return
        }
        // validate coordinates are set
        val coordinates = selectedLocation ?: gpsCoordinates
        if (coordinates == null) {
            setError(ErrorCause.COORDINATES_MISSING)
            return
        }

            val currentUser = FirebaseAuth.getInstance().currentUser
            if (currentUser == null) {
                setError(ErrorCause.USER_ERROR)
                return
            }

            val userId = currentUser.uid
            val username = currentUser.displayName ?: currentUser.email ?: "Unknown"

            val formatter = SimpleDateFormat(
                "dd.MM.yyyy HH:mm", Locale("fi", "FI")
            )
            val createdAtStr = formatter.format(java.util.Date())

            val location = Location(
                ownerId = userId,
                ownerUsername = username,
                status = "pending",
                name = locationName,
                description = description,
                tags = tags,
                latitude = coordinates.first,
                longitude = coordinates.second,
                createdAt = createdAtStr,
            )

            uiMessage = Triple("Saving location...", MsgType.LOADING, null)

            db.collection("locations")
                .add(location)
                .addOnSuccessListener {
                    uiMessage = Triple(
                        "",
                        MsgType.SUCCESS,
                        null )
                    println("Saved successfully")
                    onSuccess()
                }
                .addOnFailureListener { e ->
                    setError(ErrorCause.GENERAL_SAVE_FAIL, e)
                }
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
                setError(ErrorCause.TAG_EXISTS)
            } else {
                setError(ErrorCause.BLANK_FIELDS)
            }
        }
    }
    fun removeTag(tag: String) {
        tags = tags - tag
    }

    fun clearForm() {
        locationName = ""
        description = ""
        tags = listOf()
        gpsCoordinates = null
    }
}