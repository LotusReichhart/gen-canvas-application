package com.lotusreichhart.ads.di

import com.google.android.gms.ads.AdRequest
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object AdsModule {
    @Provides
    fun provideAdRequest(): AdRequest {
        return AdRequest.Builder().build()
    }
}