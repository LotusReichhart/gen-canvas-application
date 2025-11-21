package com.lotusreichhart.data.di

import com.lotusreichhart.domain.repository.LegalRepository
import com.lotusreichhart.domain.usecase.legal.CheckLegalUpdateUseCase
import com.lotusreichhart.domain.usecase.legal.GetLegalInfoUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object LegalUseCaseModule {

    @Provides
    fun provideGetLegalInfoUseCase(
        legalRepository: LegalRepository
    ): GetLegalInfoUseCase {
        return GetLegalInfoUseCase(legalRepository)
    }

    @Provides
    fun provideCheckLegalUpdateUseCase(
        legalRepository: LegalRepository
    ): CheckLegalUpdateUseCase {
        return CheckLegalUpdateUseCase(legalRepository)
    }
}