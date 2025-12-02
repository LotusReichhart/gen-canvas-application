package com.lotusreichhart.gencanvas.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp

@Composable
fun CutoutBottomAppBar(
    modifier: Modifier = Modifier,
    fabSize: Dp = 56.dp,
    fabMargin: Dp = 16.dp,
    notchDepth: Dp = 28.dp,
    color: Color = MaterialTheme.colorScheme.surfaceContainer,
    content: @Composable RowScope. () -> Unit
) {
    val density = LocalDensity.current

    val fabDiameterPx = with(LocalDensity.current) { fabSize.toPx() }
    val notchDepthPx = with(density) { notchDepth.toPx() }

    val cutoutShape = BottomAppBarCutoutShape(
        fabDiameter = fabDiameterPx,
        fabMargin = fabMargin,
        notchDepthPx = notchDepthPx
    )

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .height(64.dp),
        shape = cutoutShape,
        shadowElevation = 8.dp,
        color = color
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.Absolute.SpaceAround,
            verticalAlignment = Alignment.CenterVertically,
            content = content
        )
    }
}

private class BottomAppBarCutoutShape(
    private val fabDiameter: Float,
    private val fabMargin: Dp,
    private val notchDepthPx: Float
) : Shape {
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        val fabMarginPx = with(density) { fabMargin.toPx() }

        val notchRadius = fabDiameter / 2 + fabMarginPx
        val centerX = size.width / 2

        val notchDepth = notchDepthPx

        val path = Path().apply {
            moveTo(0f, 0f)
            lineTo(centerX - notchRadius, 0f)

            cubicTo(
                centerX - notchRadius * 0.65f, 0f,
                centerX - notchRadius * 0.65f, notchDepth,
                centerX, notchDepth
            )

            cubicTo(
                centerX + notchRadius * 0.65f, notchDepth,
                centerX + notchRadius * 0.65f, 0f,
                centerX + notchRadius, 0f
            )

            lineTo(size.width, 0f)
            lineTo(size.width, size.height)
            lineTo(0f, size.height)
            close()
        }

        return Outline.Generic(path)
    }
}