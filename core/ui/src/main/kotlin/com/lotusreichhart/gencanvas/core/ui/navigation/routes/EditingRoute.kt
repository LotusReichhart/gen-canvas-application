package com.lotusreichhart.gencanvas.core.ui.navigation.routes

import android.net.Uri
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

object EditingRoute {
    const val ARG_IMAGE_URI = "image_uri"
    const val EDITING_SCREEN = "editing_screen/{$ARG_IMAGE_URI}"

    fun createEditingRoute(uri: Uri): String {
        val encodedUri = URLEncoder.encode(uri.toString(), StandardCharsets.UTF_8.toString())
        return "editing_screen/$encodedUri"
    }
}