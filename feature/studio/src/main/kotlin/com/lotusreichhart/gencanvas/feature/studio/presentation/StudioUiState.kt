package com.lotusreichhart.gencanvas.feature.studio.presentation

import com.lotusreichhart.gencanvas.feature.studio.domain.model.StudioFeature
import com.lotusreichhart.gencanvas.feature.studio.domain.model.StudioStyle
import com.lotusreichhart.gencanvas.feature.studio.domain.model.StudioTool

internal data class StudioUiState(
    // --- TRẠNG THÁI ĐIỀU KHIỂN (NAVIGATION STATE) ---
    /** Tính năng đang chọn (VD: ADJUST). Null = Đang ở Menu chính */
    val activeFeature: StudioFeature? = null,

    /** Công cụ đang chọn trong tính năng đó (VD: CROP) */
    val activeTool: StudioTool? = null,

    /** Kiểu/Style đang chọn của công cụ đó (VD: 1:1) */
    val activeStyle: StudioStyle? = null,

    // --- DANH SÁCH DỮ LIỆU (DATA LISTS) ---
    /** Danh sách các Feature hiển thị ở Menu chính */
    val availableFeatures: List<StudioFeature> = emptyList(),

    /** Danh sách Tool hiển thị khi chọn Feature */
    val availableTools: List<StudioTool> = emptyList(),

    /** Danh sách Style hiển thị khi chọn Tool */
    val availableStyles: List<StudioStyle> = emptyList(),

    // --- TRẠNG THÁI HỆ THỐNG ---
    /** Flag để báo cho UI biết cần thực hiện hành động Lưu (Cắt/Apply Filter...) */
    val shouldExecuteSave: Boolean = false,

    val isImageTransitionAnimated: Boolean = false,

    val isLoading: Boolean = false,
    val error: String? = null
)
