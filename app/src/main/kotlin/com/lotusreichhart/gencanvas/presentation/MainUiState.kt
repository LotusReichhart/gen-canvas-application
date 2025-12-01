package com.lotusreichhart.gencanvas.presentation

import com.lotusreichhart.gencanvas.core.ui.navigation.routes.GenCanvasRoute

data class MainUiState(
    val isLoading: Boolean = true,
    val startDestination: String = GenCanvasRoute.ONBOARDING_FLOW_ROUTE
)
