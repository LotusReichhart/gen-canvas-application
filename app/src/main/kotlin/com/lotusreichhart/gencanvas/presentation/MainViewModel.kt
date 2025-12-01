package com.lotusreichhart.gencanvas.presentation

import androidx.lifecycle.viewModelScope
import com.lotusreichhart.gencanvas.core.common.event.GlobalUiEventManager
import com.lotusreichhart.gencanvas.core.domain.usecase.setting.ReadOnboardingStateUseCase
import com.lotusreichhart.gencanvas.core.domain.util.NetworkMonitor
import com.lotusreichhart.gencanvas.core.ui.navigation.routes.GenCanvasRoute
import com.lotusreichhart.gencanvas.core.ui.viewmodel.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    readOnboardingStateUseCase: ReadOnboardingStateUseCase,
    networkMonitor: NetworkMonitor,
    globalUiEventManager: GlobalUiEventManager,
) : BaseViewModel(
    networkMonitor = networkMonitor,
    globalUiEventManager = globalUiEventManager
) {
    private val _uiState = MutableStateFlow(MainUiState())
    val uiState: StateFlow<MainUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            val hasCompletedOnboarding = readOnboardingStateUseCase().first()
            Timber.d("hasCompletedOnboarding - $hasCompletedOnboarding")

            val destination = GenCanvasRoute.ONBOARDING_FLOW_ROUTE


            _uiState.update { it.copy(startDestination = destination) }

            _uiState.update { it.copy(isLoading = false) }
        }
    }
}