package com.lotusreichhart.gencanvas.core.domain.usecase.setting

import com.lotusreichhart.gencanvas.core.domain.repository.SettingRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ReadOnboardingStateUseCase @Inject constructor(
    private val settingRepository: SettingRepository
) {
    operator fun invoke(): Flow<Boolean> {
        return settingRepository.readOnboardingState()
    }
}