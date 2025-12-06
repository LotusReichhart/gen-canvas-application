package com.lotusreichhart.gencanvas.feature.studio.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.navigation
import androidx.navigation.navArgument
import com.lotusreichhart.gencanvas.core.ui.navigation.genCanvasComposable
import com.lotusreichhart.gencanvas.core.ui.navigation.routes.StudioRoute
import com.lotusreichhart.gencanvas.core.ui.navigation.routes.GenCanvasRoute
import com.lotusreichhart.gencanvas.feature.studio.presentation.StudioScreen

fun NavGraphBuilder.studioGraph(
    navController: NavController
) {
    navigation(
        route = GenCanvasRoute.STUDIO_FLOW_ROUTE,
        startDestination = StudioRoute.STUDIO_SCREEN
    ) {
        genCanvasComposable(
            route = StudioRoute.STUDIO_SCREEN,
            arguments = listOf(
                navArgument(StudioRoute.ARG_IMAGE_URI) { type = NavType.StringType }
            ),
        ) { backStackEntry ->
            val uriString = backStackEntry.arguments?.getString(StudioRoute.ARG_IMAGE_URI)

            if (uriString != null) {
                StudioScreen(
                    onBack = { navController.popBackStack() },
                    onSave = { resultUri ->
                        navController.previousBackStackEntry
                            ?.savedStateHandle
                            ?.set(StudioRoute.KEY_EDITED_IMAGE_RESULT, resultUri.toString())

                        navController.popBackStack()
                    }
                )
            } else {
                navController.popBackStack()
            }
        }
    }
}