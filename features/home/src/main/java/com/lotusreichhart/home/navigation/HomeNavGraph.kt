package com.lotusreichhart.home.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.lotusreichhart.core.navigation.Route
import com.lotusreichhart.home.presentation.HomeScreen

fun NavGraphBuilder.homeGraph(
    navController: NavController,
    onNavigateToAuth: () -> Unit
) {

    navigation(
        startDestination = Route.HOME_SCREEN,
        route = Route.HOME_TAB_ROUTE
    ) {
        composable(Route.HOME_SCREEN) {
            HomeScreen(onNavigateToAuth = onNavigateToAuth)
        }
    }
}