package com.lotusreichhart.gencanvas.core.ui.navigation

object RouteTransition {
    // -------------------------Zoom List------------------------ //
    val ZOOM_TRANSITION_ROUTES = listOf(
        ""
//        AppRoute.IMAGE_VIEWER_SCREEN,
    )

    fun isZoomRoute(route: String?): Boolean {
        if (route == null) return false
        return ZOOM_TRANSITION_ROUTES.any { route.contains(it) }
    }
    // --------------------------------------------------------- //

    // -------------------------Slide vertical List------------------------ //
    val SLIDE_VERTICAL_TRANSITION_ROUTES = listOf(
        ""
//        AuthRoute.SIGN_IN_SCREEN,
//        AccountRoute.EDIT_PROFILE_SCREEN
    )

    fun isSlideVerticalRoute(route: String?): Boolean {
        if (route == null) return false
        return SLIDE_VERTICAL_TRANSITION_ROUTES.any { route.contains(it) }
    }
    // --------------------------------------------------------- //
}