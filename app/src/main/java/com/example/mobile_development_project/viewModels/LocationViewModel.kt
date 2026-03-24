package com.example.mobile_development_project.viewModels

import androidx.lifecycle.ViewModel
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue

class AddLocationViewModel : ViewModel() {

    var locationName by mutableStateOf("")
    var description by mutableStateOf("")
    var tags by mutableStateOf(listOf<String>())
    var images by mutableStateOf(listOf<String>()) // if images are stored as uri's
    var gpsCoordinates by mutableStateOf<Pair<Double, Double>?>(null) // long & lat

    // todo: functions
    }