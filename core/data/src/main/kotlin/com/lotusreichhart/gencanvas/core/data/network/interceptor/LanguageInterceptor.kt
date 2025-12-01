package com.lotusreichhart.gencanvas.core.data.network.interceptor

import com.lotusreichhart.gencanvas.core.common.util.LanguageManager
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class LanguageInterceptor @Inject constructor() : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val languageCode = LanguageManager.getCurrentLanguage()

        val newRequest = originalRequest.newBuilder()
            .header("Accept-Language", languageCode)
            .build()

        return chain.proceed(newRequest)
    }
}