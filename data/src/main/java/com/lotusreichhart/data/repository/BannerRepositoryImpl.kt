package com.lotusreichhart.data.repository

import com.lotusreichhart.core.utils.logD
import com.lotusreichhart.data.local.database.dao.BannerDao
import com.lotusreichhart.data.mapper.toDomain
import com.lotusreichhart.data.mapper.toLocal
import com.lotusreichhart.data.remote.service.BannerApiService
import com.lotusreichhart.data.utils.safeApiCallData
import com.lotusreichhart.domain.entity.BannerEntity
import com.lotusreichhart.domain.repository.BannerRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class BannerRepositoryImpl @Inject constructor(
    private val bannerApiService: BannerApiService,
    private val bannerDao: BannerDao
) : BannerRepository {
    override fun getBannersStream(): Flow<List<BannerEntity>> {
        return bannerDao.getBannersFlow()
            .map { bannerLocalList ->
                bannerLocalList.toDomain()
            }
    }

    override suspend fun fetchBanners(): Result<Unit> {
        val result = safeApiCallData {
            bannerApiService.getAllBanners()
        }

        result.onSuccess { bannerListResponse ->
            logD("bannerListResponse $bannerListResponse")

            val bannerDtoList = bannerListResponse.banners

            val newBannersLocal = bannerDtoList.toLocal()
            val currentBannersLocal = bannerDao.getBanners()

            if (newBannersLocal != currentBannersLocal) {
                logD("Banner data thay đổi (hoặc cache rỗng) -> Cập nhật RoomDB")
                bannerDao.replaceBanners(newBannersLocal)
            } else {
                logD("Banner data KHÔNG đổi -> Bỏ qua ghi RoomDB")
            }
        }
        return result.map { }
    }
}