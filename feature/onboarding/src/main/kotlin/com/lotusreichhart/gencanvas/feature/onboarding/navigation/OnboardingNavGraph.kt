package com.lotusreichhart.gencanvas.feature.onboarding.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.navigation
import com.lotusreichhart.gencanvas.core.ui.navigation.genCanvasComposable
import com.lotusreichhart.gencanvas.core.ui.navigation.routes.GenCanvasRoute
import com.lotusreichhart.gencanvas.core.ui.navigation.routes.OnBoardingRoute
import com.lotusreichhart.gencanvas.feature.onboarding.presentation.OnboardingScreen

fun NavGraphBuilder.onboardingGraph(
    onNavigateToMain: () -> Unit
) {

    navigation(
        startDestination = OnBoardingRoute.ONBOARDING_SCREEN,
        route = GenCanvasRoute.ONBOARDING_FLOW_ROUTE
    ) {
        genCanvasComposable(route = OnBoardingRoute.ONBOARDING_SCREEN) {
            OnboardingScreen(
                onNavigateToMain = onNavigateToMain
            )
        }
    }
}