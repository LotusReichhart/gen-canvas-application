package com.lotusreichhart.gencanvas.feature.studio.presentation

import android.net.Uri
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.lotusreichhart.gencanvas.core.domain.repository.EditingRepository
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

    /** Map lưu trạng thái Style của từng Tool */
    private val toolStyleHistory = mutableMapOf<String, StudioStyle>()

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

    /**
     * Người dùng chọn một nhóm tính năng (VD: Bấm vào "Điều chỉnh")
     */
    fun onSelectFeature(feature: StudioFeature) {
        // Lấy danh sách tool của feature này
        val tools = feature.tools
        // Lấy tool mặc định
        val defaultTool = feature.defaultTool ?: tools.firstOrNull()

        // Lấy danh sách style và style mặc định cho tool đó
        val styles = defaultTool?.styles ?: emptyList()
        val defaultStyle = defaultTool?.defaultStyle

        _uiState.update {
            it.copy(
                activeFeature = feature,
                availableTools = tools,
                activeTool = defaultTool,
                availableStyles = styles,
                activeStyle = defaultStyle,
                shouldExecuteSave = false
            )
        }
    }

    /**
     * Người dùng chuyển sang công cụ khác trong cùng nhóm (VD: Từ Cắt sang Xoay)
     */
    fun onSelectTool(tool: StudioTool) {
        val currentTool = _uiState.value.activeTool
        val currentStyle = _uiState.value.activeStyle
        if (currentTool != null && currentStyle != null) {
            toolStyleHistory[currentTool.id] = currentStyle
        }

        val styles = tool.styles

        val savedStyle = toolStyleHistory[tool.id]
        val activeStyle = savedStyle ?: tool.defaultStyle

        _uiState.update {
            it.copy(
                activeTool = tool,
                availableStyles = styles,
                activeStyle = activeStyle
            )
        }
    }

    /**
     * Người dùng chọn một kiểu (VD: Tỉ lệ 1:1)
     */
    fun onSelectStyle(style: StudioStyle) {
        if (style is RotateStyle) {
            viewModelScope.launch {
                _uiState.update { it.copy(activeStyle = style) }
                delay(50)
                _uiState.update { it.copy(activeStyle = null) }
            }
        } else {
            _uiState.update { it.copy(activeStyle = style) }
            val currentTool = _uiState.value.activeTool
            if (currentTool != null) {
                toolStyleHistory[currentTool.id] = style
            }
        }
    }

    /**
     * Người dùng bấm nút "Hủy" (X) -> Thoát Feature, không lưu
     */
    fun onCancelFeature() {
        _uiState.update {
            it.copy(
                activeFeature = null,
                activeTool = null,
                activeStyle = null,
                shouldExecuteSave = false
            )
        }
    }

    /**
     * Người dùng bấm nút "Áp dụng" (V) -> Kích hoạt cờ hiệu để UI thực thi lệnh
     */
    fun onApplyRequest() {
        _uiState.update { it.copy(shouldExecuteSave = true) }
    }

    /**
     * UI (Component) báo cáo đã xử lý xong và có ảnh mới
     */
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