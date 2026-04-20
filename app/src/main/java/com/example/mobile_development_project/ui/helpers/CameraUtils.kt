package com.example.mobile_development_project.ui.helpers

import android.content.Context
import android.net.Uri
import androidx.core.content.FileProvider
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun createImageUri(context: Context): Uri {
    val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
    val imagesDir = File(context.cacheDir, "images").apply { mkdirs() }

    val imageFile = File.createTempFile(
        "JPEG_${timeStamp}_",
        ".jpg",
        imagesDir
    )

    return FileProvider.getUriForFile(
        context,
        "${context.packageName}.fileprovider",
        imageFile
    )
}