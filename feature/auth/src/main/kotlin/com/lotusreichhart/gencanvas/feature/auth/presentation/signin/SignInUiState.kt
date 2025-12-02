package com.lotusreichhart.gencanvas.feature.auth.presentation.signin

internal enum class SignInLoadingType {
    NONE,
    EMAIL_SIGN_IN,
    GOOGLE_SIGN_IN,
}

internal data class SignInUiState(
    val loadingType: SignInLoadingType = SignInLoadingType.NONE,
    val isSuccess: Boolean = false,

    val password: String = "",

    val emailErrorMessage: String? = null,
    val passwordErrorMessage: String? = null
) {
    val isLoading: Boolean
        get() = loadingType != SignInLoadingType.NONE
}
