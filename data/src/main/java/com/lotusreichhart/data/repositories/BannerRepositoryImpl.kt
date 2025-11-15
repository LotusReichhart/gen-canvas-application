package com.lotusreichhart.data.repositories

import com.lotusreichhart.data.mapper.toDomain
import com.lotusreichhart.data.remote.services.BannerApiService
import com.lotusreichhart.data.utils.safeApiCallData
import com.lotusreichhart.domain.entities.BannerEntity
import com.lotusreichhart.domain.repositories.BannerRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class BannerRepositoryImpl @Inject constructor(
    private val bannerApiService: BannerApiService
) : BannerRepository {
    override fun getAllBanners(): Flow<List<BannerEntity>> = flow {
        val result = safeApiCallData {
            bannerApiService.getAllBanners()
        }

        result.onSuccess { bannerListResponse ->
            val bannerDtoList = bannerListResponse.banners
            val bannerEntityList = bannerDtoList.toDomain()
            emit(bannerEntityList)

        }.onFailure { exception ->
            throw exception
        }
    }
}