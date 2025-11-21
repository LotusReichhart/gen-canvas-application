package com.lotusreichhart.auth.presentation

sealed interface AuthNavigationTarget {
    data object MainScreen : AuthNavigationTarget
    data object VerifyOtpScreen : AuthNavigationTarget
    data object SignInScreen : AuthNavigationTarget
    data object ResetPasswordScreen : AuthNavigationTarget
}

enum class AuthLoadingType {
    NONE,
    EMAIL_SIGN_IN,
    GOOGLE_SIGN_IN,
    REQUEST_SIGN_UP,
    REQUEST_FORGOT_PASSWORD,
    VERIFY_OTP,
    RESET_PASSWORD,
    RESEND_OTP
}

data class AuthUiState(
    val loadingType: AuthLoadingType = AuthLoadingType.NONE,
    val isOffline: Boolean = false,
    val error: String? = null,

    val navigationTarget: AuthNavigationTarget? = null,
    val isResendSuccess: Boolean = false,

    val name: String = "",
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val otpValue: String = "",

    val resetToken: String = "",

    val termsUrl: String? = null,
    val privacyUrl: String? = null,

    val nameError: String? = null,
    val emailError: String? = null,
    val passwordError: String? = null,
    val confirmPasswordError: String? = null,
    val otpError: String? = null
) {
    val isLoading: Boolean
        get() = loadingType != AuthLoadingType.NONE
}
