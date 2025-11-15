package com.lotusreichhart.gencanvas.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lotusreichhart.core.navigation.Route
import com.lotusreichhart.domain.use_cases.settings.ReadOnboardingStateUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    readOnboardingStateUseCase: ReadOnboardingStateUseCase
) : ViewModel() {

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _startDestination = MutableStateFlow(Route.ONBOARDING_FLOW_ROUTE)
    val startDestination: StateFlow<String> = _startDestination.asStateFlow()

    init {
        viewModelScope.launch {
            val hasCompletedOnboarding = readOnboardingStateUseCase().first()
            Log.d("MainViewModel", "hasCompletedOnboarding - $hasCompletedOnboarding")

            if (hasCompletedOnboarding) {
                _startDestination.value = Route.MAIN_FLOW_ROUTE
            } else {
                _startDestination.value = Route.ONBOARDING_FLOW_ROUTE
            }

            _isLoading.value = false
        }
    }
}