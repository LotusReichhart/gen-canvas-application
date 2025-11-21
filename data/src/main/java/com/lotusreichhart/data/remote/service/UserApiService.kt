package com.lotusreichhart.data.remote.service

import com.lotusreichhart.data.remote.dto.ResponseWrapper
import com.lotusreichhart.data.remote.dto.user.UserProfileResponse
import retrofit2.http.GET

interface UserApiService {

    @GET("users")
    suspend fun getProfile(): ResponseWrapper<UserProfileResponse>
}