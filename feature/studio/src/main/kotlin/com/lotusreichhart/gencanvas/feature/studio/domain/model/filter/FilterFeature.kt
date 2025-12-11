package com.lotusreichhart.gencanvas.feature.studio.domain.model.filter

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowOutward
import androidx.compose.material.icons.filled.Brightness5
import androidx.compose.material.icons.filled.Contrast
import androidx.compose.material.icons.filled.Filter
import androidx.compose.material.icons.filled.FilterNone
import androidx.compose.material.icons.filled.PostAdd
import androidx.compose.material.icons.filled.Scale
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Thermostat
import androidx.compose.material.icons.filled.TurnSharpRight
import androidx.compose.material.icons.filled.Vignette
import com.lotusreichhart.gencanvas.core.ui.components.UiIcon
import com.lotusreichhart.gencanvas.feature.studio.R
import com.lotusreichhart.gencanvas.feature.studio.domain.model.StyleGroupFeature
import ja.burhanrashid52.photoeditor.PhotoFilter

internal data object FilterFeature : StyleGroupFeature {
    override val id: String = "filter_feature"

    override val titleRes: Int = R.string.studio_feature_filter

    override val icon: UiIcon = UiIcon.Vector(Icons.Default.Filter)

    override val styles = listOf(
        FilterStyle(
            id = "none",
            titleRes = R.string.studio_style_filter_none,
            icon = UiIcon.Vector(Icons.Default.FilterNone),
            isDefault = true,
            filterEffect = PhotoFilter.NONE
        ),
        FilterStyle(
            id = "brightness",
            titleRes = R.string.studio_style_filter_brightness,
            icon = UiIcon.Vector(Icons.Default.Brightness5),
            filterEffect = PhotoFilter.BRIGHTNESS
        ),
        FilterStyle(
            id = "contrast",
            titleRes = R.string.studio_style_filter_contrast,
            icon = UiIcon.Vector(Icons.Default.Contrast),
            filterEffect = PhotoFilter.CONTRAST
        ),
        FilterStyle(
            id = "gray_scale",
            titleRes = R.string.studio_style_filter_gray_scale,
            icon = UiIcon.Vector(Icons.Default.Scale),
            filterEffect = PhotoFilter.GRAY_SCALE
        ),
        FilterStyle(
            id = "sepia",
            titleRes = R.string.studio_style_filter_sepia,
            icon = UiIcon.Vector(Icons.Default.Star),
            filterEffect = PhotoFilter.SEPIA
        ),
        FilterStyle(
            id = "sharp",
            titleRes = R.string.studio_style_filter_sharp,
            icon = UiIcon.Vector(Icons.Default.TurnSharpRight),
            filterEffect = PhotoFilter.SHARPEN
        ),
        FilterStyle(
            id = "warm",
            titleRes = R.string.studio_style_filter_warm,
            icon = UiIcon.Vector(Icons.Default.Thermostat),
            filterEffect = PhotoFilter.TEMPERATURE
        ),
        FilterStyle(
            id = "vignette",
            titleRes = R.string.studio_style_filter_vignette,
            icon = UiIcon.Vector(Icons.Default.Vignette),
            filterEffect = PhotoFilter.VIGNETTE
        ),
        FilterStyle(
            id = "cross",
            titleRes = R.string.studio_style_filter_cross,
            icon = UiIcon.Vector(Icons.Default.ArrowOutward),
            filterEffect = PhotoFilter.CROSS_PROCESS
        ),
        FilterStyle(
            id = "poster",
            titleRes = R.string.studio_style_filter_poster,
            icon = UiIcon.Vector(Icons.Default.PostAdd),
            filterEffect = PhotoFilter.POSTERIZE
        ),
        FilterStyle(
            id = "black_white",
            titleRes = R.string.studio_style_filter_black_white,
            icon = UiIcon.Vector(Icons.Default.Contrast),
            filterEffect = PhotoFilter.BLACK_WHITE
        )
    )

    override val defaultStyle = styles.first()
}