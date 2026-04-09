package com.example.mobile_development_project.helpers

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.VectorDrawable
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat

// helper function to create the map marker as drawable bitmap
fun createMapMarker(
    context: Context,
    @DrawableRes resId: Int,
    size: Int,
    color: Int? = null
): Drawable {

    val drawable = ContextCompat.getDrawable(context, resId) as VectorDrawable
    color?.let {
        drawable.setTint(it)
    }

    val bitmap = Bitmap.createBitmap(
        drawable.intrinsicWidth,
        drawable.intrinsicHeight,
        Bitmap.Config.ARGB_8888
    )

    val canvas = Canvas(bitmap)
    drawable.setBounds(0, 0, canvas.width, canvas.height)
    drawable.draw(canvas)

    val scaled = Bitmap.createScaledBitmap(bitmap, size, size, false)
    return BitmapDrawable(context.resources, scaled)
}