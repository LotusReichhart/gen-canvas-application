package com.lotusreichhart.data.remote.service

import com.lotusreichhart.data.remote.dto.ResponseWrapper
import com.lotusreichhart.data.remote.dto.legal.LegalInfoDTO
import retrofit2.http.GET

interface LegalApiService {
    @GET("legal/info")
    suspend fun getLegalInfo(): ResponseWrapper<LegalInfoDTO>
}