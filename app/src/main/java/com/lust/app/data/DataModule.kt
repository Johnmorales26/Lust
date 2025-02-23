package com.lust.app.data

import android.content.Context
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.firebase.firestore.FirebaseFirestore
import com.google.gson.Gson
import com.lust.app.data.database.Database
import com.lust.app.data.utils.JsonMapper
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val DataModule = module {
    singleOf(::Gson) { bind<Gson>() }
    singleOf(::JsonMapper) { bind<JsonMapper>() }
    singleOf(::Database) { bind<Database>() }
    single { FirebaseFirestore.getInstance() }
    single<FusedLocationProviderClient> {
        LocationServices.getFusedLocationProviderClient(get<Context>())
    }
}