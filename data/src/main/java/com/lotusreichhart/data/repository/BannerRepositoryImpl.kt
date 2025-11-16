package com.lotusreichhart.data.repository

import com.lotusreichhart.data.mapper.toDomain
import com.lotusreichhart.data.remote.service.BannerApiService
import com.lotusreichhart.data.utils.safeApiCallData
import com.lotusreichhart.domain.entity.BannerEntity
import com.lotusreichhart.domain.repository.BannerRepository
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