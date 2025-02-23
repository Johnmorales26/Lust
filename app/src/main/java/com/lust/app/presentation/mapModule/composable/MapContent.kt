package com.lust.app.presentation.mapModule.composable

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.lust.app.BuildConfig
import com.lust.app.R
import com.lust.app.data.entities.LocationData
import com.lust.app.data.entities.PlaceType
import com.mapbox.geojson.Point
import com.mapbox.maps.MapboxExperimental
import com.mapbox.maps.Style
import com.mapbox.maps.extension.compose.MapboxMap
import com.mapbox.maps.extension.compose.animation.viewport.MapViewportState
import com.mapbox.maps.extension.compose.annotation.generated.PointAnnotation
import com.mapbox.maps.extension.compose.annotation.rememberIconImage
import com.mapbox.maps.extension.compose.style.GenericStyle
import com.mapbox.maps.extension.compose.style.rememberStyleState

@OptIn(MapboxExperimental::class)
@Composable
fun MapContent(
    modifier: Modifier,
    mapViewportState: MapViewportState,
    locations: List<LocationData>,
    showLocationData: LocationData?,
    onLocationClick: (String) -> Unit,
    onFilterClick: (Int) -> Unit,
    onMyLocationClick: () -> Unit,
    onNavigateClick: (LocationData) -> Unit,
    onAddLocationClick: (String, String) -> Unit
) {
    Box(modifier = modifier.fillMaxSize()) {
        MapboxMap(
            modifier = Modifier.fillMaxSize(),
            compass = {},
            scaleBar = {},
            logo = {},
            attribution = {},
            style = {
                GenericStyle(
                    styleState = rememberStyleState {
                        styleInteractionsState.onMapLongClicked { context ->
                            val lat = context.coordinateInfo.coordinate.latitude().toString()
                            val lng = context.coordinateInfo.coordinate.longitude().toString()
                            onAddLocationClick(lat, lng)
                            true
                        }
                    },
                    style = Style.STANDARD,
                )
            },
            mapViewportState = mapViewportState
        ) {
            locations.forEach { location ->
                val marker = rememberIconImage(
                    key = location.category.image,
                    painter = painterResource(location.category.image)
                )
                PointAnnotation(
                    point = Point.fromLngLat(location.longitude, location.latitude),
                    onClick = {
                        location.uid?.let { onLocationClick(it) }
                        true
                    }
                ) {
                    iconImage = marker
                    iconColor = location.category.color
                    iconSize = 1.5
                }
            }
        }

        FilterChipsRow(onFilterClick = onFilterClick)
        Column(
            modifier = Modifier.align(Alignment.BottomCenter),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.End
        ) {
            MyLocationButton(
                modifier = Modifier,
                onMyLocationClick = onMyLocationClick
            )
            showLocationData?.let { location ->
                LocationInfoCard(
                    location = location,
                    onNavigateClick = onNavigateClick,
                    onHideClick = { onLocationClick("") }
                )
            }
            AdMobBanner(if (BuildConfig.DEBUG) BuildConfig.AD_UNIT_ID_BANNER_DEBUG else BuildConfig.AD_UNIT_ID_BANNER_RELEASE)
        }
    }
}

@Composable
private fun FilterChipsRow(onFilterClick: (Int) -> Unit) {
    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
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
                onClick = { onFilterClick(item.id) },
                label = { Text(stringResource(item.title)) },
                colors = AssistChipDefaults.elevatedAssistChipColors(
                    leadingIconContentColor = item.color
                ),
            )
        }
    }
}

@Composable
private fun MyLocationButton(modifier: Modifier, onMyLocationClick: () -> Unit) {
    FloatingActionButton(
        modifier = modifier
            .padding(end = 8.dp, bottom = 8.dp),
        onClick = onMyLocationClick
    ) {
        Icon(
            painter = painterResource(R.drawable.ic_my_location),
            contentDescription = null
        )
    }
}

@Composable
private fun LocationInfoCard(
    location: LocationData,
    onNavigateClick: (LocationData) -> Unit,
    onHideClick: () -> Unit
) {
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(location.name, style = MaterialTheme.typography.titleLarge)
                Text(location.description, style = MaterialTheme.typography.bodyMedium)
                Text(location.distanceInKm, style = MaterialTheme.typography.labelMedium)
            }
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(onClick = onHideClick) {
                    Icon(
                        painter = painterResource(R.drawable.ic_hide),
                        contentDescription = null
                    )
                }
                Button(onClick = { onNavigateClick(location) }) {
                    Icon(
                        painter = painterResource(R.drawable.ic_navigate),
                        contentDescription = null
                    )
                }
            }
        }
    }
}