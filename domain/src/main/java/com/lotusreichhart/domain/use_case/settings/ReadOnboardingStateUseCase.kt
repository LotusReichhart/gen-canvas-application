package com.lotusreichhart.domain.use_case.settings

import com.lotusreichhart.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.Flow

class ReadOnboardingStateUseCase(
    private val settingsRepository: SettingsRepository
) {
    operator fun invoke(): Flow<Boolean> {
        return settingsRepository.readOnboardingState()
    }
}