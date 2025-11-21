package com.lotusreichhart.gencanvas.presentation

import androidx.lifecycle.viewModelScope
import com.lotusreichhart.core.navigation.Route
import com.lotusreichhart.core.ui.event.GlobalUiEventManager
import com.lotusreichhart.core.ui.event.UiEvent
import com.lotusreichhart.core.utils.logD
import com.lotusreichhart.core.viewmodel.BaseViewModel
import com.lotusreichhart.domain.monitor.NetworkMonitor
import com.lotusreichhart.domain.usecase.legal.CheckLegalUpdateUseCase
import com.lotusreichhart.domain.usecase.legal.GetLegalInfoUseCase
import com.lotusreichhart.domain.usecase.settings.ReadOnboardingStateUseCase
import com.lotusreichhart.domain.usecase.user.FetchProfileUseCase
import com.lotusreichhart.domain.usecase.user.GetProfileStreamUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    readOnboardingStateUseCase: ReadOnboardingStateUseCase,
    networkMonitor: NetworkMonitor,
    globalUiEventManager: GlobalUiEventManager,
    getProfileStreamUseCase: GetProfileStreamUseCase,
    private val checkLegalUpdateUseCase: CheckLegalUpdateUseCase,
    private val getLegalInfoUseCase: GetLegalInfoUseCase,
    private val fetchProfileUseCase: FetchProfileUseCase
) : BaseViewModel(
    networkMonitor = networkMonitor,
    globalUiEventManager = globalUiEventManager,
    getProfileStreamUseCase = getProfileStreamUseCase
) {

    private val _uiState = MutableStateFlow(MainUiState())
    val uiState: StateFlow<MainUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            val hasCompletedOnboarding = readOnboardingStateUseCase().first()
            logD("hasCompletedOnboarding - $hasCompletedOnboarding")
            val destination = if (hasCompletedOnboarding) {
                Route.MAIN_FLOW_ROUTE
            } else {
                Route.ONBOARDING_FLOW_ROUTE
            }

            _uiState.update { it.copy(startDestination = destination) }

            fetchUserInfo()
            checkLegalUpdates()

            _uiState.update { it.copy(isLoading = false) }
        }
    }

    private fun fetchUserInfo() {
        viewModelScope.launch {
            fetchProfileUseCase()
        }
    }

    private fun checkLegalUpdates() {
        viewModelScope.launch {
            val updateResult = checkLegalUpdateUseCase()

            if (updateResult.isSuccess && updateResult.getOrNull() == true) {
                val infoResult = getLegalInfoUseCase()
                val legalInfo = infoResult.getOrNull()

                if (legalInfo != null) {
                    sendUiEvent(
                        UiEvent.ShowDialog(
                            title = "Cập nhật chính sách",
                            message = "Chúng tôi đã cập nhật Điều khoản dịch vụ và Chính sách bảo mật." +
                                    " Vui lòng xem lại những thay đổi mới nhất.",
                            positiveButtonText = "Xem ngay",
                            onPositiveClick = {
                                val url = legalInfo.termsUrl
                                val encodedUrl =
                                    URLEncoder.encode(url, StandardCharsets.UTF_8.toString())
                                val route =
                                    Route.createWebViewRoute(encodedUrl, "Điều khoản dịch vụ")
                                sendUiEvent(UiEvent.Navigate(route))
                            },
                            negativeButtonText = "Đã hiểu",
                            onNegativeClick = { }
                        )
                    )
                }
            }
        }
    }
}