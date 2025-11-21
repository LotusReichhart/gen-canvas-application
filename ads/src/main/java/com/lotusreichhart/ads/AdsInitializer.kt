package com.lotusreichhart.ads

import android.content.Context
import android.util.Log
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.RequestConfiguration
import com.lotusreichhart.core.utils.logD
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AdsInitializer @Inject constructor(
    @param:ApplicationContext private val context: Context
) {
    fun initialize() {
        CoroutineScope(Dispatchers.IO).launch {
            logD("Bắt đầu khởi tạo AdMob SDK...")

            if (BuildConfig.DEBUG) {
                logD("Đang ở chế độ DEBUG. Cấu hình thiết bị test.")

                val requestConfiguration = RequestConfiguration.Builder()
                    .setTestDeviceIds(
                        listOf(
                            AdRequest.DEVICE_ID_EMULATOR,
                        )
                    )
                    .build()
                MobileAds.setRequestConfiguration(requestConfiguration)
            } else {
                Log.d("AdsInitializer", "Đang ở chế độ PRODUCTION." +
                        " Không cấu hình thiết bị test.")
            }

            MobileAds.initialize(context) { initializationStatus ->

                val newTestDeviceIds = MobileAds.getRequestConfiguration().testDeviceIds
                Log.d(
                    "AdsInitializer",
                    "newTestDeviceIds ID thiết bị đã cấu hình: $newTestDeviceIds"
                )

                val statusMap = initializationStatus.adapterStatusMap
                for ((adapterClass, status) in statusMap) {
                    Log.d(
                        "AdsInitializer",
                        "Adapter: $adapterClass, Trạng thái: ${status.initializationState}" +
                                " - ${status.description}"
                    )
                }
                logD("Khởi tạo AdMob SDK hoàn tất.")
            }
        }
    }
}