package com.lotusreichhart.gencanvas.feature.studio.domain.model.edit.crop

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CropFree
import androidx.compose.material.icons.filled.CropOriginal
import androidx.compose.material.icons.filled.CropSquare
import androidx.compose.material.icons.outlined.Lens
import androidx.compose.ui.graphics.vector.ImageVector
import com.lotusreichhart.gencanvas.core.ui.components.UiIcon
import com.lotusreichhart.gencanvas.feature.studio.R
import com.lotusreichhart.gencanvas.feature.studio.domain.model.StudioStyle
import com.lotusreichhart.gencanvas.feature.studio.domain.util.createAspectRatioIcon

internal sealed class CropStyle(
    override val id: String,
    override val titleRes: Int,
    override val icon: UiIcon,
    val aspectRatio: AspectRatio?
) : StudioStyle {
    data object Free : CropStyle(
        "free",
        R.string.studio_style_free,
        UiIcon.Vector(Icons.Default.CropFree),
        null
    )

    data object Original : CropStyle(
        "original",
        R.string.studio_style_original,
        UiIcon.Vector(Icons.Default.CropOriginal),
        null
    )

    data object Square : CropStyle(
        "1:1",
        R.string.studio_style_square,
        UiIcon.Vector(Icons.Default.CropSquare),
        AspectRatio(1, 1)
    )

    data object Circle : CropStyle(
        "circle",
        R.string.studio_style_circle,
        UiIcon.Vector(Icons.Outlined.Lens),
        AspectRatio(1, 1)
    )

    data object Ratio2_3 : CropStyle(
        id = "2:3",
        titleRes = R.string.studio_style_ratio_2_3,
        icon = UiIcon.Vector(createAspectRatioIcon(2f, 3f)),
        aspectRatio = AspectRatio(2, 3)
    )

    data object Ratio3_2 : CropStyle(
        "3:2",
        R.string.studio_style_ratio_3_2,
        UiIcon.Vector(createAspectRatioIcon(3f, 2f)),
        AspectRatio(3, 2)
    )

    data object Ratio3_4 : CropStyle(
        id = "3:4",
        titleRes = R.string.studio_style_ratio_3_4,
        icon = UiIcon.Vector(createAspectRatioIcon(3f, 4f)),
        aspectRatio = AspectRatio(3, 4)
    )

    data object Ratio4_3 : CropStyle(
        "4:3",
        R.string.studio_style_ratio_4_3,
        UiIcon.Vector(createAspectRatioIcon(4f, 3f)),
        AspectRatio(4, 3)
    )

    data object Ratio9_16 : CropStyle(
        id = "9:16",
        titleRes = R.string.studio_style_ratio_9_16,
        icon = UiIcon.Vector(createAspectRatioIcon(9f, 16f)),
        aspectRatio = AspectRatio(9, 16)
    )

    data object Ratio16_9 : CropStyle(
        "16:9",
        R.string.studio_style_ratio_16_9,
        UiIcon.Vector(createAspectRatioIcon(16f, 9f)),
        AspectRatio(16, 9)
    )
}