package com.lotusreichhart.gencanvas.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.lotusreichhart.gencanvas.feature.onboarding.navigation.onboardingGraph

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

            }
        )
    }
}