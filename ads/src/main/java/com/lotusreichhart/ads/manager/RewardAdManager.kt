package com.lotusreichhart.ads.manager

import android.app.Activity
import android.content.Context
import android.util.Log
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback
import com.google.android.gms.ads.rewarded.ServerSideVerificationOptions
import com.lotusreichhart.domain.usecase.user.FetchProfileUseCase
import com.lotusreichhart.domain.usecase.user.GetProfileStreamUseCase
import dagger.hilt.android.qualifiers.ActivityContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

import com.lotusreichhart.ads.BuildConfig
import com.lotusreichhart.core.utils.logD

/**
 * Quản lý việc tải và hiển thị Quảng cáo có Thưởng (Rewarded Ads)
 * sử dụng Server-Side Verification (SSV).
 */
class RewardAdManager @Inject constructor(
    @param:ActivityContext private val context: Context,
    private val getProfileStreamUseCase: GetProfileStreamUseCase,
    private val fetchProfileUseCase: FetchProfileUseCase,
    private val adRequest: AdRequest
) {
    private var rewardedAd: RewardedAd? = null
    private val adUnitId = BuildConfig.REWARDED_AD_UNIT_ID

    fun loadAd() {
        CoroutineScope(Dispatchers.IO).launch {
            val userId = getProfileStreamUseCase().first()?.id?.toString()

            if (userId == null) {
                Log.e("RewardAdManager", "Không thể tải quảng cáo: User chưa đăng nhập.")
                return@launch
            }

            logD("Đang tải quảng cáo cho UserID: $userId")

            val options = ServerSideVerificationOptions.Builder()
                .setUserId(userId)
                .build()

            withContext(Dispatchers.Main) {
                RewardedAd.load(
                    context,
                    adUnitId,
                    adRequest,
                    object : RewardedAdLoadCallback() {
                        override fun onAdLoaded(ad: RewardedAd) {
                            logD("Quảng cáo đã tải thành công.")
                            ad.setServerSideVerificationOptions(options)
                            rewardedAd = ad
                        }

                        override fun onAdFailedToLoad(error: LoadAdError) {
                            Log.e("RewardAdManager", "Tải quảng cáo thất bại: ${error.message}")
                            rewardedAd = null
                        }
                    }
                )
            }
        }
    }

    fun showAd(onRewardEarned: (success: Boolean) -> Unit) {
        val activity = context as? Activity
        if (rewardedAd == null || activity == null) {
            Log.w("RewardAdManager", "Quảng cáo chưa sẵn sàng để hiển thị.")
            onRewardEarned(false)
            return
        }

        rewardedAd?.show(activity) { _ ->
            logD("Người dùng đã xem xong (onUserEarnedReward).")

            CoroutineScope(Dispatchers.IO).launch {
                logD("Đang gọi fetchProfile() để cập nhật số dư...")
                val result = fetchProfileUseCase()

                withContext(Dispatchers.Main) {
                    onRewardEarned(result.isSuccess)
                }
            }
        }
        rewardedAd = null
    }
}