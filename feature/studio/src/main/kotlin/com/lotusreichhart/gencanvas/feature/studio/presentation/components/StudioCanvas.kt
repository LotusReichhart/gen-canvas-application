package com.lotusreichhart.gencanvas.feature.studio.presentation.components

import android.net.Uri
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
import com.lotusreichhart.gencanvas.feature.studio.domain.model.filter.FilterFeature
import com.lotusreichhart.gencanvas.feature.studio.presentation.components.edit.EditToolView
import com.lotusreichhart.gencanvas.feature.studio.presentation.components.filter.FilterToolView

@Composable
internal fun StudioCanvas(
    modifier: Modifier = Modifier,
    currentImageUri: Uri?,
    activeFeature: StudioFeature?,
    activeTool: StudioTool?,
    activeStyle: StudioStyle?,
    shouldExecuteSave: Boolean,
    onInteract:(Boolean) -> Unit,
    onSaveSuccess: (Uri) -> Unit,
    onSaveError: (String?) -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Black),
        contentAlignment = Alignment.Center
    ) {

        AsyncImage(
            model = currentImageUri,
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Fit
        )

        when (activeFeature) {
            EditFeature -> {
                when (activeTool) {
                    CropTool, RotateTool -> {
                        EditToolView(
                            imageUri = currentImageUri!!,
                            activeStyle = activeStyle,
                            shouldExecuteEdit = shouldExecuteSave,
                            onInteract = onInteract,
                            onEditSuccess = onSaveSuccess,
                            onEditError = { onSaveError(it?.message) }
                        )
                    }

                }
            }

            FilterFeature -> {
                FilterToolView(
                    imageUri = currentImageUri!!,
                    activeStyle = activeStyle,
                    shouldExecuteFilter = shouldExecuteSave,
                    onInteract = onInteract,
                    onFilterSuccess = onSaveSuccess,
                    onFilterError = { onSaveError(it?.message) }
                )
            }

            else -> {}
        }
    }
}