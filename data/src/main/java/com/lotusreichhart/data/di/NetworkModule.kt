package com.lotusreichhart.data.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.Lazy
import jakarta.inject.Singleton
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import jakarta.inject.Named

import com.lotusreichhart.data.remote.interceptor.AuthInterceptor
import com.lotusreichhart.data.remote.interceptor.TokenAuthenticator
import com.lotusreichhart.data.remote.service.AuthApiService

import com.lotusreichhart.data.BuildConfig
import com.lotusreichhart.data.local.TokenLocalDataSource
import com.lotusreichhart.data.remote.service.BannerApiService

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Provides
    @Singleton
    fun provideBaseUrl(): String {
        return "${BuildConfig.BASE_URL}/api/v1/"
    }

    @Provides
    @Singleton
    fun provideLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }

    @Provides
    @Singleton
    fun provideAuthInterceptor(
        tokenDataSource: TokenLocalDataSource
    ): AuthInterceptor {
        return AuthInterceptor(tokenDataSource)
    }

    @Provides
    @Singleton
    @Named("AuthOkHttpClient")
    fun provideAuthOkHttpClient(
        loggingInterceptor: HttpLoggingInterceptor
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addNetworkInterceptor(loggingInterceptor)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()
    }

    @Provides
    @Singleton
    @Named("AuthRetrofit")
    fun provideAuthRetrofit(
        @Named("AuthOkHttpClient") okHttpClient: OkHttpClient,
        baseUrl: String
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideAuthApiService(
        @Named("AuthRetrofit") retrofit: Retrofit
    ): AuthApiService {
        return retrofit.create(AuthApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideTokenAuthenticator(
        tokenDataSource: TokenLocalDataSource,
        authApiService: Lazy<AuthApiService>
    ): TokenAuthenticator {
        return TokenAuthenticator(
            tokenDataSource = tokenDataSource,
            authApiService = authApiService
        )
    }

    @Provides
    @Singleton
    @Named("MainOkHttpClient")
    fun provideOkHttpClient(
        authInterceptor: AuthInterceptor,
        tokenAuthenticator: TokenAuthenticator,
        loggingInterceptor: HttpLoggingInterceptor
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .authenticator(tokenAuthenticator)
            .addNetworkInterceptor(loggingInterceptor)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()
    }

    @Provides
    @Singleton
    @Named("MainRetrofit")
    fun provideRetrofit(
        @Named("MainOkHttpClient") okHttpClient: OkHttpClient,
        baseUrl: String
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideBannerApiService(
        @Named("MainRetrofit") retrofit: Retrofit
    ): BannerApiService {
        return retrofit.create(BannerApiService::class.java)
    }
}