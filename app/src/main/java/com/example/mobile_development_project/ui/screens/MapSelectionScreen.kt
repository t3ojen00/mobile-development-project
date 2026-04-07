package com.example.mobile_development_project.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavHostController
import com.example.mobile_development_project.R
import com.example.mobile_development_project.ui.components.reusable.PrimaryButton
import com.example.mobile_development_project.ui.createMapMarker
import org.osmdroid.util.GeoPoint
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker

@Composable
fun MapSelectionScreen(
    navController: NavHostController,
) {
    val context = LocalContext.current
    var selectedPoint by remember { mutableStateOf<org.osmdroid.util.GeoPoint?>(null) }

    // custom marker
    val scaledMarker = remember {
        createMapMarker(context, R.drawable.location_marker, 150)
    }

    // create OpenStreet map
    val mapView = remember {
        MapView(context).apply {
            setTileSource(TileSourceFactory.MAPNIK)
            setMultiTouchControls(true)

            val startPoint = GeoPoint(65.0099, 25.4730)

            controller.setZoom(12.0)
            controller.setCenter(startPoint)
        }
    }

    // overlay and tap listener
    DisposableEffect(mapView) {
        val eventsOverlay = org.osmdroid.views.overlay.MapEventsOverlay(
            object : org.osmdroid.events.MapEventsReceiver {
                override fun singleTapConfirmedHelper(p: org.osmdroid.util.GeoPoint?): Boolean {
                    p?.let { it ->
                        selectedPoint = it
                        mapView.overlays.removeAll { it is Marker } // only one marker visible, not all clicks
                        val marker = Marker(mapView).apply {
                            position = it
                            setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
                            icon = scaledMarker // use the custom marker instead of default by OpenStreet
                            title = "Selected location"
                        }
                        mapView.overlays.add(marker)
                        mapView.invalidate()
                    }
                    return true
                }
                override fun longPressHelper(p: org.osmdroid.util.GeoPoint): Boolean = false
            }
        )
        mapView.overlays.add(eventsOverlay)
        mapView.onResume()
        onDispose {
            mapView.onPause()
            mapView.onDetach()
        }
    }

    Column(modifier = Modifier.fillMaxSize())
    {
        AndroidView(
            factory = { mapView },
            modifier = Modifier
                .weight(1f)
                .fillMaxSize()
        )

        PrimaryButton(
            onClick = {
                // return selected point to previous screen (AddLocation screen)
                selectedPoint?.let { point ->
                    navController.previousBackStackEntry
                        ?.savedStateHandle
                        ?.set("selected_location", point.latitude to point.longitude)
                }
                navController.popBackStack()
            },
            label = "Confirm location"
        )
    }
}