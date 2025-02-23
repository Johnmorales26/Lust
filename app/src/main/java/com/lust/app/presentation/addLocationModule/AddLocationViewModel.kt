package com.lust.app.presentation.addLocationModule

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lust.app.presentation.addLocationModule.intent.AddLocationIntent
import com.lust.app.presentation.addLocationModule.model.AddLocationRepository
import com.lust.app.presentation.addLocationModule.model.AddLocationState
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch

class AddLocationViewModel(private val repository: AddLocationRepository) : ViewModel() {
    private val _state = MutableStateFlow<AddLocationState>(AddLocationState.Init)
    val state: StateFlow<AddLocationState> = _state

    private val channel = Channel<AddLocationIntent>(Channel.UNLIMITED)

    init {
        setupIntent()
    }

    private fun setupIntent() {
        viewModelScope.launch {
            channel.consumeAsFlow()
                .collect { i ->
                    when (i) {
                        is AddLocationIntent.RegisterPlace -> registerPlace(
                            categoryId = i.categoryId,
                            namePlace = i.namePlace,
                            descriptionPlace = i.descriptionPlace,
                            latitude = i.latitude,
                            longitude = i.longitude
                        )
                    }
                }
        }
    }

    fun launchIntent(intent: AddLocationIntent) {
        viewModelScope.launch { channel.send(intent) }
    }

    private fun registerPlace(
        categoryId: Int, namePlace: String, descriptionPlace: String,
        latitude: Double,
        longitude: Double
    ) {
        _state.value =
            repository.registerPlace(categoryId, namePlace, descriptionPlace, latitude, longitude)
    }
}