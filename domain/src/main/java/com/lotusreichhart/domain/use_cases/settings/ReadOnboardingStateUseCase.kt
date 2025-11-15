package com.lotusreichhart.domain.use_cases.settings

import com.lotusreichhart.domain.repositories.SettingsRepository
import kotlinx.coroutines.flow.Flow

class ReadOnboardingStateUseCase(
    private val settingsRepository: SettingsRepository
) {
    operator fun invoke(): Flow<Boolean> {
        return settingsRepository.readOnboardingState()
    }
}