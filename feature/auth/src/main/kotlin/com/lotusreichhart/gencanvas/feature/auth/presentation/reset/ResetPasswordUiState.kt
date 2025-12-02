package com.lotusreichhart.gencanvas.feature.auth.presentation.reset

internal data class ResetPasswordUiState(
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,

    val password: String = "",
    val confirmPassword: String = "",

    val passwordErrorMessage: String? = null,
    val confirmPasswordErrorMessage: String? = null
)
