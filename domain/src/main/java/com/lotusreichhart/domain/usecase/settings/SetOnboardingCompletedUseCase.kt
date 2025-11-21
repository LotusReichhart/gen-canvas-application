package com.lotusreichhart.domain.usecase.settings

import com.lotusreichhart.domain.repository.SettingsRepository

class SetOnboardingCompletedUseCase(
    private val settingsRepository: SettingsRepository
) {
    suspend operator fun invoke() {
        settingsRepository.saveOnboardingState(completed = true)
    }
}