package com.lust.app.presentation.mapModule.model

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.util.Log
import com.google.android.gms.location.FusedLocationProviderClient
import com.lust.app.data.database.Database
import com.lust.app.data.entities.LocationData
import com.lust.app.data.entities.Locations
import com.lust.app.data.utils.JsonMapper

class MapRepository(
    private val context: Context,
    private val db: Database,
    private val jsonMapper: JsonMapper,
    private val fusedLocationClient: FusedLocationProviderClient
) {

    private val TAG = MapRepository::class.simpleName
    private lateinit var locations: Locations

    @SuppressLint("MissingPermission")
    fun getCurrentLocation(onSuccess: (Location) -> Unit) {
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                if (location != null) {
                    location.latitude = 19.535792461607024
                    location.longitude = -99.02406680047393

                    val latitude = location.latitude
                    val longitude = location.longitude
                    Log.d(MapRepository::class.simpleName, "Lat: $latitude, Lon: $longitude")
                    onSuccess(location)
                }
            }
            .addOnFailureListener { exception ->
                Log.e(
                    MapRepository::class.simpleName,
                    "Error al obtener la ubicación: ${exception.message}"
                )
            }
    }

    fun getLocations(onResponse: (MapState) -> Unit) {
        if (::locations.isInitialized && locations.locations.isNotEmpty()) {
            onResponse(MapState.SuccessFetchLocations(locations = locations))
            return
        }

        db.fetchLocations { fetchedLocations ->
            locations = fetchedLocations
            onResponse(MapState.SuccessFetchLocations(locations = fetchedLocations))
        }
    }

    fun showInfoLocation(id: String, onResponse: (LocationData?) -> Unit) {
        val location = locations.locations.find { it.uid == id }

        if (location == null) {
            onResponse(null)
            return
        }

        getCurrentLocation { currentLocation ->
            val loc1 = Location("").apply {
                latitude = location.latitude
                longitude = location.longitude
            }
            location.apply {
                originLatitude = currentLocation.latitude
                originLongitude = currentLocation.longitude
                distance = loc1.distanceTo(currentLocation)
            }
            onResponse(location)
        }
    }

    fun filterPlaces(id: Int): MapState {
        val filterLocations = Locations(locations.locations.filter { it.categoryId == id })
        return MapState.SuccessFetchLocations(filterLocations)
    }

}