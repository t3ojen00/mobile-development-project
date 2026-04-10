package com.example.mobile_development_project.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.mobile_development_project.R
import com.example.mobile_development_project.navigation.NavRoutes
import com.example.mobile_development_project.helpers.createMapMarker
import com.example.mobile_development_project.ui.components.reusable.PrimaryButton
import com.example.mobile_development_project.viewModels.MapViewModel
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker

@Composable
fun MapScreen(
    viewModel: MapViewModel = viewModel(),
    navController: NavHostController
) {
    val context = LocalContext.current
    // custom markers
    val markerColor = MaterialTheme.colorScheme.primary.toArgb()
    val pendingLocationColor = MaterialTheme.colorScheme.secondary.toArgb()
    val scaledMarker = createMapMarker(context, R.drawable.location_marker, 120, markerColor)
    val pendingLocationMarker = createMapMarker(context, R.drawable.location_marker, 120, pendingLocationColor)


    // fetch all approved locations from database
    LaunchedEffect(Unit) {
        viewModel.fetchApprovedLocations()
    }

    val approvedLocations = viewModel.approvedLocations

    val pendingLocationLat = navController.previousBackStackEntry
        ?.savedStateHandle
        ?.get<Double>("lat")
    val pendingLocationLng = navController.previousBackStackEntry
        ?.savedStateHandle
        ?.get<Double>("lng")


    val mapView = remember {
        MapView(context).apply {
            setTileSource(TileSourceFactory.MAPNIK)
            setMultiTouchControls(true)

            val startPoint = GeoPoint(65.0099, 25.4730)
            controller.setZoom(12.0)
            controller.setCenter(startPoint)
        }
    }

    LaunchedEffect(approvedLocations, pendingLocationLat, pendingLocationLng) {
        mapView.overlays.clear()
        approvedLocations.forEach { location ->
            val marker = Marker(mapView).apply {
                position = GeoPoint(location.latitude, location.longitude)
                setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
                title = location.name
                icon = scaledMarker
                setOnMarkerClickListener { _, _ ->
                    navController.navigate(
                        NavRoutes.LocationDetail.replace("{id}", location.id)
                    )
                    true
                }
            }
            mapView.overlays.add(marker)
        }
        if (pendingLocationLat != null && pendingLocationLng != null) {
            val marker = Marker(mapView).apply {
                position = GeoPoint(pendingLocationLat, pendingLocationLng)
                icon = pendingLocationMarker
                setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
                title = "Pending location"
            }
            mapView.overlays.add(marker)
            mapView.controller.setCenter(marker.position)
        }
        mapView.invalidate()
    }

    DisposableEffect(Unit) {
        mapView.onResume()
        onDispose {
            mapView.onPause()
            mapView.onDetach()
            navController.previousBackStackEntry
                ?.savedStateHandle
                ?.remove<Double>("lat")

            navController.previousBackStackEntry
                ?.savedStateHandle
                ?.remove<Double>("lng")
        }
    }
    Box(modifier = Modifier.fillMaxSize()) {
        AndroidView(
            factory = { mapView },
            modifier = Modifier.fillMaxSize()
        )
        if (pendingLocationLat != null && pendingLocationLng != null) {
            PrimaryButton(
                onClick = { navController.popBackStack() },
                label = "Back to moderation",
                modifier = Modifier
                    .padding(16.dp)
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 40.dp),
            )
        }
    }
}