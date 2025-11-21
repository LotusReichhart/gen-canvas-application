package com.lotusreichhart.domain.usecase.banner

import com.lotusreichhart.domain.repository.BannerRepository
import com.lotusreichhart.domain.repository.SettingsRepository

class RefreshBannersUseCase(
    private val bannerRepository: BannerRepository,
    private val settingsRepository: SettingsRepository
) {
    suspend operator fun invoke(): Result<Unit> {
        return try {
            val fetchResult = bannerRepository.fetchBanners()

            if (fetchResult.isSuccess) {
                settingsRepository.saveLastBannerRefreshTime(System.currentTimeMillis())
                Result.success(Unit)
            } else {
                fetchResult
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}