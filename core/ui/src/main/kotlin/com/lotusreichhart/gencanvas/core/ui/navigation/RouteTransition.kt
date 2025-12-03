package com.lotusreichhart.gencanvas.core.ui.navigation

import com.lotusreichhart.gencanvas.core.ui.navigation.routes.AuthRoute
import com.lotusreichhart.gencanvas.core.ui.navigation.routes.GenCanvasRoute

object RouteTransition {
    // -------------------------Zoom List------------------------ //
    val ZOOM_TRANSITION_ROUTES = listOf(
        GenCanvasRoute.IMAGE_VIEWER_VIEW
    )

    fun isZoomRoute(route: String?): Boolean {
        if (route == null) return false
        return ZOOM_TRANSITION_ROUTES.any { route.contains(it) }
    }
    // --------------------------------------------------------- //

    // -------------------------Slide vertical List------------------------ //
    val SLIDE_VERTICAL_TRANSITION_ROUTES = listOf(
        AuthRoute.SIGN_IN_SCREEN,
        AuthRoute.VERIFY_OTP_SCREEN,
        AuthRoute.RESET_PASSWORD_SCREEN
//        AccountRoute.EDIT_PROFILE_SCREEN
    )

    fun isSlideVerticalRoute(route: String?): Boolean {
        if (route == null) return false
        return SLIDE_VERTICAL_TRANSITION_ROUTES.any { route.contains(it) }
    }
    // --------------------------------------------------------- //
}