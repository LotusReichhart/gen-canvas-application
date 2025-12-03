package com.lotusreichhart.gencanvas.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.navArgument
import com.lotusreichhart.gencanvas.core.ui.navigation.genCanvasComposable
import com.lotusreichhart.gencanvas.core.ui.navigation.routes.GenCanvasRoute
import com.lotusreichhart.gencanvas.core.ui.view.GenCanvasWebView
import com.lotusreichhart.gencanvas.feature.auth.navigation.authGraph
import com.lotusreichhart.gencanvas.feature.onboarding.navigation.onboardingGraph
import com.lotusreichhart.gencanvas.presentation.MainScreen
import java.net.URLDecoder
import java.nio.charset.StandardCharsets
import com.lotusreichhart.gencanvas.core.common.R
import com.lotusreichhart.gencanvas.core.common.util.TextResource
import com.lotusreichhart.gencanvas.core.ui.navigation.routes.AccountRoute
import com.lotusreichhart.gencanvas.core.ui.view.ImageViewerView
import com.lotusreichhart.gencanvas.feature.account.navigation.accountGraph

@Composable
fun MainNavHost(
    navController: NavHostController,
    startDestination: String,
    onShowExitSnackBar: (TextResource) -> Unit
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
                    navController.navigate(GenCanvasRoute.AUTH_FLOW_ROUTE)
                },
                onNavigateToProfile = {
                    navController.navigate(AccountRoute.PROFILE_SCREEN)
                },
                onShowExitSnackBar = onShowExitSnackBar
            )
        }

        authGraph(
            navController = navController,
            isAuthSuccessful = {
                navController.popBackStack(GenCanvasRoute.AUTH_FLOW_ROUTE, inclusive = true)
            }
        )

        accountGraph(
            navController = navController
        )

        genCanvasComposable(
            route = GenCanvasRoute.GEN_CANVAS_WEBVIEW,
            arguments = listOf(
                navArgument(GenCanvasRoute.ARG_URL) { type = NavType.StringType },
                navArgument(GenCanvasRoute.ARG_TITLE_RES_ID) { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val encodedUrl = backStackEntry.arguments?.getString(GenCanvasRoute.ARG_URL) ?: ""
            val titleResId = backStackEntry.arguments?.getInt(GenCanvasRoute.ARG_TITLE_RES_ID)
                ?: R.string.core_label_terms_of_service

            val decodedUrl = try {
                URLDecoder.decode(encodedUrl, StandardCharsets.UTF_8.toString())
            } catch (e: Exception) {
                encodedUrl
            }

            GenCanvasWebView(
                url = decodedUrl,
                titleResId = titleResId,
                onDismiss = { navController.popBackStack() }
            )
        }

        genCanvasComposable(
            route = GenCanvasRoute.IMAGE_VIEWER_VIEW,
            arguments = listOf(navArgument(GenCanvasRoute.ARG_URL) { type = NavType.StringType })
        ) { backStackEntry ->
            val encodedUrl = backStackEntry.arguments?.getString(GenCanvasRoute.ARG_URL) ?: ""

            val decodedUrl = try {
                URLDecoder.decode(encodedUrl, StandardCharsets.UTF_8.toString())
            } catch (e: Exception) {
                encodedUrl
            }

            ImageViewerView(
                imageUrl = decodedUrl,
                onDismiss = { navController.popBackStack() }
            )
        }
    }
}