package com.lotusreichhart.gencanvas.feature.studio.domain.model.filter

import com.lotusreichhart.gencanvas.core.ui.components.UiIcon
import com.lotusreichhart.gencanvas.feature.studio.domain.model.StudioStyle
import ja.burhanrashid52.photoeditor.PhotoFilter

internal data class FilterStyle(
    override val id: String,
    override val titleRes: Int,
    override val icon: UiIcon,
    val isDefault: Boolean = false,
    val filterEffect: PhotoFilter
) : StudioStyle


