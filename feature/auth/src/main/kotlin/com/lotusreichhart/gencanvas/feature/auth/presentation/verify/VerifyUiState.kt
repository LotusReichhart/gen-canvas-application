package com.lotusreichhart.gencanvas.feature.auth.presentation.verify

internal sealed interface NavigationTarget {
    data object MainScreen : NavigationTarget
    data object ResetPasswordScreen : NavigationTarget
}

internal data class VerifyUiState(
    val isLoading: Boolean = false,
    val isResendLoading: Boolean = false,
    val isResendSuccess: Boolean = false,
    val navigationTarget: NavigationTarget? = null,

    val otp: String = "",
    val resetToken: String = "",

    val otpErrorMessage: String? = null
)
