package com.lotusreichhart.gencanvas.presentation

import androidx.lifecycle.viewModelScope
import com.lotusreichhart.gencanvas.core.common.event.GlobalUiEventManager
import com.lotusreichhart.gencanvas.core.common.event.UiEvent
import com.lotusreichhart.gencanvas.core.common.util.TextResource
import com.lotusreichhart.gencanvas.core.domain.usecase.legal.CheckLegalUpdateUseCase
import com.lotusreichhart.gencanvas.core.domain.usecase.legal.GetLegalInfoUseCase
import com.lotusreichhart.gencanvas.core.domain.usecase.setting.ReadOnboardingStateUseCase
import com.lotusreichhart.gencanvas.core.domain.usecase.user.FetchProfileUseCase
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
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import javax.inject.Inject

import com.lotusreichhart.gencanvas.R
import com.lotusreichhart.gencanvas.core.common.R as CoreR

@HiltViewModel
class MainViewModel @Inject constructor(
    readOnboardingStateUseCase: ReadOnboardingStateUseCase,
    networkMonitor: NetworkMonitor,
    globalUiEventManager: GlobalUiEventManager,
    private val checkLegalUpdateUseCase: CheckLegalUpdateUseCase,
    private val getLegalInfoUseCase: GetLegalInfoUseCase,
    private val fetchProfileUseCase: FetchProfileUseCase
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

            val destination = if (hasCompletedOnboarding) {
                GenCanvasRoute.MAIN_FLOW_ROUTE
            } else {
                GenCanvasRoute.ONBOARDING_FLOW_ROUTE
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
                            title = TextResource.Id(resId = R.string.dialog_title_legal_update),
                            message = TextResource.Id(R.string.dialog_message_legal_update),
                            positiveButtonText = TextResource.Id(R.string.action_view_now),
                            onPositiveClick = {
                                val url = legalInfo.termsUrl
                                val encodedUrl =
                                    URLEncoder.encode(url, StandardCharsets.UTF_8.toString())
                                val route =
                                    GenCanvasRoute.createWebViewRoute(
                                        url = encodedUrl,
                                        titleResId = CoreR.string.core_label_terms_of_service
                                    )
                                sendUiEvent(UiEvent.Navigate(route))
                            },
                            negativeButtonText = TextResource.Id(R.string.action_got_it),
                            onNegativeClick = { }
                        )
                    )
                }
            }
        }
    }
}