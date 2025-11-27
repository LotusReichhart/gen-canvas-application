package com.lotusreichhart.gencanvas.core.domain.usecase.banner

import com.lotusreichhart.gencanvas.core.domain.repository.BannerRepository
import com.lotusreichhart.gencanvas.core.domain.repository.SettingsRepository
import javax.inject.Inject

class RefreshBannersUseCase @Inject constructor(
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