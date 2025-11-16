package com.lotusreichhart.domain.use_cases.banner

import com.lotusreichhart.domain.entities.BannerEntity
import com.lotusreichhart.domain.repositories.BannerRepository
import kotlinx.coroutines.flow.Flow

class GetListBannerUseCase(
    private val bannerRepository: BannerRepository
) {
    operator fun invoke(): Flow<List<BannerEntity>> {
        return bannerRepository.getAllBanners()
    }
}