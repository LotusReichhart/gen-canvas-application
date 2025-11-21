package com.lotusreichhart.gencanvas.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.navArgument
import com.lotusreichhart.auth.navigation.authGraph
import com.lotusreichhart.core.navigation.Route
import com.lotusreichhart.core.navigation.appComposable
import com.lotusreichhart.core.ui.screens.WebViewScreen
import com.lotusreichhart.gencanvas.presentation.MainScreen
import com.lotusreichhart.onboarding.navigation.onboardingGraph
import java.net.URLDecoder
import java.nio.charset.StandardCharsets

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
            navController = navController,
            onNavigateToMain = {
                navController.navigate(Route.MAIN_FLOW_ROUTE) {
                    popUpTo(Route.ONBOARDING_FLOW_ROUTE) {
                        inclusive = true
                    }
                }
            }
        )

        authGraph(
            navController = navController,
            onNavigateToMain = {
                navController.navigate(Route.MAIN_FLOW_ROUTE) {
                    popUpTo(Route.AUTH_FLOW_ROUTE) {
                        inclusive = true
                    }
                }
            }
        )

        appComposable(route = Route.MAIN_FLOW_ROUTE) {
            MainScreen(
                onNavigateToAuth = {
                    navController.navigate(Route.AUTH_FLOW_ROUTE)
                }
            )
        }

        appComposable(
            route = Route.WEBVIEW_SCREEN_ROUTE,
            arguments = listOf(
                navArgument(Route.ARG_URL) { type = NavType.StringType },
                navArgument(Route.ARG_TITLE) { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val encodedUrl = backStackEntry.arguments?.getString(Route.ARG_URL) ?: ""
            val title = backStackEntry.arguments?.getString(Route.ARG_TITLE) ?: ""

            val decodedUrl = try {
                URLDecoder.decode(encodedUrl, StandardCharsets.UTF_8.toString())
            } catch (e: Exception) {
                encodedUrl
            }

            WebViewScreen(
                url = decodedUrl,
                title = title,
                onBackClick = { navController.popBackStack() }
            )
        }
    }
}