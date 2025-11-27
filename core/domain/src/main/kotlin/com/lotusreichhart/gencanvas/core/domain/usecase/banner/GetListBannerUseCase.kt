package com.lotusreichhart.gencanvas.core.domain.usecase.banner

import com.lotusreichhart.gencanvas.core.domain.repository.BannerRepository
import com.lotusreichhart.gencanvas.core.domain.repository.SettingsRepository
import com.lotusreichhart.gencanvas.core.model.banner.Banner
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject

private const val CACHE_STALE_TIME_MS = 24 * 60 * 60 * 1000L

class GetListBannerUseCase @Inject constructor(
    private val bannerRepository: BannerRepository,
    private val settingsRepository: SettingsRepository
) {
    operator fun invoke(): Flow<List<Banner>> {
        return bannerRepository.getBannersStream()
            .onStart {
                val lastRefreshTime = settingsRepository.getLastBannerRefreshTime()
                val currentTime = System.currentTimeMillis()

                val isCacheStale = (currentTime - lastRefreshTime > CACHE_STALE_TIME_MS)

                if (isCacheStale) {
                    val result = bannerRepository.fetchBanners()
                    if (result.isSuccess) {
                        settingsRepository.saveLastBannerRefreshTime(currentTime)
                    }
                }
            }
    }
}