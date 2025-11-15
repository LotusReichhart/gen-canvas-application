package com.lotusreichhart.data.di

import com.lotusreichhart.domain.repositories.BannerRepository
import com.lotusreichhart.domain.use_cases.banner.GetListBannerUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object BannerUseCaseModule {

    @Provides
    @Singleton
    fun provideGetListBannerUseCase(
        bannerRepository: BannerRepository
    ): GetListBannerUseCase {
        return GetListBannerUseCase(bannerRepository)
    }
}