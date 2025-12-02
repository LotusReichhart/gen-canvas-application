package com.lotusreichhart.gencanvas.feature.auth.presentation.forgot

internal data class ForgotPasswordUiState(
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,

    val emailErrorMessage: String? = null,
)
