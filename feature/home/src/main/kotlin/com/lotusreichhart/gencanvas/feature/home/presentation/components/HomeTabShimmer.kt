package com.lotusreichhart.gencanvas.feature.home.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.lotusreichhart.gencanvas.core.ui.constant.Dimension
import com.lotusreichhart.gencanvas.core.ui.util.shimmerEffect
import com.lotusreichhart.gencanvas.feature.home.presentation.HomeConstant.StartHeight

@Composable
internal fun HomeTabShimmer() {
    val statusBarHeight = WindowInsets.statusBars.asPaddingValues().calculateTopPadding()

    Box(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(StartHeight)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .shimmerEffect()
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 6.dp + statusBarHeight)
                    .padding(horizontal = Dimension.Spacing.m),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(Dimension.Icon.xl)
                        .clip(CircleShape)
                        .shimmerEffect(
                            baseColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f),
                            highlightColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.3f)
                        )
                )

                Spacer(modifier = Modifier.width(16.dp))

                Box(
                    modifier = Modifier
                        .size(Dimension.Icon.xl)
                        .clip(CircleShape)
                        .shimmerEffect(
                            baseColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f),
                            highlightColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.3f)
                        )
                )
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = StartHeight)
                .padding(top = 16.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = Dimension.Spacing.m),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                repeat(2) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(150.dp)
                            .clip(RoundedCornerShape(Dimension.Radius.m))
                            .shimmerEffect()
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            LazyVerticalGrid(
                columns = GridCells.Fixed(4),
                modifier = Modifier
                    .height(100.dp)
                    .padding(horizontal = Dimension.Spacing.m),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                userScrollEnabled = false
            ) {
                items(4) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(100.dp)
                            .clip(RoundedCornerShape(Dimension.Radius.m))
                            .shimmerEffect()
                    )
                }
            }
        }
    }
}