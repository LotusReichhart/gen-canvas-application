package com.lotusreichhart.auth.presentation

import androidx.lifecycle.viewModelScope
import com.lotusreichhart.core.ui.event.GlobalUiEventManager
import com.lotusreichhart.core.ui.event.UiEvent
import com.lotusreichhart.core.utils.logD
import com.lotusreichhart.core.utils.logE
import com.lotusreichhart.core.viewmodel.BaseViewModel
import com.lotusreichhart.domain.exception.ServerException
import com.lotusreichhart.domain.monitor.NetworkMonitor
import com.lotusreichhart.domain.usecase.auth.RequestForgotPasswordUseCase
import com.lotusreichhart.domain.usecase.auth.RequestSignUpUseCase
import com.lotusreichhart.domain.usecase.auth.ResendOtpUseCase
import com.lotusreichhart.domain.usecase.auth.ResetPasswordUseCase
import com.lotusreichhart.domain.usecase.auth.SignInWithEmailUseCase
import com.lotusreichhart.domain.usecase.auth.SignInWithGoogleUseCase
import com.lotusreichhart.domain.usecase.auth.VerifyForgotPasswordUseCase
import com.lotusreichhart.domain.usecase.auth.VerifySignUpUseCase
import com.lotusreichhart.domain.usecase.legal.GetLegalInfoUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    networkMonitor: NetworkMonitor,
    globalUiEventManager: GlobalUiEventManager,
    private val signInWithEmailUseCase: SignInWithEmailUseCase,
    private val signInWithGoogleUseCase: SignInWithGoogleUseCase,
    private val requestSignUpUseCase: RequestSignUpUseCase,
    private val verifySignUpUseCase: VerifySignUpUseCase,
    private val requestForgotPasswordUseCase: RequestForgotPasswordUseCase,
    private val verifyForgotPasswordUseCase: VerifyForgotPasswordUseCase,
    private val resetPasswordUseCase: ResetPasswordUseCase,
    private val resendOtpUseCase: ResendOtpUseCase,
    private val getLegalInfoUseCase: GetLegalInfoUseCase,
) : BaseViewModel(networkMonitor, globalUiEventManager) {
    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            networkMonitor.isOnline.collectLatest { isOnline ->
                _uiState.update { it.copy(isOffline = !isOnline) }
            }
        }
        fetchLegalInfo()
    }

    fun onNameChange(newValue: String) {
        _uiState.update { it.copy(name = newValue) }
    }

    fun onEmailChange(newValue: String) {
        _uiState.update { it.copy(email = newValue, emailError = null) }
    }

    fun onPasswordChange(newValue: String) {
        _uiState.update { it.copy(password = newValue, passwordError = null) }
    }

    fun onConfirmPasswordChange(newValue: String) {
        _uiState.update { it.copy(confirmPassword = newValue) }
    }

    fun onOtpChange(newValue: String) {
        if (newValue.length <= 6) {
            _uiState.update { it.copy(otpValue = newValue) }
        }
    }

    fun onSignInClick() {
        val email = _uiState.value.email
        val password = _uiState.value.password

        logD("onSignInClick running.... $email")

        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    loadingType = AuthLoadingType.EMAIL_SIGN_IN,
                    error = null
                )
            }

            val result = signInWithEmailUseCase(email, password)
            logD("onSignInClick result: $result")

            if (result.isSuccess) {
                sendUiEvent(
                    UiEvent.ShowSnackBar(
                        "Đăng nhập thành công!",
                        type = UiEvent.SnackBarType.SUCCESS
                    )
                )
                _uiState.update {
                    it.copy(
                        loadingType = AuthLoadingType.NONE,
                        navigationTarget = AuthNavigationTarget.MainScreen
                    )
                }
            } else {
                val exception = result.exceptionOrNull()
                logE("onSignInClick exception: ", exception)
                handleAuthError(exception)
            }
        }
    }

    fun onSignInAndSignUpWithGoogleClick(idToken: String) {

        logD("onSignInAndSignUpWithGoogleClick running....")

        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    loadingType = AuthLoadingType.GOOGLE_SIGN_IN,
                    error = null
                )
            }

            val result = signInWithGoogleUseCase(idToken)
            logD("onSignInAndSignUpWithGoogleClick result: $result")

            if (result.isSuccess) {
                sendUiEvent(
                    UiEvent.ShowSnackBar(
                        "Đăng nhập thành công!",
                        type = UiEvent.SnackBarType.SUCCESS
                    )
                )
                _uiState.update {
                    it.copy(
                        loadingType = AuthLoadingType.NONE,
                        navigationTarget = AuthNavigationTarget.MainScreen
                    )
                }
            } else {
                val exception = result.exceptionOrNull()
                logE("onSignInAndSignUpWithGoogleClick exception: ", exception)
                handleAuthError(exception)
            }
        }
    }

    fun onRequestSignUpClick() {
        val name = _uiState.value.name
        val email = _uiState.value.email
        val password = _uiState.value.password

        logD("onRequestSignUpClick running....: $name $email")

        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    loadingType = AuthLoadingType.REQUEST_SIGN_UP,
                    error = null
                )
            }

            val result = requestSignUpUseCase(name, email, password)
            logD("onRequestSignUpClick result: $result")

            if (result.isSuccess) {
                _uiState.update {
                    it.copy(
                        loadingType = AuthLoadingType.NONE,
                        navigationTarget = AuthNavigationTarget.VerifyOtpScreen
                    )
                }
            } else {
                val exception = result.exceptionOrNull()
                logE("onRequestSignUpClick exception: ", exception)
                handleAuthError(exception)
            }
        }
    }

    fun onVerifySignUpClick() {
        val otp = _uiState.value.otpValue
        val email = _uiState.value.email

        logD("onVerifySignUpClick running....: $otp $email")

        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    loadingType = AuthLoadingType.VERIFY_OTP,
                    error = null
                )
            }

            val result = verifySignUpUseCase(otp = otp, email = email)
            logD("onVerifySignUpClick result: $result")

            if (result.isSuccess) {
                sendUiEvent(
                    UiEvent.ShowSnackBar(
                        "Đăng ký thành công!",
                        type = UiEvent.SnackBarType.SUCCESS
                    )
                )
                _uiState.update {
                    it.copy(
                        loadingType = AuthLoadingType.NONE,
                        navigationTarget = AuthNavigationTarget.MainScreen
                    )
                }
            } else {
                val exception = result.exceptionOrNull()
                logE("onVerifySignUpClick exception: ", exception)
                handleAuthError(exception)
            }
        }
    }

    fun onRequestForgotPasswordClick() {
        val email = _uiState.value.email
        logD("onRequestForgotPasswordClick running....: $email")

        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    loadingType = AuthLoadingType.REQUEST_FORGOT_PASSWORD,
                    error = null
                )
            }

            val result = requestForgotPasswordUseCase(email = email)
            logD("onRequestForgotPasswordClick result: $result")

            if (result.isSuccess) {
                _uiState.update {
                    it.copy(
                        loadingType = AuthLoadingType.NONE,
                        navigationTarget = AuthNavigationTarget.VerifyOtpScreen
                    )
                }
            } else {
                val exception = result.exceptionOrNull()
                logE("onRequestForgotPasswordClick exception: ", exception)
                handleAuthError(exception)
            }
        }
    }

    fun onVerifyForgotPasswordClick() {
        val otp = _uiState.value.otpValue
        val email = _uiState.value.email
        logD("onVerifyForgotPasswordClick running....: $otp $email")

        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    loadingType = AuthLoadingType.VERIFY_OTP,
                    error = null
                )
            }

            val result = verifyForgotPasswordUseCase(email = email, otp = otp)
            logD("onVerifyForgotPasswordClick: $result")

            if (result.isSuccess) {

                val token = result.getOrNull()

                _uiState.update {
                    it.copy(
                        loadingType = AuthLoadingType.NONE,
                        navigationTarget = AuthNavigationTarget.ResetPasswordScreen,
                        resetToken = token ?: ""
                    )
                }
            } else {
                val exception = result.exceptionOrNull()
                logE("onVerifyForgotPasswordClick exception: ", exception)
                handleAuthError(exception)
            }
        }
    }

    fun onResetPasswordClick() {
        val resetToken = _uiState.value.resetToken
        val password = _uiState.value.password
        val confirmPassword = _uiState.value.confirmPassword
        logD("onResetPasswordClick running....")

        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    loadingType = AuthLoadingType.RESET_PASSWORD,
                    error = null
                )
            }

            val result = resetPasswordUseCase(
                resetToken = resetToken,
                newPassword = password,
                confirmPassword = confirmPassword
            )
            logD("onResetPasswordClick: $result")

            if (result.isSuccess) {
                _uiState.update {
                    sendUiEvent(
                        UiEvent.ShowSnackBar(
                            "Thay đổi mật khẩu thành công!",
                            type = UiEvent.SnackBarType.SUCCESS
                        )
                    )
                    it.copy(
                        loadingType = AuthLoadingType.NONE,
                        resetToken = "",
                        navigationTarget = AuthNavigationTarget.SignInScreen
                    )
                }
            } else {
                val exception = result.exceptionOrNull()
                logE("onResetPasswordClick exception: ", exception)
                handleAuthError(exception)
            }
        }
    }

    fun onResendOtpClick() {
        val email = _uiState.value.email
        logD("onResendOtpClick running....")

        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    loadingType = AuthLoadingType.RESEND_OTP,
                    error = null,
                    isResendSuccess = false
                )
            }

            val result = resendOtpUseCase(email = email)
            logD("onResendOtpClick: $result")
            if (result.isSuccess) {
                _uiState.update {
                    it.copy(
                        loadingType = AuthLoadingType.NONE,
                        isResendSuccess = true
                    )
                }
                sendUiEvent(
                    UiEvent.ShowSnackBar(
                        "Đã gửi lại mã xác thực",
                        type = UiEvent.SnackBarType.SUCCESS
                    )
                )
            } else {
                val exception = result.exceptionOrNull()
                logE("onResendOtpClick exception: ", exception)
                handleAuthError(exception)
            }
        }
    }

    fun consumeEvents() {
        _uiState.update { currentState ->
            currentState.copy(
                loadingType = AuthLoadingType.NONE,
                navigationTarget = null,
                error = null,

                password = "",
                confirmPassword = "",
                otpValue = "",

                emailError = null,
                passwordError = null,
                nameError = null,
                otpError = null
            )
        }
    }

    fun consumeResendSuccess() {
        _uiState.update { it.copy(isResendSuccess = false) }
    }

    fun clearAllState() {
        _uiState.update { AuthUiState() }
    }

    private fun fetchLegalInfo() {
        viewModelScope.launch {
            val result = getLegalInfoUseCase()
            if (result.isSuccess) {
                val info = result.getOrNull()
                _uiState.update {
                    it.copy(
                        termsUrl = info?.termsUrl,
                        privacyUrl = info?.privacyUrl
                    )
                }
                logD("Lấy thông tin pháp lý thành công: $info")
            } else {
                logE("Lấy thông tin pháp lý thất bại", result.exceptionOrNull())
            }
        }
    }

    private fun handleAuthError(exception: Throwable?) {
        logE("handleAuthError exception: ", exception)

        _uiState.update {
            it.copy(
                loadingType = AuthLoadingType.NONE,
                password = "",
                confirmPassword = "",
                otpValue = ""
            )
        }

        if (exception is ServerException && exception.fieldErrors != null) {
            val errors = exception.fieldErrors

            logD("handleAuthError errors body: $errors")

            _uiState.update { currentState ->
                currentState.copy(
                    nameError = errors?.get("name"),
                    emailError = errors?.get("email"),
                    passwordError = errors?.get("password") ?: errors?.get("new_password"),
                    confirmPasswordError = errors?.get("confirm_password"),
                    otpError = errors?.get("otp")
                )
            }

            val generalMessage = errors?.get("general") ?: errors?.get("message")

            if (generalMessage != null) {
                sendUiEvent(UiEvent.ShowSnackBar(generalMessage, type = UiEvent.SnackBarType.ERROR))
            }
        } else {
            val message = exception?.message ?: "Đã xảy ra lỗi không xác định"
            sendUiEvent(UiEvent.ShowSnackBar(message, type = UiEvent.SnackBarType.ERROR))
        }
    }
}