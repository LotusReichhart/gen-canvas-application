package com.lotusreichhart.data.di

import com.lotusreichhart.data.repository.AuthRepositoryImpl
import com.lotusreichhart.data.repository.BannerRepositoryImpl
import com.lotusreichhart.data.repository.SettingsRepositoryImpl
import com.lotusreichhart.domain.repository.AuthRepository
import com.lotusreichhart.domain.repository.BannerRepository
import com.lotusreichhart.domain.repository.SettingsRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    @Singleton
    abstract fun bindAuthRepository(
        impl: AuthRepositoryImpl
    ): AuthRepository

    @Binds
    @Singleton
    abstract fun bindSettingsRepository(
        impl: SettingsRepositoryImpl
    ): SettingsRepository

    @Binds
    @Singleton
    abstract fun bindBannerRepository(
        impl: BannerRepositoryImpl
    ): BannerRepository
}