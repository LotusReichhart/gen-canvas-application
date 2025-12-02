package com.lotusreichhart.gencanvas.feature.home.presentation

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults
import androidx.compose.material3.pulltorefresh.pullToRefresh
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.lerp
import androidx.constraintlayout.compose.Dimension.Companion.fillToConstraints
import androidx.constraintlayout.compose.ExperimentalMotionApi
import androidx.constraintlayout.compose.MotionLayout
import androidx.constraintlayout.compose.MotionScene
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.lotusreichhart.gencanvas.core.common.R
import com.lotusreichhart.gencanvas.core.model.banner.Banner
import com.lotusreichhart.gencanvas.core.model.user.User
import com.lotusreichhart.gencanvas.core.ui.components.UserAvatar
import com.lotusreichhart.gencanvas.core.ui.constant.Dimension
import com.lotusreichhart.gencanvas.feature.home.presentation.HomeConstant.EndHeight
import com.lotusreichhart.gencanvas.feature.home.presentation.HomeConstant.StartHeight
import com.lotusreichhart.gencanvas.feature.home.presentation.components.AutoScrollingCarousel
import com.lotusreichhart.gencanvas.feature.home.presentation.components.HomeTabShimmer
import com.lotusreichhart.gencanvas.feature.home.presentation.components.MainContentList


