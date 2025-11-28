package com.lotusreichhart.gencanvas.core.data.network.service

import com.lotusreichhart.gencanvas.core.data.network.model.ResponseWrapper
import com.lotusreichhart.gencanvas.core.data.network.model.legalinfo.LegalInfoDto
import retrofit2.http.GET

interface LegalInfoApiService {
    @GET("legal/info")
    suspend fun getLegalInfo(): ResponseWrapper<LegalInfoDto>
}