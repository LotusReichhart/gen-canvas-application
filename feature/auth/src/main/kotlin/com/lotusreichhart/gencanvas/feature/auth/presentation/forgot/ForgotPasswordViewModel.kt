package com.lotusreichhart.gencanvas.feature.auth.presentation.forgot

import androidx.lifecycle.viewModelScope
import com.lotusreichhart.gencanvas.core.common.event.GlobalUiEventManager
import com.lotusreichhart.gencanvas.core.domain.usecase.auth.RequestForgotPasswordUseCase
import com.lotusreichhart.gencanvas.core.domain.util.NetworkMonitor
import com.lotusreichhart.gencanvas.core.ui.viewmodel.BaseViewModel
import com.lotusreichhart.gencanvas.feature.auth.presentation.util.AuthErrorHandler
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
internal class ForgotPasswordViewModel @Inject constructor(
    networkMonitor: NetworkMonitor,
    globalUiEventManager: GlobalUiEventManager,
    private val requestForgotPasswordUseCase: RequestForgotPasswordUseCase,
    private val authErrorHandler: AuthErrorHandler
) : BaseViewModel(
    networkMonitor = networkMonitor,
    globalUiEventManager = globalUiEventManager
) {

    private val _uiState = MutableStateFlow(ForgotPasswordUiState())
    val uiState: StateFlow<ForgotPasswordUiState> = _uiState.asStateFlow()

    fun clearEmailErrorMessage() {
        _uiState.update { it.copy(emailErrorMessage = null) }
    }

    fun onRequestForgotPasswordClick(email: String) {
        Timber.d("onRequestForgotPasswordClick running....: $email")

        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    isLoading = true
                )
            }

            val result = requestForgotPasswordUseCase(email = email)
            Timber.d("onRequestForgotPasswordClick result: $result")


            if (result.isSuccess) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        isSuccess = true
                    )
                }
            } else {
                val exception = result.exceptionOrNull()

                authErrorHandler.handleError(exception) { errors ->
                    _uiState.update {
                        it.copy(
                            emailErrorMessage = errors["email"],
                        )
                    }
                }

                _uiState.update { it.copy(isLoading = false) }
            }
        }
    }

    fun clearState() {
        _uiState.update { ForgotPasswordUiState() }
    }
}