package com.lotusreichhart.gencanvas.core.data.database.di

import com.lotusreichhart.gencanvas.core.data.database.GenCanvasDatabase
import com.lotusreichhart.gencanvas.core.data.database.dao.BannerDao
import com.lotusreichhart.gencanvas.core.data.database.dao.LegalInfoDao
import com.lotusreichhart.gencanvas.core.data.database.dao.UserDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal object DaoModule {
    @Provides
    fun providesUserDao(
        database: GenCanvasDatabase
    ): UserDao = database.userDao()

    @Provides
    fun providesLegalInfoDao(
        database: GenCanvasDatabase
    ): LegalInfoDao = database.legalInfoDao()

    @Provides
    fun providesBannerDao(
        database: GenCanvasDatabase
    ): BannerDao = database.bannerDao()
}