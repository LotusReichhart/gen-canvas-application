package com.lotusreichhart.gencanvas.feature.editing.presentation.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.lotusreichhart.gencanvas.core.ui.components.GenCanvasIconButton
import com.lotusreichhart.gencanvas.core.ui.components.GenCanvasTextButton
import com.lotusreichhart.gencanvas.core.ui.constant.Dimension

import com.lotusreichhart.gencanvas.core.common.R as CoreR
import com.lotusreichhart.gencanvas.core.ui.R as CoreUiR

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun EditorTopBar(
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
        CenterAlignedTopAppBar(
            title = {
                if (isCenterVisible) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        IconButton(onClick = onUndo, enabled = canUndo) {
                            Icon(
                                painter = painterResource(CoreUiR.drawable.ic_undo),
                                contentDescription = "Undo",
                                tint = if (canUndo) Color.White else Color.Gray
                            )
                        }

                        IconButton(onClick = onRedo, enabled = canRedo) {
                            Icon(
                                painter = painterResource(CoreUiR.drawable.ic_redo),
                                contentDescription = "Redo",
                                tint = if (canRedo) Color.White else Color.Gray
                            )
                        }
                    }
                }
            },
            navigationIcon = {
                GenCanvasIconButton(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Back Icon",
                    iconSize = Dimension.Icon.m,
                    tint = Color.White,
                    onClick = onBack,
                )
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color.Black.copy(alpha = 0.5f)
            ),
            actions = {
                GenCanvasTextButton(
                    text = stringResource(id = CoreR.string.core_action_save),
                    textColor = MaterialTheme.colorScheme.primary,
                    textStyle = MaterialTheme.typography.labelMedium.copy(
                        fontSize = Dimension.TextSize.titleSmall
                    ),
                    onClick = onSaveFinal
                )
            }
        )
    }
}