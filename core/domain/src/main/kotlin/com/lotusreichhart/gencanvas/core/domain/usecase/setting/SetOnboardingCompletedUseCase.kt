package com.lotusreichhart.gencanvas.core.domain.usecase.setting

import com.lotusreichhart.gencanvas.core.domain.repository.SettingRepository
import javax.inject.Inject

class SetOnboardingCompletedUseCase @Inject constructor(
    private val settingRepository: SettingRepository
) {
    suspend operator fun invoke() {
        settingRepository.saveOnboardingState(completed = true)
    }
}