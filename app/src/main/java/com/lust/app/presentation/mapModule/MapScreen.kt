package com.lust.app.presentation.mapModule

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.lust.app.R
import com.lust.app.data.entities.LocationData
import com.mapbox.geojson.Point
import com.mapbox.maps.Style
import com.mapbox.maps.extension.compose.MapboxMap
import com.mapbox.maps.extension.compose.animation.viewport.MapViewportState
import com.mapbox.maps.extension.compose.annotation.generated.PointAnnotation
import com.mapbox.maps.extension.compose.annotation.rememberIconImage
import com.mapbox.maps.extension.compose.style.MapStyle
import org.koin.androidx.compose.koinViewModel

@Composable
fun MapScreen(modifier: Modifier = Modifier, vm: MapViewModel = koinViewModel()) {
    val context = LocalContext.current
    val state by vm.state.collectAsState(MapState.Init)
    var locations by remember { mutableStateOf(emptyList<LocationData>()) }
    var showLocationData by remember { mutableStateOf(Pair<Boolean, LocationData?>(false, null)) }

    val mapViewportState = remember {
        MapViewportState().apply {
            setCameraOptions {
                zoom(16.0)
                center(Point.fromLngLat(19.43312659851937, -99.12982261372065))
            }
        }
    }

    LaunchedEffect(state) {
        if (state is MapState.Init) {
            vm.launchIntent(MapIntent.GetCurrentLocation)
            vm.launchIntent(MapIntent.GetLocations)
        }
        if (state is MapState.Error) {
            Toast.makeText(context, (state as MapState.Error).msg, Toast.LENGTH_SHORT).show()
        }
        if (state is MapState.SuccessLocation) {
            val lat = (state as MapState.SuccessLocation).location.latitude
            val lon = (state as MapState.SuccessLocation).location.longitude
            mapViewportState.setCameraOptions {
                zoom(16.0)
                center(Point.fromLngLat(lon, lat))
            }
        }
        if (state is MapState.ShowInfoLocation) {
            val locationData = (state as MapState.ShowInfoLocation).locationData
            showLocationData = Pair(true, locationData)
        }
        if (state is MapState.HideLocationData) {
            showLocationData = Pair(false, null)
        }
        if (state is MapState.SuccessFetchLocations) {
            locations = (state as MapState.SuccessFetchLocations).locations.locations
        }
    }

    MapContent(
        modifier = modifier,
        mapViewportState = mapViewportState,
        locations = locations,
        showLocationData = showLocationData,
        vm = vm
    )
}

@Composable
fun MapContent(
    modifier: Modifier,
    mapViewportState: MapViewportState,
    locations: List<LocationData>,
    showLocationData: Pair<Boolean, LocationData?>,
    vm: MapViewModel
) {
    Box(modifier = modifier.fillMaxSize()) {
        MapboxMap(
            modifier = Modifier
                .fillMaxSize()
                .clickable {
                    vm.launchIntent(MapIntent.HideLocationData)
                },
            compass = {},
            scaleBar = {},
            logo = {},
            attribution = {},
            style = { MapStyle(Style.DARK) },
            mapViewportState = mapViewportState
        ) {
            locations.forEach { location ->
                val markerResourceId = remember { location.category.getIcon() }
                val marker = rememberIconImage(
                    key = markerResourceId,
                    painter = painterResource(markerResourceId)
                )
                PointAnnotation(
                    point = Point.fromLngLat(location.longitude, location.latitude),
                    onClick = {
                        vm.launchIntent(MapIntent.ShowInfoLocation(location.id))
                        true
                    }) {
                    iconImage = marker
                }
            }
        }

        FloatingActionButton(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .padding(end = 8.dp, bottom = 8.dp),
            onClick = { vm.launchIntent(MapIntent.GetCurrentLocation) }) {
            Icon(
                painter = painterResource(R.drawable.ic_my_location),
                contentDescription = null
            )
        }

        AnimatedVisibility(
            showLocationData.first && showLocationData.second != null,
            modifier = Modifier.align(Alignment.BottomCenter)
        ) {
            showLocationData.second?.let {
                ElevatedCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Row(modifier = Modifier.fillMaxWidth().padding(16.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text(it.name)
                        Button(onClick = { vm.launchIntent(MapIntent.HideLocationData) }) {
                            Icon(
                                painter = painterResource(R.drawable.ic_hide),
                                contentDescription = null
                            )
                        }
                    }
                }
            }
        }
    }
}
