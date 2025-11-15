package com.lotusreichhart.domain.repositories

import com.lotusreichhart.domain.entities.BannerEntity
import kotlinx.coroutines.flow.Flow

interface BannerRepository {
    fun getAllBanners(): Flow<List<BannerEntity>>
}