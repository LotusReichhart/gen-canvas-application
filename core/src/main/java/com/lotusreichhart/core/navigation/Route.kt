package com.lotusreichhart.core.navigation

object Route {
    const val ONBOARDING_FLOW_ROUTE = "onboarding_flow"
    const val AUTH_FLOW_ROUTE = "auth_flow"
    const val MAIN_FLOW_ROUTE = "main_flow"

    // --------------------------------------------------------- //
    const val HOME_TAB_ROUTE = "home_tab_route"
    const val AI_GENERATE_TAB_ROUTE = "ai_generate_tab_route"
    const val CANVAS_TAB_ROUTE = "canvas_tab_route"
    const val ACCOUNT_TAB_ROUTE = "account_tab_route"

    // --------------------------------------------------------- //
    const val ONBOARDING_SCREEN = "onboarding_screen"
    const val HOME_SCREEN = "home_screen"

    const val SIGN_IN_SCREEN = "sign_in_screen"
    const val SIGN_UP_SCREEN = "sign_up_screen"
    const val FORGOT_PASSWORD_SCREEN = "forgot_password_screen"
    const val RESET_PASSWORD_SCREEN = "reset_password_screen"


    // --------------------------------------------------------- //
    const val VERIFY_OTP_SCREEN_BASE = "verify_otp_screen"
    const val ARG_FLOW_TYPE = "flow_type"
    const val VERIFY_OTP_SCREEN_ROUTE = "$VERIFY_OTP_SCREEN_BASE/{$ARG_FLOW_TYPE}"
    fun createVerifyOtpRoute(flowType: String): String {
        return "$VERIFY_OTP_SCREEN_BASE/$flowType"
    }
    // --------------------------------------------------------- //

    const val WEBVIEW_SCREEN_BASE = "webview_screen"
    const val ARG_URL = "url"
    const val ARG_TITLE = "title"

    const val WEBVIEW_SCREEN_ROUTE = "$WEBVIEW_SCREEN_BASE/{$ARG_URL}/{$ARG_TITLE}"

    fun createWebViewRoute(url: String, title: String): String {
        return "$WEBVIEW_SCREEN_BASE/$url/$title"
    }

    // --------------------------------------------------------- //
}