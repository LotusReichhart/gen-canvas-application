package com.lotusreichhart.gencanvas.core.ui.navigation.routes

object AuthRoute {
    const val SIGN_IN_SCREEN = "sign_in_screen"
    const val SIGN_UP_SCREEN = "sign_up_screen"
    const val FORGOT_PASSWORD_SCREEN = "forgot_password_screen"
    const val RESET_PASSWORD_SCREEN = "reset_password_screen"

    // Verify OTP
    const val ARG_FLOW_TYPE = "flow_type"
    const val VERIFY_OTP_SCREEN = "verify_otp_screen/{$ARG_FLOW_TYPE}"

    fun createVerifyOtpRoute(flowType: String): String {
        return "verify_otp_screen/$flowType"
    }
}