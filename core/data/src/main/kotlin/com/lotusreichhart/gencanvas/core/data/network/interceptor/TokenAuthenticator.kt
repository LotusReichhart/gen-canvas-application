package com.lotusreichhart.gencanvas.core.data.network.interceptor

import com.lotusreichhart.gencanvas.core.common.event.GlobalUiEventManager
import com.lotusreichhart.gencanvas.core.common.event.UiEvent
import com.lotusreichhart.gencanvas.core.data.database.dao.UserDao
import com.lotusreichhart.gencanvas.core.data.datastore.TokenDataStore
import com.lotusreichhart.gencanvas.core.data.network.model.auth.RefreshTokenRequest
import com.lotusreichhart.gencanvas.core.data.network.service.AuthApiService
import dagger.Lazy
import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route

class TokenAuthenticator(
    private val tokenDataStore: TokenDataStore,
    private val userDao: UserDao,
    private val globalUiEventManager: GlobalUiEventManager,
    private val authApiService: Lazy<AuthApiService>
) : Authenticator {

    override fun authenticate(route: Route?, response: Response): Request? {
        val currentRefreshToken = tokenDataStore.getRefreshTokenValue()

        if (currentRefreshToken == null) {
            performLogout(showNotification = false)
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
                runBlocking {
                    newTokens.data?.let {
                        tokenDataStore.updateAccessToken(
                            access = it.accessToken
                        )
                    }
                }
                return response.request.newBuilder()
                    .header("Authorization", "Bearer ${newTokens.data?.accessToken}")
                    .build()
            } else {
                // Refresh token thất bại
                // Đăng xuất người dùng
                performLogout(showNotification = true)
                return null // Hủy request
            }
        }
    }

    private fun performLogout(showNotification: Boolean) {
        runBlocking {
            tokenDataStore.clearTokens()
            userDao.deleteUser()

            if (showNotification) {
                globalUiEventManager.showSnackBar(
                    message = "Phiên đăng nhập đã hết hạn. Vui lòng đăng nhập lại.",
                    type = UiEvent.SnackBarType.ERROR
                )
            }
        }
    }
}