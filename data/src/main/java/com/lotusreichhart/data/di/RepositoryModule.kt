package com.lotusreichhart.data.di

import com.lotusreichhart.data.repositories.AuthRepositoryImpl
import com.lotusreichhart.data.repositories.BannerRepositoryImpl
import com.lotusreichhart.data.repositories.SettingsRepositoryImpl
import com.lotusreichhart.domain.repositories.AuthRepository
import com.lotusreichhart.domain.repositories.BannerRepository
import com.lotusreichhart.domain.repositories.SettingsRepository
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