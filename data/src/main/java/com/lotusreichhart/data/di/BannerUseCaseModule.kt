package com.lotusreichhart.data.di

import com.lotusreichhart.domain.repository.BannerRepository
import com.lotusreichhart.domain.use_case.banner.GetListBannerUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object BannerUseCaseModule {

    @Provides
    fun provideGetListBannerUseCase(
        bannerRepository: BannerRepository
    ): GetListBannerUseCase {
        return GetListBannerUseCase(bannerRepository)
    }
}