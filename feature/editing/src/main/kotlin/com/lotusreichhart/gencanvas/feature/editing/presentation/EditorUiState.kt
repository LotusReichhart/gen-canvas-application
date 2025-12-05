package com.lotusreichhart.gencanvas.feature.editing.presentation

import com.lotusreichhart.gencanvas.feature.editing.domain.model.EditorFeature
import com.lotusreichhart.gencanvas.feature.editing.domain.model.EditorTool
import com.lotusreichhart.gencanvas.feature.editing.domain.model.ToolStyle

internal data class EditorUiState(
    // --- TRẠNG THÁI ĐIỀU KHIỂN (NAVIGATION STATE) ---
    /** Tính năng đang chọn (VD: ADJUST). Null = Đang ở Menu chính */
    val activeFeature: EditorFeature? = null,

    /** Công cụ đang chọn trong tính năng đó (VD: CROP) */
    val activeTool: EditorTool? = null,

    /** Kiểu/Style đang chọn của công cụ đó (VD: 1:1) */
    val activeStyle: ToolStyle? = null,

    // --- DANH SÁCH DỮ LIỆU (DATA LISTS) ---
    /** Danh sách các Feature hiển thị ở Menu chính */
    val availableFeatures: List<EditorFeature> = emptyList(),

    /** Danh sách Tool hiển thị khi chọn Feature */
    val availableTools: List<EditorTool> = emptyList(),

    /** Danh sách Style hiển thị khi chọn Tool */
    val availableStyles: List<ToolStyle> = emptyList(),

    // --- TRẠNG THÁI HỆ THỐNG ---
    /** Flag để báo cho UI biết cần thực hiện hành động Lưu (Cắt/Apply Filter...) */
    val shouldExecuteSave: Boolean = false,

    val isImageTransitionAnimated: Boolean = false,

    val isLoading: Boolean = false,
    val error: String? = null
)
