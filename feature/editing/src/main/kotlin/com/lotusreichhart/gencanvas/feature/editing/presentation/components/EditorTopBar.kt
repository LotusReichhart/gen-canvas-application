package com.lotusreichhart.gencanvas.feature.editing.presentation.components

import android.graphics.drawable.PaintDrawable
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import androidx.compose.ui.text.font.FontWeight
import com.lotusreichhart.gencanvas.core.ui.R

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
                                painter = painterResource(R.drawable.ic_undo),
                                contentDescription = "Undo",
                                tint = if (canUndo) Color.White else Color.Gray
                            )
                        }

                        IconButton(onClick = onRedo, enabled = canRedo) {
                            Icon(
                                painter = painterResource(R.drawable.ic_redo),
                                contentDescription = "Redo",
                                tint = if (canRedo) Color.White else Color.Gray
                            )
                        }
                    }
                }
            },
            navigationIcon = {
                IconButton(onClick = onBack) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.White
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color.Black.copy(alpha = 0.5f)
            ),
            actions = {
                TextButton(onClick = onSaveFinal) {
                    Text(
                        text = "Lưu",
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        )
    }
}