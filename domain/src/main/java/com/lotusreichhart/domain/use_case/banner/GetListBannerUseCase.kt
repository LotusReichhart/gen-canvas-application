package com.lotusreichhart.domain.use_case.banner

import com.lotusreichhart.domain.entity.BannerEntity
import com.lotusreichhart.domain.repository.BannerRepository
import kotlinx.coroutines.flow.Flow

class GetListBannerUseCase(
    private val bannerRepository: BannerRepository
) {
    operator fun invoke(): Flow<List<BannerEntity>> {
        return bannerRepository.getAllBanners()
    }
}