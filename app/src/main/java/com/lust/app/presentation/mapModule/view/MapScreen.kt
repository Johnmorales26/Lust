package com.lust.app.presentation.mapModule.view

import android.content.Intent
import android.net.Uri
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.lust.app.data.entities.LocationData
import com.lust.app.presentation.mapModule.MapIntent
import com.lust.app.presentation.mapModule.MapState
import com.lust.app.presentation.mapModule.MapViewModel
import com.lust.app.presentation.mapModule.composable.MapContent
import com.lust.app.presentation.navigation.LocalNavController
import com.lust.app.presentation.navigation.Routes
import com.mapbox.geojson.Point
import com.mapbox.maps.extension.compose.animation.viewport.MapViewportState
import org.koin.androidx.compose.koinViewModel

@Composable
fun MapScreen(modifier: Modifier = Modifier, vm: MapViewModel = koinViewModel()) {
    val context = LocalContext.current
    val state by vm.state.collectAsState(MapState.Init)
    val navController = LocalNavController.current

    val mapViewportState = rememberMapViewportState()
    val (showLocationData, setShowLocationData) = remember { mutableStateOf<LocationData?>(null) }
    val locations = rememberLocations(state)

    LaunchedEffect(Unit) { vm.launchIntent(MapIntent.GetLocations) }
    LaunchedEffect(state) { handleStateEffects(state, mapViewportState, setShowLocationData, context) }

    MapContent(
        modifier = modifier,
        mapViewportState = mapViewportState,
        locations = locations,
        showLocationData = showLocationData,
        onLocationClick = { vm.launchIntent(MapIntent.ShowInfoLocation(it)) },
        onFilterClick = { vm.launchIntent(MapIntent.FilterPlaces(it)) },
        onMyLocationClick = { vm.launchIntent(MapIntent.GetCurrentLocation) },
        onNavigateClick = { navigateToMaps(context, it) },
        onAddLocationClick = { lat, lng ->
            navController.navigate(Routes.AddLocationRoute.createRoute(lat = lat, lng = lng))
        }
    )
}

@Composable
private fun rememberMapViewportState(): MapViewportState {
    return remember {
        MapViewportState().apply {
            setCameraOptions {
                pitch(45.0)
                zoom(15.5)
                bearing(-17.6)
                center(Point.fromLngLat(19.43312659851937, -99.12982261372065))
            }
        }
    }
}

@Composable
private fun rememberLocations(state: MapState): List<LocationData> {
    return remember(state) {
        if (state is MapState.SuccessFetchLocations) state.locations.locations else emptyList()
    }
}

private fun handleStateEffects(
    state: MapState,
    mapViewportState: MapViewportState,
    setShowLocationData: (LocationData?) -> Unit,
    context: android.content.Context
) {
    when (state) {
        is MapState.SuccessLocation -> {
            val lat = state.location.latitude
            val lon = state.location.longitude
            mapViewportState.setCameraOptions {
                center(Point.fromLngLat(lon, lat))
            }
        }
        is MapState.ShowInfoLocation -> setShowLocationData(state.locationData)
        is MapState.HideLocationData -> setShowLocationData(null)
        is MapState.Error -> showToast(context, state.msg)
        else -> {}
    }
}

private fun showToast(context: android.content.Context, message: String) {
    android.widget.Toast.makeText(context, message, android.widget.Toast.LENGTH_SHORT).show()
}

private fun navigateToMaps(context: android.content.Context, location: LocationData) {
    val route = "geo:0,0?q=${location.latitude},${location.longitude}(${location.name})"
    val gmmIntentUri = Uri.parse(route)
    val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri).apply {
        setPackage("com.google.android.apps.maps")
    }
    context.startActivity(mapIntent)
}