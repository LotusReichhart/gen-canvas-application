package com.lotusreichhart.gencanvas.feature.home.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.lotusreichhart.gencanvas.core.model.banner.Banner
import com.lotusreichhart.gencanvas.core.ui.constant.Dimension
import com.lotusreichhart.gencanvas.feature.home.R
import kotlinx.coroutines.delay
import kotlin.math.abs

@Composable
fun AutoScrollingCarousel(
    banners: List<Banner>,
    modifier: Modifier = Modifier
){
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
                    fontSize = Dimension.xxlFontSize,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(horizontal = Dimension.smallPadding, vertical = Dimension.xxxlPadding)
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
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            repeat(actualPageCount) { iteration ->
                val actualCurrentPage = pagerState.currentPage % actualPageCount

                val isSelected = (actualCurrentPage == iteration)
                val color =
                    if (isSelected) MaterialTheme.colorScheme.primary.copy(alpha = 0.8f)
                    else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.8f)
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