package com.lotusreichhart.gencanvas.core.data.repository.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

import com.lotusreichhart.gencanvas.core.data.repository.AuthRepositoryImpl
import com.lotusreichhart.gencanvas.core.data.repository.BannerRepositoryImpl
import com.lotusreichhart.gencanvas.core.data.repository.EditingRepositoryImpl
import com.lotusreichhart.gencanvas.core.data.repository.LegalInfoRepositoryImpl
import com.lotusreichhart.gencanvas.core.data.repository.SettingRepositoryImpl
import com.lotusreichhart.gencanvas.core.data.repository.UserRepositoryImpl

import com.lotusreichhart.gencanvas.core.domain.repository.AuthRepository
import com.lotusreichhart.gencanvas.core.domain.repository.BannerRepository
import com.lotusreichhart.gencanvas.core.domain.repository.EditingRepository
import com.lotusreichhart.gencanvas.core.domain.repository.LegalInfoRepository
import com.lotusreichhart.gencanvas.core.domain.repository.SettingRepository
import com.lotusreichhart.gencanvas.core.domain.repository.UserRepository

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
    abstract fun bindSettingRepository(
        impl: SettingRepositoryImpl
    ): SettingRepository

    @Binds
    @Singleton
    abstract fun bindBannerRepository(
        impl: BannerRepositoryImpl
    ): BannerRepository

    @Binds
    @Singleton
    abstract fun bindUserRepository(
        impl: UserRepositoryImpl
    ): UserRepository

    @Binds
    @Singleton
    abstract fun bindLegalInfoRepository(
        impl: LegalInfoRepositoryImpl
    ): LegalInfoRepository

    @Binds
    @Singleton
    abstract fun bindEditingRepository(
        impl: EditingRepositoryImpl
    ): EditingRepository
}