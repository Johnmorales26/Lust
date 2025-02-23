package com.lust.app.presentation.mapModule

import android.content.Intent
import android.net.Uri
import android.view.GestureDetector
import android.view.MotionEvent
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.ElevatedAssistChip
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.lust.app.R
import com.lust.app.data.entities.LocationData
import com.lust.app.data.entities.PlaceType
import com.mapbox.geojson.Point
import com.mapbox.maps.Style
import com.mapbox.maps.extension.compose.MapboxMap
import com.mapbox.maps.extension.compose.animation.viewport.MapViewportState
import com.mapbox.maps.extension.compose.annotation.generated.PointAnnotation
import com.mapbox.maps.extension.compose.annotation.rememberIconImage
import com.mapbox.maps.extension.compose.style.MapStyle
import com.mapbox.maps.plugin.gestures.OnMapLongClickListener
import com.mapbox.maps.plugin.gestures.OnRotateListener
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
    val context = LocalContext.current

    Box(modifier = modifier.fillMaxSize()) {
        MapboxMap(
            modifier = Modifier
                .fillMaxSize(),
            compass = {},
            scaleBar = {},
            logo = {},
            attribution = {},
            style = { MapStyle(Style.DARK) },
            mapViewportState = mapViewportState
        ) {
            locations.forEach { location ->
                val markerResourceId = remember { location.category.image }
                val marker = rememberIconImage(
                    key = markerResourceId,
                    painter = painterResource(markerResourceId)
                )
                PointAnnotation(
                    point = Point.fromLngLat(location.longitude, location.latitude),
                    onClick = {
                        location.uid?.let {
                            vm.launchIntent(MapIntent.ShowInfoLocation(it))
                        }
                        true
                    }
                ) {
                    iconImage = marker
                    iconColor = location.category.color
                    iconSize = 1.5
                }
            }
        }

        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.TopStart),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            itemsIndexed(PlaceType.entries) { _, item ->
                ElevatedAssistChip(
                    leadingIcon = {
                        Icon(
                            painter = painterResource(item.image),
                            contentDescription = null
                        )
                    },
                    onClick = { vm.launchIntent(MapIntent.FilterPlaces(item.id)) },
                    label = { Text(stringResource(item.title)) },
                    colors = AssistChipDefaults.elevatedAssistChipColors(
                        leadingIconContentColor = item.color
                    ),
                )
            }
        }

        Column(
            modifier = Modifier
                .wrapContentHeight()
                .fillMaxWidth()
                .align(Alignment.BottomCenter),
            horizontalAlignment = Alignment.End,
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            FloatingActionButton(
                modifier = Modifier
                    .padding(end = 8.dp, bottom = 8.dp),
                onClick = { vm.launchIntent(MapIntent.GetCurrentLocation) }) {
                Icon(
                    painter = painterResource(R.drawable.ic_my_location),
                    contentDescription = null
                )
            }
            AnimatedVisibility(
                showLocationData.first && showLocationData.second != null
            ) {
                showLocationData.second?.let {
                    ElevatedCard(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp, start = 8.dp, end = 8.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(
                                modifier = Modifier
                                    .wrapContentHeight()
                                    .weight(1f),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Text(it.name, style = MaterialTheme.typography.titleLarge)
                                Text(it.description, style = MaterialTheme.typography.bodyMedium)
                                Text(it.distanceInKm, style = MaterialTheme.typography.labelMedium)
                            }
                            Column(
                                modifier = Modifier.wrapContentHeight(),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Button(onClick = { vm.launchIntent(MapIntent.HideLocationData) }) {
                                    Icon(
                                        painter = painterResource(R.drawable.ic_hide),
                                        contentDescription = null
                                    )
                                }
                                Button(onClick = { }) {
                                    Icon(
                                        painter = painterResource(R.drawable.ic_comments),
                                        contentDescription = null
                                    )
                                }
                                Button(onClick = {
                                    val route =
                                        "geo:0,0?q=${it.latitude},${it.longitude}(${it.name})"
                                    val gmmIntentUri = Uri.parse(route)
                                    val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
                                    mapIntent.setPackage("com.google.android.apps.maps")
                                    context.startActivity(mapIntent)
                                }) {
                                    Icon(
                                        painter = painterResource(R.drawable.ic_navigate),
                                        contentDescription = null
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
