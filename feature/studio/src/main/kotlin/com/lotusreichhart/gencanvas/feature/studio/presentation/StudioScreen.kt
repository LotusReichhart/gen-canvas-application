package com.lotusreichhart.gencanvas.feature.studio.presentation

import android.net.Uri
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.lotusreichhart.gencanvas.feature.studio.presentation.components.StudioBottomBar
import com.lotusreichhart.gencanvas.feature.studio.presentation.components.StudioCanvas
import com.lotusreichhart.gencanvas.feature.studio.presentation.components.StudioTopBar

private val FIXED_TOP_PADDING = 90.dp
private val FIXED_BOTTOM_PADDING = 130.dp

@Composable
internal fun StudioScreen(
    viewModel: StudioViewModel = hiltViewModel(),
    onBack: () -> Unit,
    onSave: (Uri) -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val currentImageUri by viewModel.currentImageUri.collectAsStateWithLifecycle()
    val canUndo by viewModel.canUndo.collectAsStateWithLifecycle()
    val canRedo by viewModel.canRedo.collectAsStateWithLifecycle()

    val isEditing = uiState.activeFeature != null

    val targetExtraPadding = if (isEditing) 20.dp else 0.dp

    val animatedExtraPadding by animateDpAsState(
        targetValue = targetExtraPadding,
        animationSpec = tween(durationMillis = 350, easing = FastOutSlowInEasing),
        label = "WorkspacePadding"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    top = FIXED_TOP_PADDING + animatedExtraPadding,
                    bottom = FIXED_BOTTOM_PADDING + animatedExtraPadding,
                    start = animatedExtraPadding,
                    end = animatedExtraPadding
                ),
            contentAlignment = Alignment.Center
        ) {
            StudioCanvas(
                modifier = Modifier.fillMaxSize(),
                currentImageUri = currentImageUri,
                activeFeature = uiState.activeFeature,
                activeTool = uiState.activeTool,
                activeStyle = uiState.activeStyle,
                shouldExecuteSave = uiState.shouldExecuteSave,
                isImageTransitionAnimated = uiState.isImageTransitionAnimated,
                onSaveSuccess = { newUri -> viewModel.onNewImageApplied(newUri) },
                onSaveError = { errorMsg -> viewModel.onApplyError(errorMsg) }
            )
        }

        Box(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .statusBarsPadding()
        ) {
            StudioTopBar(
                isVisible = (uiState.activeFeature == null),
                canUndo = canUndo,
                canRedo = canRedo,
                onBack = onBack,
                onUndo = viewModel::onUndo,
                onRedo = viewModel::onRedo,
                onSaveFinal = { currentImageUri?.let { onSave(it) } }
            )
        }

        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .navigationBarsPadding()
        ) {
            StudioBottomBar(
                uiState = uiState,
                onSelectFeature = viewModel::onSelectFeature,
                onCancelFeature = viewModel::onCancelFeature,
                onApplyFeature = viewModel::onApplyRequest,
                onSelectTool = viewModel::onSelectTool,
                onSelectStyle = viewModel::onSelectStyle
            )
        }
    }
}