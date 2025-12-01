package com.lotusreichhart.gencanvas.feature.onboarding.presentation

import androidx.lifecycle.ViewModel
import com.lotusreichhart.gencanvas.core.domain.usecase.setting.SetOnboardingCompletedUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class OnboardingViewModel @Inject constructor(
    private val setOnboardingCompletedUseCase: SetOnboardingCompletedUseCase
) : ViewModel() {
    suspend fun onGetStartedClick() {
        setOnboardingCompletedUseCase()
    }
}