package com.lotusreichhart.data.repository

import com.lotusreichhart.data.local.database.dao.UserDao
import com.lotusreichhart.data.local.datastore.TokenDataStore
import com.lotusreichhart.data.remote.dto.auth.EmailRequest
import com.lotusreichhart.data.remote.dto.auth.EmailSignInRequest
import com.lotusreichhart.data.remote.dto.auth.GoogleSignInRequest
import com.lotusreichhart.data.remote.dto.auth.ResetPasswordRequest
import com.lotusreichhart.data.remote.dto.auth.SignUpRequest
import com.lotusreichhart.data.remote.dto.auth.VerifyOtpRequest
import com.lotusreichhart.data.remote.service.AuthApiService
import com.lotusreichhart.data.utils.safeApiCallData
import com.lotusreichhart.data.utils.safeApiCallUnit
import com.lotusreichhart.domain.repository.AuthRepository
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val authApiService: AuthApiService,
    private val userDao: UserDao,
    private val tokenDataSource: TokenDataStore
) : AuthRepository {

    override suspend fun signInWithEmail(
        email: String,
        password: String
    ): Result<Unit> {
        val result = safeApiCallData {
            authApiService.signInWithEmail(EmailSignInRequest(email = email, password = password))
        }
        result.onSuccess { authResponse ->
            tokenDataSource.saveTokens(
                access = authResponse.accessToken,
                refresh = authResponse.refreshToken
            )
        }
        return result.map { }
    }

    override suspend fun signInWithGoogle(idToken: String): Result<Unit> {
        val result = safeApiCallData {
            authApiService.signInWithGoogle(GoogleSignInRequest(idToken))
        }

        result.onSuccess { authResponse ->
            tokenDataSource.saveTokens(
                access = authResponse.accessToken,
                refresh = authResponse.refreshToken
            )
        }

        return result.map { }
    }

    override suspend fun requestSignUp(
        name: String,
        email: String,
        password: String
    ): Result<Unit> {
        return safeApiCallUnit {
            authApiService.requestSignUp(
                SignUpRequest(
                    name = name,
                    email = email,
                    password = password
                )
            )
        }
    }

    override suspend fun verifySignUp(
        otp: String,
        email: String
    ): Result<Unit> {
        val result = safeApiCallData {
            authApiService.verifySignUp(VerifyOtpRequest(email = email, otp = otp))
        }

        result.onSuccess { authResponse ->
            tokenDataSource.saveTokens(
                access = authResponse.accessToken,
                refresh = authResponse.refreshToken
            )
        }

        return result.map { }
    }

    override suspend fun requestForgotPassword(email: String): Result<Unit> {
        return safeApiCallUnit {
            authApiService.requestForgotPassword(EmailRequest(email))
        }
    }

    override suspend fun verifyForgotPassword(
        otp: String,
        email: String
    ): Result<String> {
        val result = safeApiCallData {
            authApiService.verifyForgotPassword(VerifyOtpRequest(otp = otp, email = email))
        }
        return result.map { response ->
            response.resetToken
        }
    }

    override suspend fun resetPassword(
        resetToken: String,
        newPassword: String,
        confirmPassword: String
    ): Result<Unit> {
        return safeApiCallUnit {
            authApiService.resetPassword(
                ResetPasswordRequest(
                    resetToken = resetToken,
                    newPassword = newPassword,
                    confirmPassword = confirmPassword
                )
            )
        }
    }

    override suspend fun resendOtp(email: String): Result<Unit> {
        return safeApiCallUnit {
            authApiService.resendOtp(EmailRequest(email))
        }
    }

    override suspend fun signOut(): Result<Unit> {
        try {
            authApiService.signOut()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        userDao.deleteUser()
        tokenDataSource.clearTokens()
        return Result.success(Unit)
    }
}