@OptIn(ExperimentalMotionApi::class)
@Composable
fun HomeTab(
    viewModel: HomeTabViewModel = hiltViewModel(),
    onNavigateToAuth: () -> Unit,
    onNavigateToProfile: () -> Unit
) {
    val lazyListState = rememberLazyListState()
    val density = LocalDensity.current

    val searchBg = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.8f)

    val maxOffsetPx = remember(StartHeight, EndHeight, density) {
        with(density) { (StartHeight - EndHeight).toPx() }
    }
    var headerOffset by rememberSaveable { mutableFloatStateOf(0f) }
    val progress by remember {
        derivedStateOf {
            if (maxOffsetPx == 0f) 0f
            else (headerOffset / -maxOffsetPx).coerceIn(0f, 1f)
        }
    }
    val dynamicHeight = lerp(start = StartHeight, stop = EndHeight, fraction = progress)

    val nestedScrollConnection = remember {
        object : NestedScrollConnection {
            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                val delta = available.y // < 0 khi cuộn LÊN

                if (delta < 0 && headerOffset > -maxOffsetPx) {
                    val newOffset = (headerOffset + delta).coerceIn(-maxOffsetPx, 0f)
                    val consumed = newOffset - headerOffset
                    headerOffset = newOffset
                    return Offset(0f, consumed)
                }
                return Offset.Zero
            }

            override fun onPostScroll(
                consumed: Offset,
                available: Offset,
                source: NestedScrollSource
            ): Offset {
                val delta = available.y // > 0 khi cuộn XUỐNG

                if (delta > 0 && lazyListState.firstVisibleItemIndex == 0 && lazyListState.firstVisibleItemScrollOffset == 0) {
                    val newOffset = (headerOffset + delta).coerceIn(-maxOffsetPx, 0f)
                    val consumedScroll = newOffset - headerOffset
                    headerOffset = newOffset
                    return Offset(0f, consumedScroll)
                }
                return Offset.Zero
            }
        }
    }

    val statusBarHeight = WindowInsets.statusBars.asPaddingValues().calculateTopPadding()

    val motionScene = remember(searchBg, statusBarHeight) {
        MotionScene {
            val searchRef = createRefFor("search")
            val avatarRef = createRefFor("avatar")
            val carouselRef = createRefFor("carousel")

            val startSet = constraintSet("start") {
                // Search (Icon)
                constrain(searchRef) {
                    top.linkTo(parent.top, 6.dp + statusBarHeight)
                    start.linkTo(parent.start, 18.dp)
                    height = Dimension.quickAccessItemSize.asDimension()
                    width = Dimension.quickAccessItemSize.asDimension()
                    customColor("background", searchBg)
                }
                // Avatar (Ghim)
                constrain(avatarRef) {
                    top.linkTo(parent.top, 6.dp + statusBarHeight)
                    end.linkTo(parent.end, 18.dp)
                    height = Dimension.quickAccessItemSize.asDimension()
                    width = Dimension.quickAccessItemSize.asDimension()
                }
                // Carousel (Hiển thị)
                constrain(carouselRef) {
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    height = fillToConstraints
                    width = fillToConstraints
                    alpha = 1f
                    scaleY = 1f
                }
            }

            val endSet = constraintSet("end") {
                // Search (Searchbar)
                constrain(searchRef) {
                    top.linkTo(parent.top, 6.dp + statusBarHeight)
                    start.linkTo(parent.start, Dimension.horizontalPadding)
                    end.linkTo(avatarRef.start, 8.dp)
                    height = Dimension.quickAccessItemSize.asDimension()
                    width = fillToConstraints
                    customColor("background", searchBg)
                }
                // Avatar
                constrain(avatarRef) {
                    top.linkTo(parent.top, 6.dp + statusBarHeight)
                    end.linkTo(parent.end, Dimension.horizontalPadding)
                    height = Dimension.quickAccessItemSize.asDimension()
                    width = Dimension.quickAccessItemSize.asDimension()
                }
                // Carousel (Ẩn)
                constrain(carouselRef) {
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    height = fillToConstraints
                    width = fillToConstraints
                    alpha = 0f
                    scaleY = 1f
                }
            }

            transition(from = startSet, to = endSet) {}
        }
    }

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val user by viewModel.user.collectAsStateWithLifecycle()

    val pullToRefreshState = rememberPullToRefreshState()
    val pullToRefreshEnabled = (progress == 0f)

    if (uiState.isLoading) {
        HomeTabShimmer()
    } else {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .nestedScroll(nestedScrollConnection)
                .pullToRefresh(
                    state = pullToRefreshState,
                    isRefreshing = uiState.isRefreshing,
                    onRefresh = { viewModel.onPullToRefresh() },
                    enabled = pullToRefreshEnabled
                )
        ) {

            MainContentList(
                lazyListState = lazyListState,
                dynamicHeight = dynamicHeight,
                progress = progress,
                statusBarHeight = statusBarHeight,
                uiState = uiState
            )

            HomeHeader(
                motionScene = motionScene,
                progress = progress,
                dynamicHeight = dynamicHeight,
                user = user,
                banners = uiState.banners,
                onAvatarClick = {
                    if (user == null) {
                        onNavigateToAuth()
                    } else {
                        onNavigateToProfile()
                    }
                }
            )

            PullToRefreshDefaults.Indicator(
                state = pullToRefreshState,
                isRefreshing = uiState.isRefreshing,
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = statusBarHeight),
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@OptIn(ExperimentalMotionApi::class)
@Composable
private fun HomeHeader(
    motionScene: MotionScene,
    progress: Float,
    dynamicHeight: Dp,
    user: User? = null,
    banners: List<Banner>,
    onAvatarClick: () -> Unit
) {
    MotionLayout(
        motionScene = motionScene,
        progress = progress,
        modifier = Modifier
            .fillMaxWidth()
            .height(dynamicHeight)
            .background(Color.Transparent)
    ) {
        val searchProps = customProperties("search")

        AutoScrollingCarousel(
            banners = banners,
            modifier = Modifier
                .layoutId("carousel")
        )

        // Search UI
        Card(
            modifier = Modifier.layoutId("search"),
            shape = CircleShape,
            colors = CardDefaults.cardColors(
                containerColor = searchProps.color("background")
            ),
            border = BorderStroke(
                width = 0.1.dp,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.2f)
            ),
            elevation = CardDefaults.cardElevation(2.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = stringResource(R.string.core_search),
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier
                        .padding(start = 8.dp)
                        .graphicsLayer(alpha = progress)
                )
            }
        }

        // Avatar UI
        UserAvatar(
            user = user,
            modifier = Modifier.layoutId("avatar"),
            onClick = onAvatarClick
        )
    }
}