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
import com.lotusreichhart.gencanvas.feature.studio.domain.model.StyleGroupFeature
import com.lotusreichhart.gencanvas.feature.studio.domain.model.ToolGroupFeature
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
        when (feature) {
            is ToolGroupFeature -> {
                val tools = feature.tools
                val defaultTool = feature.defaultTool ?: tools.firstOrNull()

                val styles = defaultTool?.styles ?: emptyList()
                val savedStyle = _uiState.value.toolStyleHistory[defaultTool]
                val activeStyle = savedStyle ?: defaultTool?.defaultStyle

                _uiState.update {
                    it.copy(
                        activeFeature = feature,
                        availableTools = tools,
                        activeTool = defaultTool,
                        availableStyles = styles,
                        activeStyle = activeStyle,
                        shouldExecuteSave = false
                    )
                }
            }

            is StyleGroupFeature -> {
                val styles = feature.styles

                val savedStyle = _uiState.value.featureStyleHistory[feature.id]
                val activeStyle = savedStyle ?: feature.defaultStyle

                _uiState.update {
                    it.copy(
                        activeFeature = feature,
                        availableTools = emptyList(),
                        activeTool = null,
                        availableStyles = styles,
                        activeStyle = activeStyle,
                        shouldExecuteSave = false
                    )
                }
            }
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
                delay(50) // Hiệu ứng click
                _uiState.update { it.copy(activeStyle = null) }
            }
            return
        }

        _uiState.update { state ->
            val currentFeature = state.activeFeature
            val currentTool = state.activeTool

            var newToolHistory = state.toolStyleHistory
            var newFeatureHistory = state.featureStyleHistory

            if (currentTool != null) {
                newToolHistory = newToolHistory + (currentTool to style)
            } else if (currentFeature != null) {
                newFeatureHistory = newFeatureHistory + (currentFeature.id to style)
            }

            state.copy(
                activeStyle = style,
                toolStyleHistory = newToolHistory,
                featureStyleHistory = newFeatureHistory
            )
        }
    }

    fun onCancelFeature() {
        _uiState.update {
            it.copy(
                toolStyleHistory = emptyMap(),
                featureStyleHistory = emptyMap(),
                activeFeature = null,
                activeTool = null,
                activeStyle = null,
                shouldExecuteSave = false,
                availableTools = emptyList(),
                availableStyles = emptyList()
            )
        }
    }

    fun onUserInteraction(hasInteracting: Boolean = false) {
        _uiState.update { it.copy(hasUserInteracted = hasInteracting) }
    }

    fun onApplyRequest() {
        if (_uiState.value.hasUserInteracted) {
            _uiState.update {
                it.copy(
                    shouldExecuteSave = true,
                )
            }
        } else {
            onCancelFeature()
        }
    }

    fun onNewImageApplied(uri: Uri) {
        editingRepository.updateImage(uri)

        _uiState.update {
            it.copy(
                toolStyleHistory = emptyMap(),
                featureStyleHistory = emptyMap(),
                activeFeature = null,
                activeTool = null,
                activeStyle = null,
                shouldExecuteSave = false,
                availableTools = emptyList(),
                availableStyles = emptyList(),
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
        editingRepository.undo()
    }

    fun onRedo() {
        editingRepository.redo()
    }

    fun clearError() {
        _uiState.update { it.copy(error = null) }
    }
}