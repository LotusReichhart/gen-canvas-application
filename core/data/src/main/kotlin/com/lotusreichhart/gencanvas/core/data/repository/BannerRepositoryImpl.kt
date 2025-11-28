package com.lotusreichhart.gencanvas.core.data.repository

import com.lotusreichhart.gencanvas.core.data.database.dao.BannerDao
import com.lotusreichhart.gencanvas.core.data.mapper.toEntity
import com.lotusreichhart.gencanvas.core.data.mapper.toModel
import com.lotusreichhart.gencanvas.core.data.network.service.BannerApiService
import com.lotusreichhart.gencanvas.core.data.network.util.safeApiCallData
import com.lotusreichhart.gencanvas.core.domain.repository.BannerRepository
import com.lotusreichhart.gencanvas.core.model.banner.Banner
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import timber.log.Timber
import javax.inject.Inject

class BannerRepositoryImpl@Inject constructor(
    private val apiService: BannerApiService,
    private val dao: BannerDao
) : BannerRepository {
    override fun getBannersStream(): Flow<List<Banner>> {
        return dao.getBannersFlow()
            .map { bannerEntityList ->
                bannerEntityList.toModel()
            }
    }

    override suspend fun fetchBanners(): Result<Unit> {
        val result = safeApiCallData {
            apiService.getAllBanners()
        }

        result.onSuccess { bannerListResponse ->
            Timber.d("bannerListResponse $bannerListResponse")


            val bannerDtoList = bannerListResponse.banners

            val newBannersLocal = bannerDtoList.toEntity()
            val currentBannersLocal = dao.getBanners()

            if (newBannersLocal != currentBannersLocal) {
                Timber.d("Banner data thay đổi (hoặc cache rỗng) -> Cập nhật RoomDB")
                dao.replaceBanners(newBannersLocal)
            } else {
                Timber.d("Banner data KHÔNG đổi -> Bỏ qua ghi RoomDB")
            }
        }
        return result.map { }
    }
}