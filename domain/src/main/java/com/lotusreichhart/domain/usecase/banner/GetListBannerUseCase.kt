package com.lotusreichhart.domain.usecase.banner

import com.lotusreichhart.domain.entity.BannerEntity
import com.lotusreichhart.domain.repository.BannerRepository
import com.lotusreichhart.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onStart

private const val CACHE_STALE_TIME_MS = 24 * 60 * 60 * 1000L

class GetListBannerUseCase(
    private val bannerRepository: BannerRepository,
    private val settingsRepository: SettingsRepository
) {
    operator fun invoke(): Flow<List<BannerEntity>> {
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