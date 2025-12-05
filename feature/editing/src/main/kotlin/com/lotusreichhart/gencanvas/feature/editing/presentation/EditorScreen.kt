package com.lotusreichhart.gencanvas.feature.editing.presentation

import android.net.Uri
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
import com.lotusreichhart.gencanvas.feature.editing.presentation.components.EditorBottomBar
import com.lotusreichhart.gencanvas.feature.editing.presentation.components.EditorCanvas
import com.lotusreichhart.gencanvas.feature.editing.presentation.components.EditorTopBar

private val FIXED_TOP_PADDING = 110.dp
private val FIXED_BOTTOM_PADDING = 150.dp

@Composable
internal fun EditorScreen(
    viewModel: EditorViewModel = hiltViewModel(),
    onBack: () -> Unit,
    onSave: (Uri) -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val currentImageUri by viewModel.currentImageUri.collectAsStateWithLifecycle()
    val canUndo by viewModel.canUndo.collectAsStateWithLifecycle()
    val canRedo by viewModel.canRedo.collectAsStateWithLifecycle()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        EditorCanvas(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    top = FIXED_TOP_PADDING,
                    bottom = FIXED_BOTTOM_PADDING
                ),
            currentImageUri = currentImageUri,
            activeFeature = uiState.activeFeature,
            activeTool = uiState.activeTool,
            activeStyle = uiState.activeStyle,
            shouldExecuteSave = uiState.shouldExecuteSave,
            isImageTransitionAnimated = uiState.isImageTransitionAnimated,
            onSaveSuccess = { newUri -> viewModel.onNewImageApplied(newUri) },
            onSaveError = { errorMsg -> viewModel.onApplyError(errorMsg) }
        )

        Box(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .statusBarsPadding()
        ) {
            EditorTopBar(
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
            EditorBottomBar(
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