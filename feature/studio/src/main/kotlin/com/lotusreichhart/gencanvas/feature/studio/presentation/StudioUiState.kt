package com.lotusreichhart.gencanvas.feature.studio.presentation

import com.lotusreichhart.gencanvas.feature.studio.domain.model.StudioFeature
import com.lotusreichhart.gencanvas.feature.studio.domain.model.StudioStyle
import com.lotusreichhart.gencanvas.feature.studio.domain.model.StudioTool

internal data class StudioUiState(
    val toolStyleHistory: Map<StudioTool, StudioStyle> = emptyMap(),

    val activeFeature: StudioFeature? = null,
    val activeTool: StudioTool? = null,
    val activeStyle: StudioStyle? = null,

    val availableFeatures: List<StudioFeature> = emptyList(),
    val availableTools: List<StudioTool> = emptyList(),
    val availableStyles: List<StudioStyle> = emptyList(),

    val shouldExecuteSave: Boolean = false,

    val isImageTransitionAnimated: Boolean = false,
    val isLoading: Boolean = false,
    val error: String? = null
)
