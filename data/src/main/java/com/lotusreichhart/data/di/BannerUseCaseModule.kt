package com.lotusreichhart.data.di

import com.lotusreichhart.domain.repository.BannerRepository
import com.lotusreichhart.domain.repository.SettingsRepository
import com.lotusreichhart.domain.usecase.banner.GetCachedBannersUseCase
import com.lotusreichhart.domain.usecase.banner.GetListBannerUseCase
import com.lotusreichhart.domain.usecase.banner.RefreshBannersUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object BannerUseCaseModule {

    @Provides
    fun provideGetListBannerUseCase(
        bannerRepository: BannerRepository,
        settingsRepository: SettingsRepository
    ): GetListBannerUseCase {
        return GetListBannerUseCase(
            bannerRepository = bannerRepository,
            settingsRepository = settingsRepository
        )
    }

    @Provides
    fun provideGetCachedBannersUseCase(
        bannerRepository: BannerRepository
    ): GetCachedBannersUseCase {
        return GetCachedBannersUseCase(bannerRepository)
    }

    @Provides
    fun provideRefreshBannersUseCase(
        bannerRepository: BannerRepository,
        settingsRepository: SettingsRepository
    ): RefreshBannersUseCase {
        return RefreshBannersUseCase(
            bannerRepository = bannerRepository,
            settingsRepository = settingsRepository
        )
    }
}