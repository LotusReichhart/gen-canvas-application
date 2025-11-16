package com.lotusreichhart.domain.repository

import com.lotusreichhart.domain.entity.BannerEntity
import kotlinx.coroutines.flow.Flow

interface BannerRepository {
    fun getAllBanners(): Flow<List<BannerEntity>>
}