package com.lotusreichhart.data.remote.interceptor

import com.lotusreichhart.data.local.datastore.TokenDataStore
import com.lotusreichhart.data.remote.dto.auth.RefreshTokenRequest
import com.lotusreichhart.data.remote.service.AuthApiService
import dagger.Lazy
import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route

class TokenAuthenticator(
    private val tokenDataStore: TokenDataStore,
    private val authApiService: Lazy<AuthApiService>
) : Authenticator {

    override fun authenticate(route: Route?, response: Response): Request? {
        val currentRefreshToken = tokenDataStore.getRefreshTokenValue()

        if (currentRefreshToken == null) {
            runBlocking { tokenDataStore.clearTokens() }
            return null
        }

        synchronized(this) {
            val newRefreshToken = tokenDataStore.getRefreshTokenValue()

            if (currentRefreshToken != newRefreshToken) {
                val newAccessToken = tokenDataStore.getAccessTokenValue()
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
                        tokenDataStore.saveTokens(
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
                // Refresh token thất bại
                // Đăng xuất người dùng
                runBlocking { tokenDataStore.clearTokens() }
                return null // Hủy request
            }
        }
    }
}