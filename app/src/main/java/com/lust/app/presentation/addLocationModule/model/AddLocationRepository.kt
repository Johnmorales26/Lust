package com.lust.app.presentation.addLocationModule

import com.lust.app.data.database.Database
import com.lust.app.data.entities.LocationData

class AddLocationRepository(private val database: Database) {

    fun registerPlace(
        categoryId: Int, namePlace: String, descriptionPlace: String,
        latitude: Double,
        longitude: Double
    ): AddLocationState {
        if (namePlace.isBlank()) {
            return AddLocationState.NamePlaceEmpty
        }

        if (descriptionPlace.isBlank()) {
            return AddLocationState.DescriptionPlaceEmpty
        }

        database.addNewLocation(LocationData(
            categoryId = categoryId,
            name = namePlace,
            description = descriptionPlace,
            latitude = latitude,
            longitude = longitude
        ))

        return AddLocationState.SavePlace
    }

}