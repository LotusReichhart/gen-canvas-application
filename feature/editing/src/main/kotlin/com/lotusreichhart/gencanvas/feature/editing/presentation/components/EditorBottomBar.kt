package com.lotusreichhart.gencanvas.feature.editing.presentation.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.Crossfade
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.lotusreichhart.gencanvas.feature.editing.domain.model.EditorFeature
import com.lotusreichhart.gencanvas.feature.editing.domain.model.EditorTool
import com.lotusreichhart.gencanvas.feature.editing.domain.model.ToolStyle
import com.lotusreichhart.gencanvas.feature.editing.presentation.EditorUiState

@Composable
internal fun EditorBottomBar(
    uiState: EditorUiState,
    onSelectFeature: (EditorFeature) -> Unit,
    onCancelFeature: () -> Unit,
    onApplyFeature: () -> Unit,
    onSelectTool: (EditorTool) -> Unit,
    onSelectStyle: (ToolStyle) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.Black)
    ) {
        AnimatedContent(
            targetState = uiState.activeFeature,
            label = "BottomBarAnimation",
            transitionSpec = {
                if (targetState != null) {
                    (slideInVertically { height -> height } + fadeIn()) togetherWith
                            fadeOut()
                } else {
                    fadeIn() togetherWith
                            (slideOutVertically { height -> height } + fadeOut())
                }.using(
                    SizeTransform(clip = false)
                )
            }
        ) { feature ->
            if (feature == null) {
                FeatureListRow(
                    features = uiState.availableFeatures,
                    onSelect = onSelectFeature
                )
            } else {
                FeatureDetailLayout(
                    tools = uiState.availableTools,
                    styles = uiState.availableStyles,
                    activeTool = uiState.activeTool,
                    activeStyle = uiState.activeStyle,
                    onCancel = onCancelFeature,
                    onApply = onApplyFeature,
                    onSelectTool = onSelectTool,
                    onSelectStyle = onSelectStyle
                )
            }
        }
    }
}

@Composable
private fun FeatureListRow(
    features: List<EditorFeature>,
    onSelect: (EditorFeature) -> Unit
) {
    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        horizontalArrangement = Arrangement.Center,
        contentPadding = PaddingValues(horizontal = 16.dp)
    ) {
        items(features) { feature ->
            BottomBarItem(
                icon = feature.icon,
                label = stringResource(feature.titleRes),
                isSelected = false,
                onClick = { onSelect(feature) }
            )
        }
    }
}

@Composable
private fun FeatureDetailLayout(
    tools: List<EditorTool>,
    styles: List<ToolStyle>,
    activeTool: EditorTool?,
    activeStyle: ToolStyle?,
    onCancel: () -> Unit,
    onApply: () -> Unit,
    onSelectTool: (EditorTool) -> Unit,
    onSelectStyle: (ToolStyle) -> Unit
) {
    Column {
        if (styles.isNotEmpty()) {
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(horizontal = 16.dp)
            ) {
                items(styles) { style ->
                    StyleChip(
                        label = style.title,
                        icon = style.icon,
                        isSelected = style == activeStyle,
                        onClick = { onSelectStyle(style) }
                    )
                }
            }
            HorizontalDivider(thickness = 0.5.dp, color = Color.DarkGray)
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onCancel, modifier = Modifier.weight(1f)) {
                Icon(Icons.Default.Close, contentDescription = "Cancel", tint = Color.White)
            }

            Box(modifier = Modifier.weight(4f)) {
                LazyRow(
                    modifier = Modifier.fillMaxSize(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    items(tools) { tool ->
                        BottomBarItem(
                            icon = tool.icon,
                            label = stringResource(tool.titleRes),
                            isSelected = tool == activeTool,
                            onClick = { onSelectTool(tool) }
                        )
                    }
                }
            }

            IconButton(onClick = onApply, modifier = Modifier.weight(1f)) {
                Icon(
                    Icons.Default.Check,
                    contentDescription = "Apply",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

@Composable
private fun BottomBarItem(
    icon: ImageVector,
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clickable(onClick = onClick)
            .padding(horizontal = 12.dp, vertical = 4.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = if (isSelected) MaterialTheme.colorScheme.primary else Color.White,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = if (isSelected) MaterialTheme.colorScheme.primary else Color.Gray
        )
    }
}

@Composable
private fun StyleChip(
    label: String,
    icon: ImageVector,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clickable(onClick = onClick)
            .padding(horizontal = 12.dp, vertical = 4.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = if (isSelected) MaterialTheme.colorScheme.primary else Color.White,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            color = if (isSelected) MaterialTheme.colorScheme.primary else Color.White,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
        )
    }
}