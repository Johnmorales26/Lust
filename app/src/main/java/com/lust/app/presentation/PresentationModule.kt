package com.lust.app.presentation

import com.lust.app.presentation.mapModule.MapRepository
import com.lust.app.presentation.mapModule.MapViewModel
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val PresentationModule = module {
    singleOf(::MapRepository) { bind<MapRepository>() }
    viewModelOf(::MapViewModel)
}