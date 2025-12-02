package com.lotusreichhart.gencanvas.feature.auth.presentation.signup

internal data class SignUpUiState(
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,

    val name: String = "",
    val password: String = "",

    val nameErrorMessage: String? = null,
    val emailErrorMessage: String? = null,
    val passwordErrorMessage: String? = null
)
