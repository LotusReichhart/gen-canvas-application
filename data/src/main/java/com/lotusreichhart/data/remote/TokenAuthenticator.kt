package com.lotusreichhart.data.remote

import com.lotusreichhart.data.local.TokenLocalDataSource
import com.lotusreichhart.data.remote.dto.auth.RefreshTokenRequest
import com.lotusreichhart.data.remote.services.AuthApiService
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import dagger.Lazy

class TokenAuthenticator(
    private val tokenDataSource: TokenLocalDataSource,
    private val authApiService: Lazy<AuthApiService>
) : Authenticator {

    override fun authenticate(route: Route?, response: Response): Request? {
        val currentRefreshToken = runBlocking { tokenDataSource.refreshToken.firstOrNull() }

        if (currentRefreshToken == null) {
            runBlocking { tokenDataSource.clearTokens() }
            return null
        }

        synchronized(this) {
            val newRefreshToken = runBlocking { tokenDataSource.refreshToken.firstOrNull() }

            if (currentRefreshToken != newRefreshToken) {
                val newAccessToken = runBlocking { tokenDataSource.accessToken.firstOrNull() }
                if (newAccessToken != null) {
                    return response.request.newBuilder()
                        .header("Authorization", "Bearer $newAccessToken")
                        .build()
                }
            }

            val refreshTokenResponse = authApiService.get()
                .refreshSignIn(RefreshTokenRequest(currentRefreshToken))
                .execute()

            if (refreshTokenResponse.isSuccessful && refreshTokenResponse.body() != null) {
                val newTokens = refreshTokenResponse.body()!!
                // Lưu token mới
                runBlocking {
                    newTokens.data?.let {
                        tokenDataSource.saveTokens(
                            access = it.accessToken,
                            refresh = it.refreshToken
                        )
                    }
                }
                // Trả về request mới với token mới
                return response.request.newBuilder()
                    .header("Authorization", "Bearer ${newTokens.data?.accessToken}")
                    .build()
            } else {
                // Refresh token thất bại (ví dụ: refresh token hết hạn)
                // Đăng xuất người dùng
                runBlocking { tokenDataSource.clearTokens() }
                return null // Hủy request
            }
        }
    }
}