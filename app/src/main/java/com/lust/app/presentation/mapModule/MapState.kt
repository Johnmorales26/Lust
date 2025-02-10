package com.lust.app.presentation.mapModule

import android.location.Location
import com.lust.app.data.entities.LocationData
import com.lust.app.data.entities.Locations

sealed class MapState {
    data object Init: MapState()
    data object HideLocationData: MapState()

    data class SuccessLocation(val location: Location): MapState()
    data class SuccessFetchLocations(val locations: Locations): MapState()
    data class ShowInfoLocation(val locationData: LocationData): MapState()
    data class Error(val msg: String): MapState()
}