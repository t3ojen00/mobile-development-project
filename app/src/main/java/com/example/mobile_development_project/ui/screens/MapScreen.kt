package com.example.mobile_development_project.ui.screens

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavHostController
import com.example.mobile_development_project.navigation.NavRoutes
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker

@Composable
fun MapScreen(navController: NavHostController) {
    val context = LocalContext.current

    val mapView = remember {
        MapView(context).apply {
            setTileSource(TileSourceFactory.MAPNIK)
            setMultiTouchControls(true)

            val startPoint = GeoPoint(65.0051, 25.2819)
            controller.setZoom(12.0)
            controller.setCenter(startPoint)


            //test marker to see that the locationdetailscreen works (it doesnt render because mock id doesnt exist in firebase)
          /*  val marker = Marker(this).apply {
                position = startPoint
                setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
                title = "Test Location"

                setOnMarkerClickListener { _, _ ->
                    navController.navigate(
                        NavRoutes.LocationDetail.replace("{id}", "test123")
                    )
                    true
                }
            }

            overlays.add(marker)*/
        }
    }


    DisposableEffect(Unit) {
        mapView.onResume()
        onDispose {
            mapView.onPause()
            mapView.onDetach()
        }
    }

    AndroidView(
        factory = { mapView },
        modifier = Modifier.fillMaxSize()
    )
}