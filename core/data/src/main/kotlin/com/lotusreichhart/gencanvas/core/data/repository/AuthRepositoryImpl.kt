package com.lotusreichhart.gencanvas.core.data.repository

import com.lotusreichhart.gencanvas.core.data.database.dao.UserDao
import com.lotusreichhart.gencanvas.core.data.datastore.TokenDataStore
import com.lotusreichhart.gencanvas.core.data.network.model.auth.EmailRequest
import com.lotusreichhart.gencanvas.core.data.network.model.auth.EmailSignInRequest
import com.lotusreichhart.gencanvas.core.data.network.model.auth.GoogleSignInRequest
import com.lotusreichhart.gencanvas.core.data.network.model.auth.ResetPasswordRequest
import com.lotusreichhart.gencanvas.core.data.network.model.auth.SignUpRequest
import com.lotusreichhart.gencanvas.core.data.network.model.auth.VerifyOtpRequest
import com.lotusreichhart.gencanvas.core.data.network.service.AuthApiService
import com.lotusreichhart.gencanvas.core.data.network.service.UserApiService
import com.lotusreichhart.gencanvas.core.data.network.util.safeApiCallData
import com.lotusreichhart.gencanvas.core.data.network.util.safeApiCallUnit
import com.lotusreichhart.gencanvas.core.domain.repository.AuthRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val authApiService: AuthApiService,
    private val userApiService: UserApiService,
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
            Timber.d("signInWithEmail authResponse: $authResponse")
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
            Timber.d("signInWithGoogle authResponse: $authResponse")

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
            Timber.d("verifySignUp authResponse: $authResponse")

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
        CoroutineScope(Dispatchers.IO).launch {
            try {
                userApiService.signOut()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        try {
            userDao.deleteUser()
            tokenDataSource.clearTokens()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return Result.success(Unit)
    }
}