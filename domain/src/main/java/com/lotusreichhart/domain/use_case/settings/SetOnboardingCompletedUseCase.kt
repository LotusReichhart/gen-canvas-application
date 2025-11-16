package com.lotusreichhart.domain.use_case.settings

import com.lotusreichhart.domain.repository.SettingsRepository

class SetOnboardingCompletedUseCase(
    private val settingsRepository: SettingsRepository
) {
    suspend operator fun invoke() {
        settingsRepository.saveOnboardingState(completed = true)
    }
}