package com.lotusreichhart.gencanvas.feature.editing.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.navigation
import androidx.navigation.navArgument
import com.lotusreichhart.gencanvas.core.ui.navigation.genCanvasComposable
import com.lotusreichhart.gencanvas.core.ui.navigation.routes.EditingRoute
import com.lotusreichhart.gencanvas.core.ui.navigation.routes.GenCanvasRoute
import com.lotusreichhart.gencanvas.feature.editing.presentation.EditorScreen

fun NavGraphBuilder.editingGraph(
    navController: NavController
) {
    navigation(
        route = GenCanvasRoute.EDITING_FLOW_ROUTE,
        startDestination = EditingRoute.EDITING_SCREEN
    ) {
        genCanvasComposable(
            route = EditingRoute.EDITING_SCREEN,
            arguments = listOf(
                navArgument(EditingRoute.ARG_IMAGE_URI) { type = NavType.StringType }
            ),
        ) { backStackEntry ->
            val uriString = backStackEntry.arguments?.getString(EditingRoute.ARG_IMAGE_URI)

            if (uriString != null) {
                EditorScreen(
                    onBack = { navController.popBackStack() },
                    onSave = { resultUri ->
                        navController.previousBackStackEntry
                            ?.savedStateHandle
                            ?.set("edited_image_result", resultUri.toString())

                        navController.popBackStack()
                    }
                )
            } else {
                navController.popBackStack()
            }
        }
    }
}