package com.lotusreichhart.domain.usecase.banner

import com.lotusreichhart.domain.entity.BannerEntity
import com.lotusreichhart.domain.repository.BannerRepository
import kotlinx.coroutines.flow.Flow

class GetCachedBannersUseCase(
    private val bannerRepository: BannerRepository
) {
    operator fun invoke(): Flow<List<BannerEntity>> {
        return bannerRepository.getBannersStream()
    }
}