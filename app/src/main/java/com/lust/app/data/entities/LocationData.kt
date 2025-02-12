package com.lust.app.data.entities

import com.google.gson.annotations.SerializedName

data class LocationData(
    @SerializedName("category") val categoryId: Int,
    val id: Int,
    val latitude: Double,
    val longitude: Double,
    val name: String,
    var originLatitude: Double = 0.0,
    var originLongitude: Double = 0.0,
    var description: String = "",
    var distance: Float,
) {
    val category: PlaceType get() = PlaceType.fromId(categoryId)

    val distanceInKm: String
        get() = "%.2f km".format(distance / 1000)
}