package com.lust.app.data.entities

import androidx.annotation.Keep
import com.google.firebase.firestore.Exclude

@Keep
data class LocationData(
    val categoryId: Int,
    val uid: String?,
    val latitude: Double,
    val longitude: Double,
    val name: String,
    val comments: List<Comment>,
    val qualification: Float,
    val photoFromOutside: String?,
    val photoFromInside: String?,
    var description: String = "",
    @Exclude
    var originLatitude: Double = 0.0,
    @Exclude
    var originLongitude: Double = 0.0,
    @Exclude
    var distance: Float,
) {
    constructor() : this(0, null, 0.0, 0.0, "", listOf(), 0.0f, null, null, "", 0.0, 0.0, 0f)

    val category: PlaceType get() = PlaceType.fromId(categoryId)

    val distanceInKm: String
        get() = "%.2f km".format(distance / 1000)
}