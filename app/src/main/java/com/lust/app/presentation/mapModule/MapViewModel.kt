package com.lust.app.presentation.mapModule

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch

class MapViewModel(
    private val context: Context,
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

    private fun showInfoLocation(id: Int) {
        try {
            repository.showInfoLocation(id = id) { location ->
                if (location != null) {
                    _state.value = MapState.ShowInfoLocation(location)
                } else {
                    _state.value = MapState.Error(msg = "")
                }
            }
        } catch (e: Exception) {
            _state.value = MapState.Error(msg = "")
        }
    }

    private fun getLocations() {
        try {
            _state.value = repository.getLocations()
        } catch (e: Exception) {
            _state.value = MapState.Error(msg = "")
        }
    }

    private fun getCurrentLocation() {
        try {
            repository.getCurrentLocation { location ->
                _state.value = MapState.SuccessLocation(location = location)
            }
        } catch (e: Exception) {
            _state.value =
                MapState.Error(msg = "Unable to retrieve your location. Try again later.")
        }
    }

    private fun filterPlaces(id: Int) {
        _state.value = repository.filterPlaces(id)
    }

}