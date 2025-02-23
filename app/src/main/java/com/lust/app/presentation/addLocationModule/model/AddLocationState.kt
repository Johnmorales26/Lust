package com.lust.app.presentation.addLocationModule.model

sealed class AddLocationState {
    data object Init: AddLocationState()
    data object NamePlaceEmpty: AddLocationState()
    data object DescriptionPlaceEmpty: AddLocationState()
    data object SavePlace: AddLocationState()
}