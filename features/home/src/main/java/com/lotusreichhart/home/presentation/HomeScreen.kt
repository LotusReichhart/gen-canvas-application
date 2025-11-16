package com.lotusreichhart.home.presentation

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.lerp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.*
import androidx.constraintlayout.compose.Dimension.Companion.fillToConstraints
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import kotlinx.coroutines.delay
import kotlin.math.abs

import com.lotusreichhart.domain.entity.BannerEntity
import com.lotusreichhart.home.utils.EndHeight
import com.lotusreichhart.home.utils.StartHeight

import com.lotusreichhart.home.R

import com.lotusreichhart.core.ui.theme.primaryGradient

@OptIn(
    ExperimentalMaterial3Api::class,
    ExperimentalMotionApi::class,
    ExperimentalFoundationApi::class
)
@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel()
) {
    val lazyListState = rememberLazyListState()
    val density = LocalDensity.current

    val searchBg = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.2f)

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
                    start.linkTo(parent.start, 16.dp)
                    height = 40.dp.asDimension()
                    width = 40.dp.asDimension()
                    customColor("background", searchBg)
                }
                // Avatar (Ghim)
                constrain(avatarRef) {
                    top.linkTo(parent.top, 6.dp + statusBarHeight)
                    end.linkTo(parent.end, 16.dp)
                    height = 40.dp.asDimension()
                    width = 40.dp.asDimension()
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
                    start.linkTo(parent.start, 8.dp)
                    end.linkTo(avatarRef.start, 8.dp)
                    height = 40.dp.asDimension()
                    width = fillToConstraints
                    customColor("background", searchBg)
                }
                // Avatar
                constrain(avatarRef) {
                    top.linkTo(parent.top, 6.dp + statusBarHeight)
                    end.linkTo(parent.end, 8.dp)
                    height = 40.dp.asDimension()
                    width = 40.dp.asDimension()
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

    Box(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(nestedScrollConnection)
    ) {

        HomeContentList(
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
            banners = uiState.banners
        )
    }
}

@OptIn(ExperimentalMotionApi::class)
@Composable
private fun HomeHeader(
    motionScene: MotionScene,
    progress: Float,
    dynamicHeight: Dp,
    banners: List<BannerEntity>
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
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(
                containerColor = searchProps.color("background")
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
                    Icons.Default.Search,
                    contentDescription = "Search",
                    tint = MaterialTheme.colorScheme.onPrimary
                )
                Text(
                    text = "Search...",
                    color = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier
                        .padding(start = 8.dp)
                        .graphicsLayer(alpha = progress)
                )
            }
        }

        // Avatar UI
        Box(
            modifier = Modifier
                .layoutId("avatar")
                .clip(CircleShape)
                .background(
                    brush = primaryGradient()
                ),
            contentAlignment = Alignment.Center
        ) {
            Text("LR", color = Color.White, fontWeight = FontWeight.Bold)
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun HomeContentList(
    lazyListState: LazyListState,
    dynamicHeight: Dp,
    progress: Float,
    statusBarHeight: Dp,
    uiState: HomeUiState
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
                    MyHorizontalListComponent(
                        items = uiState.horizontalListItems,
                        statusBarHeight = statusBarHeight // Truyền padding
                    )
                } else {
                    MyGridComponent(
                        items = uiState.gridItems
                    )
                }
            }
        }

        items(uiState.mainListItems) { item ->
            Text("Nội dung khác ${item.id}", modifier = Modifier.padding(16.dp))
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun AutoScrollingCarousel(
    banners: List<BannerEntity>,
    modifier: Modifier = Modifier
) {
    val actualPageCount = banners.size

    if (actualPageCount == 0) {
        return
    }

    val pagerState = rememberPagerState(
        initialPage = Int.MAX_VALUE / 2,
        pageCount = { Int.MAX_VALUE }
    )

    val textGradient = Brush.linearGradient(
        colors = listOf(
            Color.White,
            MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.7f),
            Color.White
        )
    )

    LaunchedEffect(pagerState, actualPageCount) {
        while (true) {
            delay(5000)
            pagerState.animateScrollToPage(pagerState.currentPage + 1)
        }
    }

    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize()
        ) { infinitePage ->

            val actualPage = infinitePage % actualPageCount
            val banner = banners[actualPage]

            val imageSource: Any = banner.imageUrl.toIntOrNull() ?: banner.imageUrl
            val pageOffset =
                (pagerState.currentPage.toFloat() + pagerState.currentPageOffsetFraction) - infinitePage

            Box(
                modifier = Modifier.fillMaxSize()
            ) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(imageSource)
                        .crossfade(true)
                        .build(),
                    contentDescription = "Banner ${actualPage + 1}",
                    placeholder = painterResource(id = R.drawable.default_banner_1),
                    error = painterResource(id = R.drawable.default_banner_1),
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                colorStops = arrayOf(
                                    0.6f to Color.Transparent,
                                    1.0f to MaterialTheme.colorScheme.background
                                )
                            )
                        )
                )

                Text(
                    text = banner.title ?: "",
                    style = MaterialTheme.typography.titleLarge.copy(
                        shadow = Shadow(Color.Black.copy(0.7f), Offset(4f, 4f), 8f),
                        brush = textGradient
                    ),
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(horizontal = 16.dp, vertical = 40.dp)
                        .graphicsLayer {
                            alpha = 1f - abs(pageOffset)
                            translationY = pageOffset * 50.dp.toPx()
                        }
                )
            }
        }

        Row(
            Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(bottom = 8.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            repeat(actualPageCount) { iteration ->
                val actualCurrentPage = pagerState.currentPage % actualPageCount

                val isSelected = (actualCurrentPage == iteration)
                val color =
                    if (isSelected) MaterialTheme.colorScheme.onPrimary
                    else MaterialTheme.colorScheme.onSurface.copy(
                        alpha = 0.4f
                    )
                val size = if (isSelected) 10.dp else 8.dp

                Box(
                    modifier = Modifier
                        .padding(4.dp)
                        .clip(CircleShape)
                        .background(color)
                        .size(size)
                )
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun MyGridComponent(
    items: List<GridItemData>
) {
    Column(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
    ) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier.height(150.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            userScrollEnabled = false
        ) {
            items(items.filter { it.isLarge }) { item -> // Lọc item lớn
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp)
                        .background(MaterialTheme.colorScheme.secondary, RoundedCornerShape(12.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Grid ${item.id} (To)")
                }
            }
        }
        Spacer(Modifier.height(8.dp))
        LazyVerticalGrid(
            columns = GridCells.Fixed(4),
            modifier = Modifier.height(100.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            userScrollEnabled = false
        ) {
            items(items.filter { !it.isLarge }) { item -> // Lọc item nhỏ
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp)
                        .background(MaterialTheme.colorScheme.secondary, RoundedCornerShape(12.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Grid ${item.id}")
                }
            }
        }
    }
}

@Composable
private fun MyHorizontalListComponent(
    items: List<HorizontalItemData>,
    statusBarHeight: Dp
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background)
            .padding(top = statusBarHeight + 56.dp)
    ) {
        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(items) { item ->
                Box(
                    modifier = Modifier
                        .size(120.dp)
                        .background(MaterialTheme.colorScheme.secondary, RoundedCornerShape(12.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Text("List ${item.id}")
                }
            }
        }
    }
}
