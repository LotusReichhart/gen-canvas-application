package com.lotusreichhart.gencanvas.core.data.network.service

import com.lotusreichhart.gencanvas.core.data.network.model.ResponseWrapper
import com.lotusreichhart.gencanvas.core.data.network.model.user.UserProfileResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.PATCH
import retrofit2.http.Part

interface UserApiService {
    @GET("users/me")
    suspend fun getProfile(): ResponseWrapper<UserProfileResponse>

    @Multipart
    @PATCH("users/me")
    suspend fun updateProfile(
        @Part("name") name: RequestBody?,
        @Part avatar: MultipartBody.Part?
    ): ResponseWrapper<UserProfileResponse>

    @GET("auth/sign-out")
    suspend fun signOut(): ResponseWrapper<Unit>
}