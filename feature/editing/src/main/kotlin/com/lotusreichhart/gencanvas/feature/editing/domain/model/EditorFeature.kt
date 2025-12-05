package com.lotusreichhart.gencanvas.feature.editing.domain.model

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Crop
import androidx.compose.material.icons.filled.Tune
import androidx.compose.ui.graphics.vector.ImageVector
import com.lotusreichhart.gencanvas.feature.editing.R

internal enum class EditorFeature(
    val id: String,
    val titleRes: Int,
    val icon: ImageVector
) {
    EDIT(
        id = "edit",
        titleRes = R.string.editing_feature_edit,
        icon = Icons.Default.Crop
    ),
    FILTER(
        id = "filter",
        titleRes = R.string.editing_feature_filter,
        icon = Icons.Default.Tune
    );
}
