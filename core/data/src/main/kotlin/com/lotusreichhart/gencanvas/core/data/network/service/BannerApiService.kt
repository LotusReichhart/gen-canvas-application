package com.lotusreichhart.gencanvas.core.data.network.service

import com.lotusreichhart.gencanvas.core.data.network.model.ResponseWrapper
import com.lotusreichhart.gencanvas.core.data.network.model.banner.BannerListResponse
import retrofit2.http.GET

interface BannerApiService {
    @GET("banners")
    suspend fun getAllBanners(): ResponseWrapper<BannerListResponse>
}