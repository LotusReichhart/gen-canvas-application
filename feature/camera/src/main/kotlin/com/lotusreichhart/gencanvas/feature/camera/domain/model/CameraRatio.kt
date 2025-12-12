package com.lotusreichhart.gencanvas.feature.camera.domain.model

import androidx.camera.core.AspectRatio

enum class CameraRatio(val label: String, val aspectRatioStrategy: Int, val floatRatio: Float) {
    RATIO_4_3("4:3", AspectRatio.RATIO_4_3, 3f / 4f),
    RATIO_16_9("16:9", AspectRatio.RATIO_16_9, 9f / 16f);

    fun next(): CameraRatio {
        return if (this == RATIO_4_3) RATIO_16_9 else RATIO_4_3
    }
}