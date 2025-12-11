package com.lotusreichhart.gencanvas.feature.studio.presentation

import android.app.Activity
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.lotusreichhart.gencanvas.feature.studio.presentation.components.StudioBottomBar
import com.lotusreichhart.gencanvas.feature.studio.presentation.components.StudioCanvas
import com.lotusreichhart.gencanvas.feature.studio.presentation.components.StudioTopBar

private val FIXED_BOTTOM_PADDING = 150.dp

@Composable
internal fun StudioScreen(
    viewModel: StudioViewModel = hiltViewModel(),
    onBack: () -> Unit,
    onSave: (Uri) -> Unit
) {
    val view = LocalView.current
    if (!view.isInEditMode) {
        DisposableEffect(Unit) {
            val window = (view.context as Activity).window
            val insetsController = WindowCompat.getInsetsController(window, view)

            insetsController.hide(WindowInsetsCompat.Type.statusBars())
            insetsController.systemBarsBehavior =
                WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE

            onDispose {
                insetsController.show(WindowInsetsCompat.Type.statusBars())
            }
        }
    }

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val currentImageUri by viewModel.currentImageUri.collectAsStateWithLifecycle()
    val canUndo by viewModel.canUndo.collectAsStateWithLifecycle()
    val canRedo by viewModel.canRedo.collectAsStateWithLifecycle()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    bottom = FIXED_BOTTOM_PADDING,
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
                onInteract = { hasInteracting -> viewModel.onUserInteraction(hasInteracting) },
                onSaveSuccess = { newUri -> viewModel.onNewImageApplied(newUri) },
                onSaveError = { errorMsg -> viewModel.onApplyError(errorMsg) }
            )
        }

        Box(
            modifier = Modifier.align(Alignment.TopCenter)
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