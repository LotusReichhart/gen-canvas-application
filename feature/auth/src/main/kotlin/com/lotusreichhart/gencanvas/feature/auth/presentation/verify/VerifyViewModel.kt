package com.lotusreichhart.gencanvas.feature.auth.presentation.verify

import androidx.lifecycle.viewModelScope
import com.lotusreichhart.gencanvas.core.common.event.GlobalUiEventManager
import com.lotusreichhart.gencanvas.core.common.event.UiEvent
import com.lotusreichhart.gencanvas.core.common.util.TextResource
import com.lotusreichhart.gencanvas.core.domain.usecase.auth.ResendOtpUseCase
import com.lotusreichhart.gencanvas.core.domain.usecase.auth.VerifyForgotPasswordUseCase
import com.lotusreichhart.gencanvas.core.domain.usecase.auth.VerifySignUpUseCase
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
internal class VerifyViewModel @Inject constructor(
    networkMonitor: NetworkMonitor,
    globalUiEventManager: GlobalUiEventManager,
    private val verifySignUpUseCase: VerifySignUpUseCase,
    private val verifyForgotPasswordUseCase: VerifyForgotPasswordUseCase,
    private val resendOtpUseCase: ResendOtpUseCase,
    private val authErrorHandler: AuthErrorHandler
) : BaseViewModel(
    networkMonitor = networkMonitor,
    globalUiEventManager = globalUiEventManager,
) {
    private val _uiState = MutableStateFlow(VerifyUiState())
    val uiState: StateFlow<VerifyUiState> = _uiState.asStateFlow()

    fun onOtpChange(newValue: String) {
        _uiState.update { it.copy(otp = newValue, otpErrorMessage = null) }
    }

    fun onVerifySignUpClick(email: String) {
        val otp = _uiState.value.otp

        Timber.d("onVerifySignUpClick running....: $otp $email")


        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    isLoading = true
                )
            }

            val result = verifySignUpUseCase(otp = otp, email = email)
            Timber.d("onVerifySignUpClick result: $result")


            if (result.isSuccess) {
                sendUiEvent(
                    UiEvent.ShowSnackBar(
                        TextResource.Id(R.string.signup_successful),
                        type = UiEvent.SnackBarType.SUCCESS
                    )
                )
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        navigationTarget = NavigationTarget.MainScreen
                    )
                }
            } else {
                val exception = result.exceptionOrNull()

                authErrorHandler.handleError(exception) { errors ->
                    _uiState.update {
                        it.copy(
                            otpErrorMessage = errors["otp"],
                        )
                    }
                }

                _uiState.update { it.copy(isLoading = false) }
            }
        }
    }

    fun onVerifyForgotPasswordClick(email: String) {
        val otp = _uiState.value.otp
        Timber.d("onVerifyForgotPasswordClick running....: $otp $email")


        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    isLoading = true
                )
            }

            val result = verifyForgotPasswordUseCase(email = email, otp = otp)
            Timber.d("onVerifyForgotPasswordClick result: $result")


            if (result.isSuccess) {

                val token = result.getOrNull()

                _uiState.update {
                    it.copy(
                        isLoading = false,
                        navigationTarget = NavigationTarget.ResetPasswordScreen,
                        resetToken = token ?: ""
                    )
                }
            } else {
                val exception = result.exceptionOrNull()

                authErrorHandler.handleError(exception) { errors ->
                    _uiState.update {
                        it.copy(
                            otpErrorMessage = errors["otp"],
                        )
                    }
                }

                _uiState.update { it.copy(isLoading = false) }
            }
        }
    }

    fun onResendOtpClick(email: String) {
        Timber.d("onResendOtpClick running....")


        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    isResendLoading = true
                )
            }

            val result = resendOtpUseCase(email = email)
            Timber.d("onResendOtpClick result: $result")


            if (result.isSuccess) {
                _uiState.update {
                    it.copy(
                        isResendLoading = false,
                        isResendSuccess = true
                    )
                }
                sendUiEvent(
                    UiEvent.ShowSnackBar(
                        TextResource.Id(R.string.resend_otp_successful),
                        type = UiEvent.SnackBarType.SUCCESS
                    )
                )
            } else {
                val exception = result.exceptionOrNull()

                authErrorHandler.handleError(exception) {}

                _uiState.update { it.copy(isResendLoading = false) }
            }
        }
    }
}