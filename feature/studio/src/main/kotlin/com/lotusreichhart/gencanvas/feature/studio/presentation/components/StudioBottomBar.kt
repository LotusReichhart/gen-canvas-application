package com.lotusreichhart.gencanvas.feature.studio.presentation.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.lotusreichhart.gencanvas.core.ui.components.GenCanvasHorizontalDivider
import com.lotusreichhart.gencanvas.core.ui.components.GenCanvasIcon
import com.lotusreichhart.gencanvas.core.ui.components.GenCanvasIconButton
import com.lotusreichhart.gencanvas.core.ui.components.UiIcon
import com.lotusreichhart.gencanvas.core.ui.constant.Dimension
import com.lotusreichhart.gencanvas.feature.studio.domain.model.StudioFeature
import com.lotusreichhart.gencanvas.feature.studio.domain.model.StudioStyle
import com.lotusreichhart.gencanvas.feature.studio.domain.model.StudioTool
import com.lotusreichhart.gencanvas.feature.studio.presentation.StudioUiState

@Composable
internal fun StudioBottomBar(
    uiState: StudioUiState,
    onSelectFeature: (StudioFeature) -> Unit,
    onSelectTool: (StudioTool) -> Unit,
    onSelectStyle: (StudioStyle) -> Unit,
    onCancelFeature: () -> Unit,
    onApplyFeature: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.Transparent)
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
                    featureTitle = stringResource(feature.titleRes),
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
    features: List<StudioFeature>,
    onSelect: (StudioFeature) -> Unit
) {
    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = Dimension.Spacing.m),
        horizontalArrangement = Arrangement.Center,
        contentPadding = PaddingValues(horizontal = Dimension.Spacing.m)
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
    featureTitle: String,
    tools: List<StudioTool>,
    styles: List<StudioStyle>,
    activeTool: StudioTool?,
    activeStyle: StudioStyle?,
    onCancel: () -> Unit,
    onApply: () -> Unit,
    onSelectTool: (StudioTool) -> Unit,
    onSelectStyle: (StudioStyle) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        if (styles.isNotEmpty()) {
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = Dimension.Spacing.s),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(
                    space = Dimension.Spacing.m,
                    alignment = Alignment.CenterHorizontally
                ),
                contentPadding = PaddingValues(
                    vertical = Dimension.Spacing.none,
                    horizontal = Dimension.Spacing.m
                )
            ) {
                items(styles) { style ->
                    BottomBarItem(
                        label = stringResource(style.titleRes),
                        icon = style.icon,
                        isSelected = style == activeStyle,
                        onClick = { onSelectStyle(style) }
                    )
                }
            }
            GenCanvasHorizontalDivider(thickness = 0.5.dp, color = Color.White.copy(alpha = 0.3f))
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .padding(vertical = Dimension.Spacing.xs),
            verticalAlignment = Alignment.CenterVertically
        ) {
            GenCanvasIconButton(
                modifier = Modifier.weight(1f),
                imageVector = Icons.Default.Close,
                contentDescription = "Cancel Icon",
                iconSize = Dimension.Icon.m,
                tint = Color.White,
                onClick = onCancel,
            )

            Box(modifier = Modifier.weight(4f)) {
                if (tools.isNotEmpty()) {
                    LazyRow(
                        modifier = Modifier.fillMaxSize(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(
                            Dimension.Spacing.m,
                            Alignment.CenterHorizontally
                        ),
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
                } else {
                    Text(
                        modifier = Modifier.align(Alignment.Center),
                        text = featureTitle.uppercase(),
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }

            GenCanvasIconButton(
                modifier = Modifier.weight(1f),
                imageVector = Icons.Default.Check,
                contentDescription = "Apply Icon",
                iconSize = Dimension.Icon.m,
                tint = MaterialTheme.colorScheme.primary,
                onClick = onApply
            )
        }
    }
}

@Composable
private fun BottomBarItem(
    icon: UiIcon,
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick
            )
            .padding(horizontal = Dimension.Spacing.s, vertical = Dimension.Spacing.xxs)
    ) {
        GenCanvasIcon(
            icon = icon,
            contentDescription = label,
            tint = if (isSelected) MaterialTheme.colorScheme.primary else Color.White,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.height(Dimension.Spacing.xxs))
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = if (isSelected) MaterialTheme.colorScheme.primary else Color.Gray
        )
    }
}