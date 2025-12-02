package com.lotusreichhart.gencanvas.feature.home.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.navigation
import com.lotusreichhart.gencanvas.core.ui.navigation.genCanvasComposable
import com.lotusreichhart.gencanvas.core.ui.navigation.routes.HomeRoute
import com.lotusreichhart.gencanvas.core.ui.navigation.routes.MainTabRoute
import com.lotusreichhart.gencanvas.feature.home.presentation.HomeTab

fun NavGraphBuilder.homeTabGraph(
    onNavigateToAuth: () -> Unit,
    onNavigateToProfile: () -> Unit
) {

    navigation(
        startDestination = HomeRoute.HOME_TAB,
        route = MainTabRoute.HOME_TAB
    ) {
        genCanvasComposable(route = HomeRoute.HOME_TAB) {
            HomeTab(
                onNavigateToAuth = onNavigateToAuth,
                onNavigateToProfile = onNavigateToProfile
            )
        }
    }
}