package com.lotusreichhart.gencanvas.feature.studio.domain.model.edit.rotate

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.RotateLeft
import androidx.compose.material.icons.automirrored.filled.RotateRight
import com.lotusreichhart.gencanvas.core.ui.components.UiIcon
import com.lotusreichhart.gencanvas.feature.studio.R
import com.lotusreichhart.gencanvas.feature.studio.domain.model.StudioStyle

internal sealed class RotateStyle(
    override val id: String,
    override val titleRes: Int,
    override val icon: UiIcon
) : StudioStyle {
    data object RotateLeft : RotateStyle(
        "rot_left",
        R.string.studio_style_rotate_left,
        UiIcon.Vector(Icons.AutoMirrored.Filled.RotateLeft)
    )

    data object RotateRight : RotateStyle(
        "rot_right",
        R.string.studio_style_rotate_right,
        UiIcon.Vector(Icons.AutoMirrored.Filled.RotateRight)
    )

    data object FlipHorizontal : RotateStyle(
        "flip_h",
        R.string.studio_style_flip_h,
        UiIcon.Drawable(R.drawable.ic_flip_horizontal)
    )

    data object FlipVertical : RotateStyle(
        "flip_v",
        R.string.studio_style_flip_v,
        UiIcon.Drawable(R.drawable.ic_flip_vertical)
    )
}