package com.lotusreichhart.gencanvas.feature.editing.domain.model

import androidx.compose.ui.graphics.vector.ImageVector


/**
 * Interface cơ sở cho tất cả các Kiểu/Thuộc tính (Style) của mọi công cụ.
 * Ví dụ: CropStyle, FilterStyle, TextStyle...
 */
internal interface ToolStyle {
    val id: String
    val title: String
    val icon: ImageVector
}