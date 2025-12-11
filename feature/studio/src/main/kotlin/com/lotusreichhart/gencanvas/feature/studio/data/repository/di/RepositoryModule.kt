package com.lotusreichhart.gencanvas.feature.studio.data.repository.di

import com.lotusreichhart.gencanvas.feature.studio.data.repository.EditingRepositoryImpl
import com.lotusreichhart.gencanvas.feature.studio.domain.repository.EditingRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal abstract class RepositoryModule {
    @Binds
    @Singleton
    abstract fun bindEditingRepository(
        impl: EditingRepositoryImpl
    ): EditingRepository
}