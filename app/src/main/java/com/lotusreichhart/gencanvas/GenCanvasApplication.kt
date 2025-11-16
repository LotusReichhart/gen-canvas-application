package com.lotusreichhart.gencanvas

import android.app.Application
import com.google.android.gms.ads.MobileAds
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class GenCanvasApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        // Khởi tạo AdMob
        MobileAds.initialize(this) {}
    }
}