package com.lotusreichhart.gencanvas.feature.editing.presentation

import android.net.Uri
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.lotusreichhart.gencanvas.core.domain.repository.EditingRepository
import com.lotusreichhart.gencanvas.core.ui.navigation.routes.EditingRoute
import com.lotusreichhart.gencanvas.feature.editing.domain.factory.EditorConfigFactory
import com.lotusreichhart.gencanvas.feature.editing.domain.model.EditorFeature
import com.lotusreichhart.gencanvas.feature.editing.domain.model.EditorTool
import com.lotusreichhart.gencanvas.feature.editing.domain.model.ToolStyle
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.net.URLDecoder
import java.nio.charset.StandardCharsets
import javax.inject.Inject
import androidx.core.net.toUri
import timber.log.Timber

@HiltViewModel
internal class EditorViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val editingRepository: EditingRepository,
    private val configFactory: EditorConfigFactory
) : ViewModel() {
    private val _uiState = MutableStateFlow(EditorUiState())
    val uiState: StateFlow<EditorUiState> = _uiState.asStateFlow()

    val currentImageUri = editingRepository.currentImageUri
    val canUndo = editingRepository.canUndo
    val canRedo = editingRepository.canRedo

    init {
        val uriString: String? = savedStateHandle[EditingRoute.ARG_IMAGE_URI]
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
            it.copy(availableFeatures = configFactory.getAvailableFeatures())
        }
    }

    /**
     * Người dùng chọn một nhóm tính năng (VD: Bấm vào "Điều chỉnh")
     */
    fun onSelectFeature(feature: EditorFeature) {
        // Lấy danh sách tool của feature này
        val tools = configFactory.getToolsForFeature(feature)
        // Lấy tool mặc định (VD: CROP)
        val defaultTool = configFactory.getDefaultTool(feature) ?: tools.firstOrNull()

        // Lấy danh sách style và style mặc định cho tool đó
        val styles = defaultTool?.let { configFactory.getStylesForTool(it) } ?: emptyList()
        val defaultStyle = defaultTool?.let { configFactory.getDefaultStyle(it) }

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
    fun onSelectTool(tool: EditorTool) {
        val styles = configFactory.getStylesForTool(tool)
        val defaultStyle = configFactory.getDefaultStyle(tool)

        _uiState.update {
            it.copy(
                activeTool = tool,
                availableStyles = styles,
                activeStyle = defaultStyle
            )
        }
    }

    /**
     * Người dùng chọn một kiểu (VD: Tỉ lệ 1:1)
     */
    fun onSelectStyle(style: ToolStyle) {
        _uiState.update { it.copy(activeStyle = style) }
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