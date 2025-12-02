package com.lotusreichhart.gencanvas.feature.home.presentation.components

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.lotusreichhart.gencanvas.feature.home.presentation.HomeTabUiState

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MainContentList(
    lazyListState: LazyListState,
    dynamicHeight: Dp,
    progress: Float,
    statusBarHeight: Dp,
    uiState: HomeTabUiState
) {
    LazyColumn(
        state = lazyListState,
        contentPadding = PaddingValues(top = dynamicHeight),
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        stickyHeader {
            Crossfade(
                targetState = progress > 0.7f,
                label = "GridToListFade",
                animationSpec = tween(300)
            ) { isCollapsed ->
                if (isCollapsed) {
                    HorizontalListComponent(
                        items = uiState.horizontalListItems,
                        statusBarHeight = statusBarHeight
                    )
                } else {
                    GridComponent(
                        items = uiState.gridItems
                    )
                }
            }
        }

        items(uiState.mainListItems) { item ->
            Text("Nội dung khác ${item.id}", modifier = Modifier.padding(12.dp))
        }
    }
}