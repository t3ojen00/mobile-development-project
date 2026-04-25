package com.example.mobile_development_project.helpers

import com.example.mobile_development_project.data.models.ErrorCause

object ErrorMapper {
    fun getMessage(cause: ErrorCause?): String {
        return when (cause) {
            ErrorCause.LOCATION_UNAVAILABLE -> "Location not available, try again outdoors or enable GPS"
            ErrorCause.LOCATION_FETCH_FAILED -> "Failed to get location, try again"
            ErrorCause.LOCATION_PERMISSION_DENIED -> "Location permission denied, please allow in settings"
            ErrorCause.COORDINATES_MISSING -> "Coordinates missing"
            ErrorCause.BLANK_FIELDS -> "Fields can't be empty"
            ErrorCause.USER_ERROR -> "Error finding user"
            ErrorCause.TAG_EXISTS -> "Tag already exists"
            ErrorCause.GENERAL_FETCH_FAIL -> "Error fetching data"
            ErrorCause.GENERAL_UPDATE_FAIL -> "Error updating data"
            ErrorCause.GENERAL_SAVE_FAIL -> "Error saving data, please try again"
            ErrorCause.DELETE_FAIL -> "Error deleting data, please try again"
            else -> "Unexpected error"
        }
    }
}