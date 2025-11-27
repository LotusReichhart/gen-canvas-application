package com.lotusreichhart.gencanvas.core.domain.usecase.banner

import com.lotusreichhart.gencanvas.core.domain.repository.BannerRepository
import com.lotusreichhart.gencanvas.core.model.banner.Banner
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCachedBannersUseCase @Inject constructor(
    private val bannerRepository: BannerRepository
) {
    operator fun invoke(): Flow<List<Banner>> {
        return bannerRepository.getBannersStream()
    }
}