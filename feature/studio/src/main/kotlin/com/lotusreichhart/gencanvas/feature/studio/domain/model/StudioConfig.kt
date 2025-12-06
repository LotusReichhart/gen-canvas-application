package com.lotusreichhart.gencanvas.feature.studio.domain.model

import com.lotusreichhart.gencanvas.feature.studio.domain.model.edit.EditFeature
import javax.inject.Inject

internal class StudioConfig @Inject constructor() {
    val features: List<StudioFeature> = listOf(
        EditFeature,
    )
}