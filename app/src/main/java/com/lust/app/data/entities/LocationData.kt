package com.lust.app.data.entities

import androidx.annotation.Keep
import com.google.firebase.firestore.Exclude

@Keep
data class LocationData(
    val uid: String? = null,
    val name: String = "",
    var description: String = "",
    val categoryId: Int = 0,
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val comments: List<Comment> = listOf(),
    val qualification: Float = 0f,
    val photoFromOutside: String? = null,
    val photoFromInside: String? = null,
    @Exclude
    var originLatitude: Double = 0.0,
    @Exclude
    var originLongitude: Double = 0.0,
    @Exclude
    var distance: Float = 0f,
) {
    val category: PlaceType get() = PlaceType.fromId(categoryId)

    val distanceInKm: String
        get() = "%.2f km".format(distance / 1000)
}