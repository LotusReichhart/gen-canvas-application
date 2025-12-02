package com.lotusreichhart.gencanvas.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.lotusreichhart.gencanvas.core.ui.navigation.genCanvasComposable
import com.lotusreichhart.gencanvas.core.ui.navigation.routes.GenCanvasRoute
import com.lotusreichhart.gencanvas.feature.onboarding.navigation.onboardingGraph
import com.lotusreichhart.gencanvas.presentation.MainScreen

@Composable
fun MainNavHost(
    navController: NavHostController,
    startDestination: String
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {

        onboardingGraph(
            onNavigateToMain = {
                navController.navigate(GenCanvasRoute.MAIN_FLOW_ROUTE) {
                    popUpTo(GenCanvasRoute.ONBOARDING_FLOW_ROUTE) {
                        inclusive = true
                    }
                }
            }
        )

        genCanvasComposable(route = GenCanvasRoute.MAIN_FLOW_ROUTE) {
            MainScreen(
                onNavigateToAuth = {

                },
                onNavigateToProfile = {

                }
            )
        }

    }
}