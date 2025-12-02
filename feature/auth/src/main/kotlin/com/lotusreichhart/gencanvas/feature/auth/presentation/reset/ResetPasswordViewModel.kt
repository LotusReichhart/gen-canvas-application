package com.lotusreichhart.gencanvas.feature.auth.presentation.reset

import androidx.lifecycle.viewModelScope
import com.lotusreichhart.gencanvas.core.common.event.GlobalUiEventManager
import com.lotusreichhart.gencanvas.core.common.event.UiEvent
import com.lotusreichhart.gencanvas.core.common.util.TextResource
import com.lotusreichhart.gencanvas.core.domain.usecase.auth.ResetPasswordUseCase
import com.lotusreichhart.gencanvas.core.domain.util.NetworkMonitor
import com.lotusreichhart.gencanvas.core.ui.viewmodel.BaseViewModel
import com.lotusreichhart.gencanvas.feature.auth.R
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
internal class ResetPasswordViewModel @Inject constructor(
    networkMonitor: NetworkMonitor,
    globalUiEventManager: GlobalUiEventManager,
    private val resetPasswordUseCase: ResetPasswordUseCase,
    private val authErrorHandler: AuthErrorHandler
) : BaseViewModel(
    networkMonitor = networkMonitor,
    globalUiEventManager = globalUiEventManager
) {
    private val _uiState = MutableStateFlow(ResetPasswordUiState())
    val uiState: StateFlow<ResetPasswordUiState> = _uiState.asStateFlow()

    fun onPasswordChange(newValue: String) {
        _uiState.update { it.copy(password = newValue, passwordErrorMessage = null) }
    }

    fun onConfirmPasswordChange(newValue: String) {
        _uiState.update { it.copy(confirmPassword = newValue, confirmPasswordErrorMessage = null) }
    }

    fun onResetPasswordClick(resetToken: String) {
        val password = _uiState.value.password
        val confirmPassword = _uiState.value.confirmPassword
        Timber.d("onResetPasswordClick running.... pass - $password $confirmPassword token - $resetToken")

        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    isLoading = true
                )
            }

            val result = resetPasswordUseCase(
                resetToken = resetToken,
                newPassword = password,
                confirmPassword = confirmPassword
            )
            Timber.d("onResetPasswordClick: $result")


            if (result.isSuccess) {
                _uiState.update {
                    sendUiEvent(
                        UiEvent.ShowSnackBar(
                            TextResource.Id(R.string.reset_password_successful),
                            type = UiEvent.SnackBarType.SUCCESS
                        )
                    )
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
                            passwordErrorMessage = errors["new_password"],
                            confirmPasswordErrorMessage = errors["confirm_password"]
                        )
                    }
                }

                _uiState.update { it.copy(isLoading = false) }
            }
        }
    }
}