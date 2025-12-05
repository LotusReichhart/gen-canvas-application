package com.lotusreichhart.gencanvas.feature.editing.domain.model

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CropFree
import androidx.compose.material.icons.filled.CropOriginal
import androidx.compose.material.icons.filled.CropSquare
import androidx.compose.material.icons.outlined.Lens
import androidx.compose.ui.graphics.vector.ImageVector

import com.lotusreichhart.gencanvas.feature.editing.R
import com.lotusreichhart.gencanvas.feature.editing.domain.util.createAspectRatioIcon

/**
 * Các kiểu cắt ảnh (Tỉ lệ khung hình) cụ thể cho CropTool.
 */
internal sealed class CropStyle(
    override val id: String,
    override val titleRes: Int,
    override val icon: ImageVector,
    val aspectRatio: AspectRatio?
) : ToolStyle {

    // 1. FREE ORIGINAL: Tự do hoàn toàn (Không khóa góc)
    data object Free : CropStyle(
        id = "free",
        titleRes = R.string.editing_style_free,
        icon = Icons.Default.CropFree,
        aspectRatio = null
    )


    // 2. ORIGINAL: Khóa tỷ lệ theo ảnh gốc (Kéo theo đường chéo)
    data object Original : CropStyle(
        id = "original",
        titleRes = R.string.editing_style_original,
        icon = Icons.Default.CropOriginal,
        aspectRatio = null
    )

    // 3. Các tỷ lệ cố định
    data object Square : CropStyle(
        id = "1:1",
        titleRes = R.string.editing_style_square,
        icon = Icons.Default.CropSquare,
        aspectRatio = AspectRatio(1, 1)
    )

    data object Circle : CropStyle(
        id = "circle",
        titleRes = R.string.editing_style_circle,
        icon = Icons.Outlined.Lens,
        aspectRatio = AspectRatio(1, 1)
    )

    data object Ratio2_3 : CropStyle(
        id = "2:3",
        titleRes = R.string.editing_style_ratio_2_3,
        icon = createAspectRatioIcon(2f, 3f),
        aspectRatio = AspectRatio(2, 3)
    )

    data object Ratio3_2 : CropStyle(
        id = "3:2",
        titleRes = R.string.editing_style_ratio_3_2,
        icon = createAspectRatioIcon(3f, 2f),
        aspectRatio = AspectRatio(3, 2)
    )

    data object Ratio3_4 : CropStyle(
        id = "3:4",
        titleRes = R.string.editing_style_ratio_3_4,
        icon = createAspectRatioIcon(3f, 4f),
        aspectRatio = AspectRatio(3, 4)
    )

    data object Ratio4_3 : CropStyle(
        id = "4:3",
        titleRes = R.string.editing_style_ratio_4_3,
        icon = createAspectRatioIcon(4f, 3f),
        aspectRatio = AspectRatio(4, 3)
    )

    data object Ratio9_16 : CropStyle(
        id = "9:16",
        titleRes = R.string.editing_style_ratio_9_16,
        icon = createAspectRatioIcon(9f, 16f),
        aspectRatio = AspectRatio(9, 16)
    )

    data object Ratio16_9 : CropStyle(
        id = "16:9",
        titleRes = R.string.editing_style_ratio_16_9,
        icon = createAspectRatioIcon(16f, 9f),
        aspectRatio = AspectRatio(16, 9)
    )

    companion object {
        fun getAll() = listOf(
            Free,
            Original,
            Square,
            Circle,
            Ratio2_3,
            Ratio3_2,
            Ratio3_4,
            Ratio4_3,
            Ratio9_16,
            Ratio16_9
        )
    }
}