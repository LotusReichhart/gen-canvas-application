package com.lotusreichhart.gencanvas.core.ui.util

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.IntSize
import kotlin.math.PI
import kotlin.math.tan

fun Modifier.shimmerEffect(
    baseColor: Color? = null,
    highlightColor: Color? = null
): Modifier = composed {
    val base = baseColor ?: MaterialTheme.colorScheme.surfaceContainer
    val highlight = highlightColor ?: MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)

    var size by remember { mutableStateOf(IntSize.Zero) }
    val transition = rememberInfiniteTransition(label = "Shimmer")

    val gradientWidth = 1000f

    val startOffsetX by transition.animateFloat(
        initialValue = -gradientWidth,
        targetValue = size.width.toFloat() + gradientWidth,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 1500,
                easing = LinearEasing
            ),
            repeatMode = RepeatMode.Restart
        ),
        label = "ShimmerOffsetX"
    )

    val angleInRadians = 60f * (PI / 180f).toFloat()
    val xOffset = if (size.height > 0) {
        size.height.toFloat() * tan(angleInRadians)
    } else {
        0f
    }

    background(
        brush = Brush.linearGradient(
            colors = listOf(
                base,
                highlight,
                base
            ),
            start = Offset(startOffsetX, 0f),
            end = Offset(startOffsetX + xOffset, size.height.toFloat())
        )
    ).onGloballyPositioned {
        size = it.size
    }
}