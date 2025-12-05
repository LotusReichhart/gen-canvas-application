package com.lotusreichhart.gencanvas.feature.editing.domain.model

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Crop32
import androidx.compose.material.icons.filled.Crop75
import androidx.compose.material.icons.filled.CropFree
import androidx.compose.material.icons.filled.CropOriginal
import androidx.compose.material.icons.filled.CropPortrait
import androidx.compose.material.icons.filled.CropSquare
import androidx.compose.material.icons.filled.Lens
import androidx.compose.ui.graphics.vector.ImageVector

/**
 * Các kiểu cắt ảnh (Tỉ lệ khung hình) cụ thể cho CropTool.
 */
internal sealed class CropStyle(
    override val id: String,
    override val title: String,
    override val icon: ImageVector, // Override Icon
    val aspectRatio: AspectRatio? // Null = Free hoặc Original (Xử lý logic riêng)
) : ToolStyle {

    // 1. FREE ORIGINAL: Tự do hoàn toàn (Không khóa góc)
    data object Free : CropStyle(
        id = "free",
        title = "Tự do",
        icon = Icons.Default.CropFree,
        aspectRatio = null
    )


    // 2. ORIGINAL: Khóa tỷ lệ theo ảnh gốc (Kéo theo đường chéo)

    data object Original : CropStyle(
        id = "original",
        title = "Gốc",
        icon = Icons.Default.CropOriginal,
        aspectRatio = null
    )

    // 3. Các tỷ lệ cố định
    data object Square : CropStyle(
        id = "1:1",
        title = "Vuông",
        icon = Icons.Default.CropSquare,
        aspectRatio = AspectRatio(1, 1)
    )

    data object Circle : CropStyle(
        "circle",
        "Tròn", Icons.Default.Lens,
        AspectRatio(1, 1)
    )

    data object Ratio2_3 : CropStyle(
        id = "2:3",
        title = "2:3",
        icon = Icons.Default.CropPortrait, // Tạm dùng icon có sẵn
        aspectRatio = AspectRatio(2, 3)
    )

    data object Ratio3_2 : CropStyle(
        id = "3:2",
        title = "3:2",
        icon = Icons.Default.Crop32, // Tạm dùng icon có sẵn
        aspectRatio = AspectRatio(3, 2)
    )

    data object Ratio3_4 : CropStyle(
        id = "3:4",
        title = "3:4",
        icon = Icons.Default.Crop32, // Tạm dùng icon có sẵn
        aspectRatio = AspectRatio(3, 4)
    )

    data object Ratio4_3 : CropStyle(
        id = "4:3",
        title = "4:3",
        icon = Icons.Default.CropPortrait, // Tạm dùng icon có sẵn
        aspectRatio = AspectRatio(4, 3)
    )

    data object Ratio9_16 : CropStyle(
        id = "9:16",
        title = "9:16",
        icon = Icons.Default.Crop75, // Tạm dùng icon có sẵn
        aspectRatio = AspectRatio(9, 16)
    )

    data object Ratio16_9 : CropStyle(
        id = "16:9",
        title = "16:9",
        icon = Icons.Default.Crop75, // Tạm dùng icon có sẵn
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
