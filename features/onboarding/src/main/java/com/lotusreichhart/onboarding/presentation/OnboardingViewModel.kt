package com.lotusreichhart.onboarding.presentation

import androidx.lifecycle.ViewModel
import com.lotusreichhart.domain.use_case.settings.SetOnboardingCompletedUseCase
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