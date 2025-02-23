package com.lust.app.presentation.addLocationModule

sealed class AddLocationState {
    data object Init: AddLocationState()
    data object NamePlaceEmpty: AddLocationState()
    data object DescriptionPlaceEmpty: AddLocationState()
    data object SavePlace: AddLocationState()
}