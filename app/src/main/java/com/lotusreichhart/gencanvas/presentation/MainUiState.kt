package com.lotusreichhart.gencanvas.presentation

import com.lotusreichhart.core.navigation.Route

data class MainUiState(
    val isLoading: Boolean = true,
    val startDestination: String = Route.ONBOARDING_FLOW_ROUTE
)