package com.lotusreichhart.data.remote.services

import com.lotusreichhart.data.remote.dto.ResponseWrapper
import com.lotusreichhart.data.remote.dto.auth.AuthResponse
import com.lotusreichhart.data.remote.dto.auth.EmailRequest
import com.lotusreichhart.data.remote.dto.auth.EmailSignInRequest
import com.lotusreichhart.data.remote.dto.auth.GoogleSignInRequest
import com.lotusreichhart.data.remote.dto.auth.RefreshTokenRequest
import com.lotusreichhart.data.remote.dto.auth.ResetPasswordRequest
import com.lotusreichhart.data.remote.dto.auth.SignUpRequest
import com.lotusreichhart.data.remote.dto.auth.VerifyOtpRequest
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface AuthApiService {
    @POST("auth/signin")
    suspend fun signInWithEmail(@Body request: EmailSignInRequest): ResponseWrapper<AuthResponse>

    @POST("auth/google")
    suspend fun signInWithGoogle(@Body request: GoogleSignInRequest): ResponseWrapper<AuthResponse>

    @POST("auth/signup")
    suspend fun requestSignUp(@Body request: SignUpRequest): ResponseWrapper<Unit>

    @POST("auth/verify")
    suspend fun verifySignUp(@Body request: VerifyOtpRequest): ResponseWrapper<AuthResponse>

    @POST("auth/otp/resend")
    suspend fun resendOtp(@Body request: EmailRequest): ResponseWrapper<Unit>

    @POST("auth/password/forgot")
    suspend fun requestForgotPassword(@Body request: EmailRequest): ResponseWrapper<Unit>

    @POST("auth/password/verify")
    suspend fun verifyForgotPassword(@Body request: VerifyOtpRequest): ResponseWrapper<Unit>

    @POST("auth/password/reset")
    suspend fun resetPassword(@Body request: ResetPasswordRequest): ResponseWrapper<Unit>

    @GET("auth/sign-out")
    suspend fun signOut(): ResponseWrapper<Unit>

    @POST("auth/refresh")
    fun refreshSignIn(@Body request: RefreshTokenRequest): Call<ResponseWrapper<AuthResponse>>
}