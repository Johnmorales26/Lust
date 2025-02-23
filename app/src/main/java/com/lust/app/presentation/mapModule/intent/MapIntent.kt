package com.lust.app.presentation.mapModule.intent

sealed class MapIntent {
    data class ShowInfoLocation(val id: String) : MapIntent()
    data class FilterPlaces(val id: Int): MapIntent()

    data object GetCurrentLocation : MapIntent()
    data object HideLocationData : MapIntent()
    data object GetLocations : MapIntent()
}