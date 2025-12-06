package com.lotusreichhart.gencanvas.feature.studio.domain.model.edit.crop

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Crop
import com.lotusreichhart.gencanvas.core.ui.components.UiIcon
import com.lotusreichhart.gencanvas.feature.studio.R
import com.lotusreichhart.gencanvas.feature.studio.domain.model.StudioTool

internal data object CropTool : StudioTool {
    override val id = "crop_tool"
    override val titleRes = R.string.studio_tool_crop
    override val icon = UiIcon.Vector(Icons.Default.Crop)

    override val styles = listOf(
        CropStyle.Free,
        CropStyle.Original,
        CropStyle.Square,
        CropStyle.Circle,
        CropStyle.Ratio2_3,
        CropStyle.Ratio3_2,
        CropStyle.Ratio3_4,
        CropStyle.Ratio4_3,
        CropStyle.Ratio9_16,
        CropStyle.Ratio16_9
    )
    override val defaultStyle = CropStyle.Free
}