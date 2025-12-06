package com.lotusreichhart.gencanvas.core.ui.components

import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource

sealed interface UiIcon {
    data class Vector(val imageVector: ImageVector) : UiIcon
    data class Drawable(val resId: Int) : UiIcon
}

@Composable
fun GenCanvasIcon(
    icon: UiIcon,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    tint: Color = LocalContentColor.current
) {
    when (icon) {
        is UiIcon.Vector -> Icon(
            imageVector = icon.imageVector,
            contentDescription = contentDescription,
            modifier = modifier,
            tint = tint
        )

        is UiIcon.Drawable -> Icon(
            painter = painterResource(id = icon.resId),
            contentDescription = contentDescription,
            modifier = modifier,
            tint = tint
        )
    }
}