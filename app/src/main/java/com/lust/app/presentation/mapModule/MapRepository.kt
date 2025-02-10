package com.lust.app.presentation.mapModule

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.util.Log
import com.google.android.gms.location.FusedLocationProviderClient
import com.lust.app.data.entities.LocationData
import com.lust.app.data.entities.Locations
import com.lust.app.data.utils.JsonMapper

class MapRepository(
    private val context: Context,
    private val jsonMapper: JsonMapper,
    private val fusedLocationClient: FusedLocationProviderClient) {

    private lateinit var locations: Locations

    @SuppressLint("MissingPermission")
    fun getCurrentLocation(onSuccess: (Location) -> Unit) {
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                if (location != null) {
                    val latitude = location.latitude
                    val longitude = location.longitude
                    Log.d(MapRepository::class.simpleName, "Lat: $latitude, Lon: $longitude")
                    onSuccess(location)
                }
            }
            .addOnFailureListener { exception ->
                Log.e(MapRepository::class.simpleName, "Error al obtener la ubicación: ${exception.message}")
            }
    }

    fun getLocations(): MapState {
        val json = context.assets.open("locations.json").bufferedReader().use { it.readText() }
        locations = jsonMapper.mapJsonToDataClass(jsonString = json, dataClass = Locations::class.java)
        return MapState.SuccessFetchLocations(locations = locations)
    }

    fun showInfoLocation(id: Int): LocationData? {
        return locations.locations.find { it.id == id }
    }

}