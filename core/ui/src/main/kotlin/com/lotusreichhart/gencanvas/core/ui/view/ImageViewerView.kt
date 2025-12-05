package com.lotusreichhart.gencanvas.core.ui.view

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.lotusreichhart.gencanvas.core.ui.components.GenCanvasIconButton
import com.lotusreichhart.gencanvas.core.ui.components.ZoomableAsyncImage
import com.lotusreichhart.gencanvas.core.ui.constant.Dimension

@Composable
fun ImageViewerView(
    imageUrl: String,
    onDismiss: () -> Unit
) {
    var isZoomed by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        ZoomableAsyncImage(
            imageUrl = imageUrl,
            modifier = Modifier.fillMaxSize(),
            onScaleChanged = { scale ->
                isZoomed = scale > 1.01f
            }
        )

        AnimatedVisibility(
            visible = !isZoomed,
            enter = fadeIn(),
            exit = fadeOut(),
            modifier = Modifier
                .align(Alignment.TopStart)
                .safeDrawingPadding()
                .padding(Dimension.Spacing.l)
        ) {
            GenCanvasIconButton(
                imageVector = Icons.Default.Close,
                onClick = onDismiss,
                tint = Color.White,
                contentDescription = "Close"
            )
        }
    }
}