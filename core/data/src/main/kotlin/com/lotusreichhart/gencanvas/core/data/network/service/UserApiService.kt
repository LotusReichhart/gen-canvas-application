package com.lotusreichhart.gencanvas.core.data.network.service

import com.lotusreichhart.gencanvas.core.data.network.model.ResponseWrapper
import com.lotusreichhart.gencanvas.core.data.network.model.user.UserProfileResponse
import retrofit2.http.GET

interface UserApiService {
    @GET("users")
    suspend fun getProfile(): ResponseWrapper<UserProfileResponse>

    @GET("auth/sign-out")
    suspend fun signOut(): ResponseWrapper<Unit>
}