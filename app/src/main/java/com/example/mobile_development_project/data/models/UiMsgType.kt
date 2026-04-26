package com.example.mobile_development_project.data.models

// UI messages type
enum class MsgType {
    SUCCESS,
    ERROR,
    LOADING
}
enum class ErrorCause {
    LOCATION_UNAVAILABLE,
    LOCATION_FETCH_FAILED,
    LOCATION_PERMISSION_DENIED,
    COORDINATES_MISSING,
    BLANK_FIELDS,
    USER_ERROR,
    GENERAL_FETCH_FAIL,
    GENERAL_UPDATE_FAIL,
    GENERAL_SAVE_FAIL,
    TAG_EXISTS,
    DELETE_FAIL

}