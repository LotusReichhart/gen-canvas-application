package com.lotusreichhart.data.remote.service

import com.lotusreichhart.data.remote.dto.ResponseWrapper
import com.lotusreichhart.data.remote.dto.banner.BannerListResponse
import retrofit2.http.GET

interface BannerApiService {
    @GET("banners")
    suspend fun getAllBanners(): ResponseWrapper<BannerListResponse>
}