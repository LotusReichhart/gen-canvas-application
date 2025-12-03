package com.lotusreichhart.gencanvas.feature.auth.presentation

internal data class AuthSharedUiState(
    val email: String = "",
    val resetToken: String = "",
    val termsUrl: String? = null,
    val privacyUrl: String? = null
)
