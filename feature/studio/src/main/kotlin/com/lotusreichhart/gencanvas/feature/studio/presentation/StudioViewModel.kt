package com.lotusreichhart.gencanvas.feature.studio.presentation

import android.net.Uri
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.lotusreichhart.gencanvas.core.ui.navigation.routes.StudioRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.net.URLDecoder
import java.nio.charset.StandardCharsets
import javax.inject.Inject
import androidx.core.net.toUri
import androidx.lifecycle.viewModelScope
import com.lotusreichhart.gencanvas.feature.studio.domain.model.StudioConfig
import com.lotusreichhart.gencanvas.feature.studio.domain.model.StudioFeature
import com.lotusreichhart.gencanvas.feature.studio.domain.model.StudioStyle
import com.lotusreichhart.gencanvas.feature.studio.domain.model.StudioTool
import com.lotusreichhart.gencanvas.feature.studio.domain.model.edit.rotate.RotateStyle
import com.lotusreichhart.gencanvas.feature.studio.domain.repository.EditingRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber

@HiltViewModel
internal class StudioViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val editingRepository: EditingRepository,
    private val studioConfig: StudioConfig
) : ViewModel() {
    private val _uiState = MutableStateFlow(StudioUiState())
    val uiState: StateFlow<StudioUiState> = _uiState.asStateFlow()
    val currentImageUri = editingRepository.currentImageUri
    val canUndo = editingRepository.canUndo
    val canRedo = editingRepository.canRedo

    init {
        val uriString: String? = savedStateHandle[StudioRoute.ARG_IMAGE_URI]
        uriString?.let {
            try {
                val decoded = URLDecoder.decode(it, StandardCharsets.UTF_8.toString())
                val uri = decoded.toUri()
                editingRepository.initializeSession(uri)
            } catch (e: Exception) {
                _uiState.update { s -> s.copy(error = "Lỗi đọc ảnh ban đầu") }
                Timber.e(e, "Lỗi đọc ảnh ban đầu")
            }
        }

        _uiState.update {
            it.copy(availableFeatures = studioConfig.features)
        }
    }

    fun onSelectFeature(feature: StudioFeature) {
        val tools = feature.tools
        val defaultTool = feature.defaultTool ?: tools.firstOrNull()

        val styles = defaultTool?.styles ?: emptyList()
        val defaultStyle = defaultTool?.defaultStyle

        _uiState.update {
            it.copy(
                toolStyleHistory = emptyMap(),
                activeFeature = feature,
                availableTools = tools,
                activeTool = defaultTool,
                availableStyles = styles,
                activeStyle = defaultStyle,
                shouldExecuteSave = false
            )
        }
    }

    fun onSelectTool(tool: StudioTool) {
        val styles = tool.styles

        val savedStyle = _uiState.value.toolStyleHistory[tool]
        val activeStyle = savedStyle ?: tool.defaultStyle

        _uiState.update {
            it.copy(
                activeTool = tool,
                availableStyles = styles,
                activeStyle = activeStyle
            )
        }
    }

    fun onSelectStyle(style: StudioStyle) {
        if (style is RotateStyle) {
            viewModelScope.launch {
                _uiState.update { it.copy(activeStyle = style) }
                delay(50)
                _uiState.update { it.copy(activeStyle = null) }
            }
        } else {
            _uiState.update { state ->
                var newHistory = state.toolStyleHistory
                val currentTool = state.activeTool
                if (currentTool != null) {
                    newHistory = newHistory + (currentTool to style)
                }

                state.copy(
                    activeStyle = style,
                    toolStyleHistory = newHistory
                )
            }
        }
    }

    fun onCancelFeature() {
        _uiState.update {
            it.copy(
                toolStyleHistory = emptyMap(),
                activeFeature = null,
                activeTool = null,
                activeStyle = null,
                shouldExecuteSave = false
            )
        }
    }

    fun onApplyRequest() {
        _uiState.update { it.copy(shouldExecuteSave = true) }
    }

    fun onNewImageApplied(uri: Uri) {
        _uiState.update { it.copy(isImageTransitionAnimated = true) }
        editingRepository.updateImage(uri)

        _uiState.update {
            it.copy(
                activeFeature = null,
                activeTool = null,
                shouldExecuteSave = false,
                isLoading = false
            )
        }
    }

    fun onApplyError(msg: String?) {
        _uiState.update {
            it.copy(
                shouldExecuteSave = false,
                error = msg ?: "Lỗi không xác định khi lưu ảnh"
            )
        }
        Timber.e("onApplyError: $msg")
    }

    fun onUndo() {
        _uiState.update { it.copy(isImageTransitionAnimated = false) }
        editingRepository.undo()
    }

    fun onRedo() {
        _uiState.update { it.copy(isImageTransitionAnimated = false) }
        editingRepository.redo()
    }

    fun clearError() {
        _uiState.update { it.copy(error = null) }
    }
}