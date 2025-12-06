package com.lotusreichhart.gencanvas.feature.studio.domain.util

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

internal fun createAspectRatioIcon(ratioX: Float, ratioY: Float): ImageVector {
    val size = 24f
    val padding = 2f
    val cornerRadius = 2f
    val availableSize = size - (padding * 2)

    val ratio = ratioX / ratioY

    var w = availableSize
    var h = availableSize

    if (ratio > 1) {
        h = w / ratio
    } else {
        w = h * ratio
    }

    val left = (size - w) / 2
    val top = (size - h) / 2
    val right = left + w
    val bottom = top + h

    return ImageVector.Builder(
        name = "Ratio_${ratioX}_${ratioY}",
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 24f,
        viewportHeight = 24f
    ).apply {
        path(
            fill = null,
            fillAlpha = 1f,
            stroke = SolidColor(Color.White),
            strokeAlpha = 1f,
            strokeLineWidth = 2f,
            strokeLineCap = StrokeCap.Round,
            strokeLineJoin = StrokeJoin.Round,
            pathFillType = PathFillType.NonZero
        ) {
            moveTo(left + cornerRadius, top)

            lineTo(right - cornerRadius, top)
            arcTo(
                horizontalEllipseRadius = cornerRadius,
                verticalEllipseRadius = cornerRadius,
                theta = 270f,
                isMoreThanHalf = false,
                isPositiveArc = true,
                x1 = right,
                y1 = top + cornerRadius
            )

            lineTo(right, bottom - cornerRadius)
            arcTo(
                horizontalEllipseRadius = cornerRadius,
                verticalEllipseRadius = cornerRadius,
                theta = 0f,
                isMoreThanHalf = false,
                isPositiveArc = true,
                x1 = right - cornerRadius,
                y1 = bottom
            )

            lineTo(left + cornerRadius, bottom)
            arcTo(
                horizontalEllipseRadius = cornerRadius,
                verticalEllipseRadius = cornerRadius,
                theta = 90f,
                isMoreThanHalf = false,
                isPositiveArc = true,
                x1 = left,
                y1 = bottom - cornerRadius
            )

            lineTo(left, top + cornerRadius)
            arcTo(
                horizontalEllipseRadius = cornerRadius,
                verticalEllipseRadius = cornerRadius,
                theta = 180f,
                isMoreThanHalf = false,
                isPositiveArc = true,
                x1 = left + cornerRadius,
                y1 = top
            )

            close()
        }
    }.build()
}