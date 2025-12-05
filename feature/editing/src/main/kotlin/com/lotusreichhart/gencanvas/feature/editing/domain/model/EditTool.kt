package com.lotusreichhart.gencanvas.feature.editing.domain.model

import androidx.compose.ui.graphics.vector.ImageVector

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Crop
import androidx.compose.material.icons.rounded.Rotate90DegreesCw

import com.lotusreichhart.gencanvas.feature.editing.R

/**
 * Danh sách các công cụ thuộc nhóm Điều chỉnh (Edit).
 */
internal enum class EditTool(
    override val id: String,
    override val titleRes: Int,
    override val icon: ImageVector
) : EditorTool {
    CROP(
        id = "crop",
        titleRes = R.string.editing_tool_crop,
        icon = Icons.Default.Crop
    ),
    ROTATE(
        id = "rotate",
        titleRes = R.string.editing_tool_rotate,
        icon = Icons.Rounded.Rotate90DegreesCw
    );
}