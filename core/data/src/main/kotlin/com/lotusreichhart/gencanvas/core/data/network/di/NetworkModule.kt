package com.lotusreichhart.gencanvas.core.data.network.di

import com.lotusreichhart.gencanvas.core.common.event.GlobalUiEventManager
import com.lotusreichhart.gencanvas.core.data.BuildConfig
import com.lotusreichhart.gencanvas.core.data.database.dao.UserDao
import com.lotusreichhart.gencanvas.core.data.datastore.TokenDataStore
import com.lotusreichhart.gencanvas.core.data.network.interceptor.AuthInterceptor
import com.lotusreichhart.gencanvas.core.data.network.interceptor.LanguageInterceptor
import com.lotusreichhart.gencanvas.core.data.network.interceptor.TokenAuthenticator
import com.lotusreichhart.gencanvas.core.data.network.service.AuthApiService
import com.lotusreichhart.gencanvas.core.data.network.service.BannerApiService
import com.lotusreichhart.gencanvas.core.data.network.service.LegalInfoApiService
import com.lotusreichhart.gencanvas.core.data.network.service.UserApiService
import dagger.Lazy
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import jakarta.inject.Named
import jakarta.inject.Singleton
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Provides
    @Singleton
    fun provideBaseUrl(): String {
        return "${BuildConfig.BASE_URL}/v1/"
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
        tokenDataStore: TokenDataStore
    ): AuthInterceptor {
        return AuthInterceptor(tokenDataStore)
    }

    @Provides
    @Singleton
    fun provideLanguageInterceptor(): LanguageInterceptor {
        return LanguageInterceptor()
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
        tokenDataStore: TokenDataStore,
        authApiService: Lazy<AuthApiService>,
        userDao: UserDao,
        globalUiEventManager: GlobalUiEventManager
    ): TokenAuthenticator {
        return TokenAuthenticator(
            tokenDataStore = tokenDataStore,
            authApiService = authApiService,
            userDao = userDao,
            globalUiEventManager = globalUiEventManager
        )
    }

    @Provides
    @Singleton
    @Named("MainOkHttpClient")
    fun provideOkHttpClient(
        authInterceptor: AuthInterceptor,
        tokenAuthenticator: TokenAuthenticator,
        loggingInterceptor: HttpLoggingInterceptor,
        languageInterceptor: LanguageInterceptor
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(languageInterceptor)
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

    @Provides
    @Singleton
    fun provideUserApiService(
        @Named("MainRetrofit") retrofit: Retrofit
    ): UserApiService {
        return retrofit.create(UserApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideLegalApiService(
        @Named("MainRetrofit") retrofit: Retrofit
    ): LegalInfoApiService {
        return retrofit.create(LegalInfoApiService::class.java)
    }
}