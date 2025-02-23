package com.lust.app.presentation.mapModule

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lust.app.presentation.mapModule.intent.MapIntent
import com.lust.app.presentation.mapModule.model.MapRepository
import com.lust.app.presentation.mapModule.model.MapState
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch

class MapViewModel(
    private val repository: MapRepository
) : ViewModel() {

    private val _state = MutableStateFlow<MapState>(MapState.Init)
    val state: StateFlow<MapState> = _state

    private val channel = Channel<MapIntent>(Channel.UNLIMITED)

    init {
        setupIntent()
    }

    private fun setupIntent() {
        viewModelScope.launch {
            channel.consumeAsFlow()
                .collect { i ->
                    when (i) {
                        is MapIntent.GetLocations -> getLocations()
                        is MapIntent.GetCurrentLocation -> getCurrentLocation()
                        is MapIntent.HideLocationData -> _state.value = MapState.HideLocationData
                        is MapIntent.ShowInfoLocation -> showInfoLocation(i.id)
                        is MapIntent.FilterPlaces -> filterPlaces(i.id)
                    }
                }
        }
    }

    fun launchIntent(intent: MapIntent) {
        viewModelScope.launch { channel.send(intent) }
    }

    private fun showInfoLocation(id: String) {
        try {
            if (id.isBlank()) {
                _state.value = MapState.HideLocationData
                return
            }

            repository.showInfoLocation(id = id) { location ->
                if (location != null) {
                    _state.value = MapState.ShowInfoLocation(location)
                } else {
                    _state.value = MapState.Error(msg = "Location information not found.")
                }
            }
        } catch (e: Exception) {
            _state.value = MapState.Error(msg = "Failed to show location information: ${e.message}")
        }
    }

    private fun getLocations() {
        try {
            repository.getLocations { _state.value = it }
        } catch (e: Exception) {
            _state.value = MapState.Error(msg = "Failed to retrieve locations: ${e.message}")
        }
    }

    private fun getCurrentLocation() {
        try {
            repository.getCurrentLocation { location ->
                _state.value = MapState.SuccessLocation(location = location)
            }
        } catch (e: Exception) {
            _state.value =
                MapState.Error(msg = "Unable to retrieve your location. Try again later. Error: ${e.message}")
        }
    }

    private fun filterPlaces(id: Int) {
        try {
            _state.value = repository.filterPlaces(id)
        } catch (e: Exception) {
            _state.value = MapState.Error(msg = "Failed to filter places: ${e.message}")
        }
    }
}