package com.lust.app.presentation.addLocationModule.intent

sealed class AddLocationIntent {
    data class RegisterPlace(
        val categoryId: Int,
        val namePlace: String,
        val descriptionPlace: String,
        val latitude: Double,
        val longitude: Double
    ): AddLocationIntent()
}