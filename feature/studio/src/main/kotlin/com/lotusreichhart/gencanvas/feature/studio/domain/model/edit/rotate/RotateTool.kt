package com.lotusreichhart.gencanvas.feature.studio.domain.model.edit.rotate

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.RotateRight
import com.lotusreichhart.gencanvas.core.ui.components.UiIcon
import com.lotusreichhart.gencanvas.feature.studio.R
import com.lotusreichhart.gencanvas.feature.studio.domain.model.StudioTool

internal data object RotateTool : StudioTool {
    override val id = "rotate_tool"
    override val titleRes = R.string.studio_tool_rotate
    override val icon = UiIcon.Vector(Icons.AutoMirrored.Filled.RotateRight)

    override val styles = listOf(
        RotateStyle.RotateLeft,
        RotateStyle.RotateRight,
        RotateStyle.FlipHorizontal,
        RotateStyle.FlipVertical
    )
    override val defaultStyle = null
}