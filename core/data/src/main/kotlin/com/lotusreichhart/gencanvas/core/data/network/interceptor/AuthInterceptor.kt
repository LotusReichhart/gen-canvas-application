package com.lotusreichhart.gencanvas.core.data.network.interceptor

import com.lotusreichhart.gencanvas.core.data.datastore.TokenDataStore
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import timber.log.Timber

class AuthInterceptor(
    private val tokenDataStore: TokenDataStore
): Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()

        if (isAuthRequest(request)) {
            return chain.proceed(request)
        }

        val token = tokenDataStore.getAccessTokenValue()
        Timber.d("AuthInterceptor token: $token")

        val newRequest = if (token != null) {
            request.newBuilder()
                .addHeader("Authorization", "Bearer $token")
                .build()
        } else {
            request
        }
        return chain.proceed(newRequest)
    }
}

private fun isAuthRequest(request: Request): Boolean {
    val path = request.url.encodedPath
    return path.endsWith("/auth/signin") ||
            path.endsWith("/auth/google") ||
            path.endsWith("/auth/signup") ||
            path.endsWith("/auth/verify") ||
            path.endsWith("/auth/otp/resend") ||
            path.endsWith("/auth/otp/verify") ||
            path.endsWith("/auth/password/forgot") ||
            path.endsWith("/auth/password/verify") ||
            path.endsWith("/auth/password/reset") ||
            path.endsWith("/auth/refresh") ||
            path.endsWith("/banners") ||
            path.endsWith("/legal/info")
}