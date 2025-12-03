package com.lotusreichhart.gencanvas.core.ui.navigation.routes

object GenCanvasRoute {
    const val ONBOARDING_FLOW_ROUTE = "onboarding_flow"
    const val MAIN_FLOW_ROUTE = "main_flow"
    const val ACCOUNT_FLOW_ROUTE = "account_flow"
    const val AUTH_FLOW_ROUTE = "auth_flow"

    // -----------------------Web view------------------------ //
    const val ARG_URL = "url"
    const val ARG_TITLE_RES_ID = "title_res_id"

    const val GEN_CANVAS_WEBVIEW = "webview/{$ARG_URL}/{$ARG_TITLE_RES_ID}"

    fun createWebViewRoute(url: String, titleResId: Int): String {
        return "webview/$url/$titleResId"
    }
    // --------------------------------------------------------- //
    // ----------------------Image viewer view----------------------- //
    const val IMAGE_VIEWER_VIEW = "image_viewer/{$ARG_URL}"

    fun createImageViewerRoute(url: String): String {
        return "image_viewer/$url"
    }
    // --------------------------------------------------------- //
}