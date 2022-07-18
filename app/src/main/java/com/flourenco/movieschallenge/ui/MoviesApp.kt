package com.flourenco.movieschallenge.ui

import android.app.Application
import com.flourenco.movieschallenge.BuildConfig
import com.flourenco.movieschallenge.core.injection.coreModule
import com.flourenco.movieschallenge.core.injection.networkModule
import com.flourenco.movieschallenge.core.injection.storageModule
import com.flourenco.movieschallenge.ui.injection.uiModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import timber.log.Timber

class MoviesApp: Application() {

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

        startKoin {
            androidLogger(Level.ERROR)
            androidContext(this@MoviesApp)
            modules(
                networkModule,
                storageModule,
                coreModule,
                uiModule
            )
        }
    }
}