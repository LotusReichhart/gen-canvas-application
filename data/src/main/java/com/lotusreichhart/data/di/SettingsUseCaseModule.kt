package com.lotusreichhart.data.di

import com.lotusreichhart.domain.repository.SettingsRepository
import com.lotusreichhart.domain.usecase.settings.ReadOnboardingStateUseCase
import com.lotusreichhart.domain.usecase.settings.SetOnboardingCompletedUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object SettingsUseCaseModule {
    @Provides
    fun provideSetOnboardingCompletedUseCase(
        settingsRepository: SettingsRepository
    ): SetOnboardingCompletedUseCase {
        return SetOnboardingCompletedUseCase(settingsRepository)
    }

    @Provides
    fun provideReadOnboardingStateUseCase(
        settingsRepository: SettingsRepository
    ): ReadOnboardingStateUseCase {
        return ReadOnboardingStateUseCase(settingsRepository)
    }
}