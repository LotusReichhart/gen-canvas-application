package com.lotusreichhart.gencanvas.feature.account.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.navigation
import com.lotusreichhart.gencanvas.core.ui.navigation.genCanvasComposable
import com.lotusreichhart.gencanvas.core.ui.navigation.routes.AccountRoute
import com.lotusreichhart.gencanvas.core.ui.navigation.routes.MainTabRoute
import com.lotusreichhart.gencanvas.feature.account.presentation.AccountTab

fun NavGraphBuilder.accountTabGraph(
    onNavigateToAuth: () -> Unit,
    onNavigateToProfile: () -> Unit
) {
    navigation(
        startDestination = AccountRoute.ACCOUNT_TAB,
        route = MainTabRoute.ACCOUNT_TAB_ROUTE
    ) {
        genCanvasComposable(route = AccountRoute.ACCOUNT_TAB) {
            AccountTab(
                onNavigateToAuth = onNavigateToAuth,
                onNavigateToProfile = onNavigateToProfile
            )
        }
    }
}