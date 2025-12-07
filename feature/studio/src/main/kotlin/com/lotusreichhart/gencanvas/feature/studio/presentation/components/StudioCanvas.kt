package com.lotusreichhart.gencanvas.feature.studio.presentation.components

import android.net.Uri
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import coil3.compose.AsyncImage
import com.lotusreichhart.gencanvas.feature.studio.domain.model.StudioFeature
import com.lotusreichhart.gencanvas.feature.studio.domain.model.StudioStyle
import com.lotusreichhart.gencanvas.feature.studio.domain.model.StudioTool
import com.lotusreichhart.gencanvas.feature.studio.domain.model.edit.EditFeature
import com.lotusreichhart.gencanvas.feature.studio.domain.model.edit.crop.CropTool
import com.lotusreichhart.gencanvas.feature.studio.domain.model.edit.rotate.RotateTool
import com.lotusreichhart.gencanvas.feature.studio.presentation.components.edit.EditToolView

/**
 * Vùng hiển thị chính.
 * Quản lý Layering: Ảnh nền (Base) và Công cụ phủ (Overlay Tool).
 */
@Composable
internal fun StudioCanvas(
    modifier: Modifier = Modifier,
    currentImageUri: Uri?,
    activeFeature: StudioFeature?,
    activeTool: StudioTool?,
    activeStyle: StudioStyle?,
    shouldExecuteSave: Boolean,
    isImageTransitionAnimated: Boolean = true,
    onSaveSuccess: (Uri) -> Unit,
    onSaveError: (String?) -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Black),
        contentAlignment = Alignment.Center
    ) {
        AnimatedContent(
            targetState = currentImageUri,
            label = "ImageTransition",
            transitionSpec = {
                if (isImageTransitionAnimated) {
                    (fadeIn(animationSpec = tween(400)) +
                            scaleIn(initialScale = 0.85f, animationSpec = tween(400)))
                        .togetherWith(
                            fadeOut(animationSpec = tween(400))
                        )
                } else {
                    EnterTransition.None togetherWith ExitTransition.None
                }
            }
        ) { uri ->
            AsyncImage(
                model = uri,
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Fit
            )
        }

        AnimatedVisibility(
            visible = (activeFeature != null && currentImageUri != null),
            enter = fadeIn(animationSpec = tween(300)),
            exit = fadeOut(animationSpec = tween(300))
        ) {
            if (activeFeature == EditFeature) {
                when (activeTool) {
                    CropTool, RotateTool -> {
                        EditToolView(
                            imageUri = currentImageUri!!,
                            activeTool = activeTool,
                            activeStyle = activeStyle,
                            shouldExecuteCrop = shouldExecuteSave,
                            onCropSuccess = onSaveSuccess,
                            onCropError = { onSaveError(it?.message) }
                        )
                    }
                }
            }
        }
    }
}