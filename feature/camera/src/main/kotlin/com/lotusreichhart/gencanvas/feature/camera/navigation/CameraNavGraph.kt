package com.lotusreichhart.gencanvas.feature.camera.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.navigation
import com.lotusreichhart.gencanvas.core.ui.navigation.genCanvasComposable
import com.lotusreichhart.gencanvas.core.ui.navigation.routes.CameraRoute
import com.lotusreichhart.gencanvas.core.ui.navigation.routes.GenCanvasRoute
import com.lotusreichhart.gencanvas.core.ui.navigation.routes.StudioRoute
import com.lotusreichhart.gencanvas.feature.camera.presentation.CameraScreen


fun NavGraphBuilder.cameraGraph(
    navController: NavController
) {
    navigation(
        route = GenCanvasRoute.CAMERA_FLOW_ROUTE,
        startDestination = CameraRoute.CAMERA_SCREEN
    ) {
        genCanvasComposable(
            route = CameraRoute.CAMERA_SCREEN,
        ) {
            CameraScreen(
                onImageCaptured = { uri ->
                    navController.navigate(StudioRoute.createStudioRoute(uri)) {
                        popUpTo(CameraRoute.CAMERA_SCREEN) {
                            inclusive = true
                        }
                    }
                },
                onClose = { navController.popBackStack() }
            )
        }
    }
}