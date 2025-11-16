package com.lotusreichhart.onboarding.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.lotusreichhart.core.navigation.Route
import com.lotusreichhart.onboarding.presentation.OnboardingScreen

fun NavGraphBuilder.onboardingGraph(
    navController: NavController,
    onNavigateToMain: () -> Unit
) {

    navigation(
        startDestination = Route.ONBOARDING_SCREEN,
        route = Route.ONBOARDING_FLOW_ROUTE
    ) {
        composable(Route.ONBOARDING_SCREEN) {
            OnboardingScreen(
                onNavigateToMain = onNavigateToMain
            )
        }
    }
}