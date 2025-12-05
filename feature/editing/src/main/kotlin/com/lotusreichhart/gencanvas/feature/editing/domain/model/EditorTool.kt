package com.lotusreichhart.gencanvas.feature.editing.domain.model

import androidx.compose.ui.graphics.vector.ImageVector

/**
 * Interface cơ sở cho tất cả các Tool.
 * Mỗi Feature sẽ có một bộ Tool enum riêng implement interface này.
 */
internal interface EditorTool {
    val id: String
    val titleRes: Int
    val icon: ImageVector
}
