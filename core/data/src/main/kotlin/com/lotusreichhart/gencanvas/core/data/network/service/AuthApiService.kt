package com.lotusreichhart.gencanvas.core.data.network.service

import com.lotusreichhart.gencanvas.core.data.network.model.ResponseWrapper
import com.lotusreichhart.gencanvas.core.data.network.model.auth.AuthResponse
import com.lotusreichhart.gencanvas.core.data.network.model.auth.EmailRequest
import com.lotusreichhart.gencanvas.core.data.network.model.auth.EmailSignInRequest
import com.lotusreichhart.gencanvas.core.data.network.model.auth.GoogleSignInRequest
import com.lotusreichhart.gencanvas.core.data.network.model.auth.RefreshTokenRequest
import com.lotusreichhart.gencanvas.core.data.network.model.auth.ResetPasswordRequest
import com.lotusreichhart.gencanvas.core.data.network.model.auth.ResetTokenResponse
import com.lotusreichhart.gencanvas.core.data.network.model.auth.SignUpRequest
import com.lotusreichhart.gencanvas.core.data.network.model.auth.VerifyOtpRequest
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApiService {
    @POST("auth/signin")
    suspend fun signInWithEmail(@Body request: EmailSignInRequest): ResponseWrapper<AuthResponse>

    @POST("auth/google")
    suspend fun signInWithGoogle(@Body request: GoogleSignInRequest): ResponseWrapper<AuthResponse>

    @POST("auth/signup")
    suspend fun requestSignUp(@Body request: SignUpRequest): ResponseWrapper<Unit>

    @POST("auth/signup/verify")
    suspend fun verifySignUp(@Body request: VerifyOtpRequest): ResponseWrapper<AuthResponse>

    @POST("auth/otp/resend")
    suspend fun resendOtp(@Body request: EmailRequest): ResponseWrapper<Unit>

    @POST("auth/password/forgot")
    suspend fun requestForgotPassword(@Body request: EmailRequest): ResponseWrapper<Unit>

    @POST("auth/password/verify")
    suspend fun verifyForgotPassword(@Body request: VerifyOtpRequest): ResponseWrapper<ResetTokenResponse>

    @POST("auth/password/reset")
    suspend fun resetPassword(@Body request: ResetPasswordRequest): ResponseWrapper<Unit>

    @POST("auth/refresh")
    fun refreshSignIn(@Body request: RefreshTokenRequest): Call<ResponseWrapper<AuthResponse>>
}