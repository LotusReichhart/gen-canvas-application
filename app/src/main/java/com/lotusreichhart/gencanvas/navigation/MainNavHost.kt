package com.lotusreichhart.gencanvas.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.lotusreichhart.core.navigation.Route
import com.lotusreichhart.gencanvas.presentation.MainScreen
import com.lotusreichhart.onboarding.navigation.onboardingGraph

@Composable
fun MainNavHost(
    navController: NavHostController,
    startDestination: String
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {

        // LUỒNG 1: Onboarding
        onboardingGraph(
            navController = navController,
            onNavigateToMain = {
                navController.navigate(Route.MAIN_FLOW_ROUTE) {
                    popUpTo(Route.ONBOARDING_FLOW_ROUTE) {
                        inclusive = true
                    }
                }
            }
        )

        // LUỒNG 2: Auth
//        authNavigationGraph(
//            navController = navController,
//            onLoginSuccess = {
//                navController.navigate(MAIN_FLOW_ROUTE) {
//                    popUpTo("auth_flow") {
//                        inclusive = true
//                    }
//                }
//            }
//        )

        composable(route = Route.MAIN_FLOW_ROUTE) {
            MainScreen()
        }
    }
}