package com.lust.app.data.entities

import com.google.gson.annotations.SerializedName

data class LocationData(
    @SerializedName("category") val categoryId: Int,
    val id: Int,
    val latitude: Double,
    val longitude: Double,
    val name: String
) {
    val category: LocationCategory get() = LocationCategory.fromId(categoryId)
}