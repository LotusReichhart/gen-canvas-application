package com.lotusreichhart.gencanvas.feature.studio.presentation.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.lotusreichhart.gencanvas.core.ui.components.GenCanvasIconButton
import com.lotusreichhart.gencanvas.core.ui.components.GenCanvasTextButton
import com.lotusreichhart.gencanvas.core.ui.constant.Dimension

import com.lotusreichhart.gencanvas.core.common.R as CoreR
import com.lotusreichhart.gencanvas.core.ui.R as CoreUiR

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun StudioTopBar(
    isVisible: Boolean,
    canUndo: Boolean,
    canRedo: Boolean,
    onBack: () -> Unit,
    onUndo: () -> Unit,
    onRedo: () -> Unit,
    onSaveFinal: () -> Unit
) {

    val isCenterVisible = canUndo || canRedo

    AnimatedVisibility(
        visible = isVisible,
        enter = fadeIn() + slideInVertically(),
        exit = fadeOut() + slideOutVertically()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .background(Color.Transparent)
                .padding(Dimension.Spacing.m)
        ) {
            IconButton(
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .background(Color.Gray.copy(alpha = 0.3f), CircleShape)
                    .size(32.dp),
                onClick = onBack
            ) {
                Icon(
                    modifier = Modifier.size(Dimension.Icon.m),
                    imageVector = Icons.Default.Close,
                    contentDescription = "Back Icon",
                    tint = Color.White
                )
            }

            if (isCenterVisible) {
                Row(
                    modifier = Modifier.align(Alignment.Center),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(
                        space = Dimension.Spacing.m,
                        alignment = Alignment.CenterHorizontally
                    ),
                ) {
                    IconButton(
                        modifier = Modifier
                            .background(Color.Gray.copy(alpha = 0.3f), CircleShape)
                            .size(32.dp),
                        onClick = onUndo,
                        enabled = canUndo,
                    ) {
                        Icon(
                            painter = painterResource(CoreUiR.drawable.ic_undo),
                            contentDescription = "Undo",
                            tint = if (canUndo) Color.White else Color.Gray
                        )
                    }

                    IconButton(
                        modifier = Modifier
                            .background(Color.Gray.copy(alpha = 0.3f), CircleShape)
                            .size(32.dp),
                        onClick = onRedo,
                        enabled = canRedo
                    ) {
                        Icon(
                            painter = painterResource(CoreUiR.drawable.ic_redo),
                            contentDescription = "Redo",
                            tint = if (canRedo) Color.White else Color.Gray
                        )
                    }
                }
            }

            GenCanvasTextButton(
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .background(
                        Color.Gray.copy(alpha = 0.3f),
                        RoundedCornerShape(Dimension.Radius.m)
                    ),
                text = stringResource(id = CoreR.string.core_action_save),
                textColor = Color.White,
                textStyle = MaterialTheme.typography.labelMedium.copy(
                    fontSize = Dimension.TextSize.titleSmall
                ),
                onClick = onSaveFinal
            )
        }
    }
}