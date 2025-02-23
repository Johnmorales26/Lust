package com.lust.app.presentation

import com.lust.app.presentation.addLocationModule.AddLocationRepository
import com.lust.app.presentation.addLocationModule.AddLocationViewModel
import com.lust.app.presentation.mapModule.MapRepository
import com.lust.app.presentation.mapModule.MapViewModel
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val PresentationModule = module {
    singleOf(::AddLocationRepository) { bind<AddLocationRepository>() }
    viewModelOf(::AddLocationViewModel)
    singleOf(::MapRepository) { bind<MapRepository>() }
    viewModelOf(::MapViewModel)
}