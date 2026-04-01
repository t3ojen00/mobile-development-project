package com.example.mobile_development_project.data.models

import androidx.compose.ui.graphics.ImageBitmap

data class Image(
    val uri: String? = null, // from users image on device
    val localBitmap: ImageBitmap? = null, // local preview as soon as user uploads image
    val databaseUrl: String? = null    // Firebase storage downloaded url
)
