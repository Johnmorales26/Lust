package com.lust.app

import android.app.Application
import com.lust.app.data.DataModule
import com.lust.app.presentation.PresentationModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class LustApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        initKoin()
    }

    private fun initKoin() {
        startKoin {
            androidLogger()
            androidContext(this@LustApplication)
            modules(listOf(PresentationModule, DataModule))
        }
    }

}