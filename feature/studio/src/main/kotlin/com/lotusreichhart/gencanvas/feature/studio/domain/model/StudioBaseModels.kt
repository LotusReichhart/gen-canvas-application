package com.lotusreichhart.gencanvas.feature.studio.domain.model

import androidx.annotation.StringRes
import com.lotusreichhart.gencanvas.core.ui.components.UiIcon

/**
 * Interface gốc cho mọi thành phần hiển thị trên UI.
 */
internal interface StudioItem {
    val id: String
    @get:StringRes val titleRes: Int
    val icon: UiIcon
}

/**
 * Kiểu dáng/Thuộc tính.
 * Ví dụ: Tỉ lệ 1:1, Xoay trái...
 */
internal interface StudioStyle : StudioItem

/**
 * Công cụ.
 * Chứa danh sách các Style của nó.
 */
internal interface StudioTool : StudioItem {
    val styles: List<StudioStyle>
    val defaultStyle: StudioStyle?
}

/**
 * Tính năng.
 * Chứa danh sách các Tool của nó.
 */
internal sealed interface StudioFeature : StudioItem

internal interface ToolGroupFeature : StudioFeature {
    val tools: List<StudioTool>
    val defaultTool: StudioTool?
}

internal interface StyleGroupFeature : StudioFeature {
    val styles: List<StudioStyle>
    val defaultStyle: StudioStyle?
}