package com.lotusreichhart.domain.use_cases.settings

import com.lotusreichhart.domain.repositories.SettingsRepository

class SetOnboardingCompletedUseCase(
    private val settingsRepository: SettingsRepository
) {
    suspend operator fun invoke() {
        settingsRepository.saveOnboardingState(completed = true)
    }
}