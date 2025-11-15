package com.lotusreichhart.data.di

import com.lotusreichhart.domain.repositories.SettingsRepository
import com.lotusreichhart.domain.use_cases.settings.ReadOnboardingStateUseCase
import com.lotusreichhart.domain.use_cases.settings.SetOnboardingCompletedUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SettingsUseCaseModule {
    @Provides
    @Singleton
    fun provideSetOnboardingCompletedUseCase(
        settingsRepository: SettingsRepository
    ): SetOnboardingCompletedUseCase {
        return SetOnboardingCompletedUseCase(settingsRepository)
    }

    @Provides
    @Singleton
    fun provideReadOnboardingStateUseCase(
        settingsRepository: SettingsRepository
    ): ReadOnboardingStateUseCase {
        return ReadOnboardingStateUseCase(settingsRepository)
    }
}