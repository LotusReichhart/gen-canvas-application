package com.lotusreichhart.gencanvas.feature.studio.domain.model.edit

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import com.lotusreichhart.gencanvas.core.ui.components.UiIcon
import com.lotusreichhart.gencanvas.feature.studio.R
import com.lotusreichhart.gencanvas.feature.studio.domain.model.*
import com.lotusreichhart.gencanvas.feature.studio.domain.model.edit.crop.CropTool
import com.lotusreichhart.gencanvas.feature.studio.domain.model.edit.rotate.RotateTool

internal data object EditFeature : ToolGroupFeature {
    override val id = "edit_feature"
    override val titleRes = R.string.studio_feature_edit
    override val icon = UiIcon.Vector(Icons.Default.Crop)

    override val tools = listOf(
        CropTool,
        RotateTool
    )
    override val defaultTool = CropTool
}







