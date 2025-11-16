package com.lotusreichhart.data.remote.interceptor

import com.lotusreichhart.data.local.TokenLocalDataSource
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response


class AuthInterceptor(
    private val tokenDataSource: TokenLocalDataSource
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()

        if (isAuthRequest(request)) {
            return chain.proceed(request)
        }

        val token = tokenDataSource.getAccessTokenValue()

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
            path.endsWith("/banners")
}