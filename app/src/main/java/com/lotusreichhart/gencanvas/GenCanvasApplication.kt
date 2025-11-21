package com.lotusreichhart.gencanvas

import android.app.Application
import com.lotusreichhart.ads.AdsInitializer
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber
import javax.inject.Inject

@HiltAndroidApp
class GenCanvasApplication : Application() {

    @Inject
    lateinit var adsInitializer: AdsInitializer

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

        adsInitializer.initialize()
    }
}