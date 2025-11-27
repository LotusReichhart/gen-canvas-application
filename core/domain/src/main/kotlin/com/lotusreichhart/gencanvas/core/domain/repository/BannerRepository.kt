package com.lotusreichhart.gencanvas.core.domain.repository

import com.lotusreichhart.gencanvas.core.model.banner.Banner
import kotlinx.coroutines.flow.Flow

interface BannerRepository {
    fun getBannersStream(): Flow<List<Banner>>
    suspend fun fetchBanners(): Result<Unit>
}