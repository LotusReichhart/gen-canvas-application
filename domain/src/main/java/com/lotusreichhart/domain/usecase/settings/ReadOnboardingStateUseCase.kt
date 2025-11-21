package com.lotusreichhart.domain.usecase.settings

import com.lotusreichhart.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.Flow

class ReadOnboardingStateUseCase(
    private val settingsRepository: SettingsRepository
) {
    operator fun invoke(): Flow<Boolean> {
        return settingsRepository.readOnboardingState()
    }
}