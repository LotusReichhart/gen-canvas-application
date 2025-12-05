package com.lotusreichhart.gencanvas.core.ui.constant

import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

object Dimension {

    object Spacing {
        val none = 0.dp
        val xxs = 2.dp
        val xs = 4.dp
        val s = 8.dp
        val m = 12.dp
        val l = 16.dp
        val xl = 24.dp
        val xxl = 32.dp
        val xxxl = 48.dp
        val giant = 64.dp
    }

    object Radius {
        val none = 0.dp
        val xs = 4.dp
        val s = 8.dp
        val m = 12.dp
        val l = 16.dp
        val xl = 28.dp
        val full = 100.dp
    }

    object Icon {
        val xs = 12.dp
        val s = 16.dp
        val m = 24.dp
        val l = 32.dp
        val xl = 48.dp
        val xxl = 64.dp
    }

    object TextSize {
        val labelSmall = 11.sp
        val labelMedium = 12.sp
        val labelLarge = 14.sp

        val bodySmall = 12.sp
        val bodyMedium = 14.sp
        val bodyLarge = 16.sp

        val titleSmall = 14.sp
        val titleMedium = 16.sp
        val titleLarge = 22.sp

        val headlineSmall = 24.sp
        val headlineMedium = 28.sp
        val headlineLarge = 32.sp
    }

    val minTouchTarget = 48.dp
}