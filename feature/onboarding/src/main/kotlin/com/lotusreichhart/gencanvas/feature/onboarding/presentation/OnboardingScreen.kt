package com.lotusreichhart.gencanvas.feature.onboarding.presentation

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.lotusreichhart.gencanvas.core.ui.R as CoreR
import com.lotusreichhart.gencanvas.core.ui.components.GradientButton
import com.lotusreichhart.gencanvas.core.ui.constant.Dimension
import com.lotusreichhart.gencanvas.core.ui.util.primaryGradient
import com.lotusreichhart.gencanvas.core.ui.util.tertiaryGradient
import kotlinx.coroutines.launch

import com.lotusreichhart.gencanvas.feature.onboarding.R

@OptIn(ExperimentalFoundationApi::class)
@Composable
internal fun OnboardingScreen(
    viewModel: OnboardingViewModel = hiltViewModel(),
    onNavigateToMain: () -> Unit
) {
    val pageCount = onboardingPages.size
    val pagerState = rememberPagerState(pageCount = { pageCount })
    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(0.dp)
            .padding(0.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.weight(1f),
            pageSize = PageSize.Fill
        ) { pageIndex ->
            OnboardingPageItem(page = onboardingPages[pageIndex])
        }

        if (pagerState.currentPage < pageCount - 1) {
            GradientButton(
                text = stringResource(id = R.string.btn_next),
                modifier = Modifier.width(250.dp),
                gradient = primaryGradient(),
                onClick = {
                    scope.launch {
                        pagerState.animateScrollToPage(pagerState.currentPage + 1)
                    }
                }
            )
        } else {
            GradientButton(
                text = stringResource(id = R.string.btn_start),
                modifier = Modifier.width(250.dp),
                gradient = tertiaryGradient(),
                onClick = {
                    scope.launch {
                        viewModel.onGetStartedClick()
                        onNavigateToMain()
                    }
                }
            )
        }

        Spacer(modifier = Modifier.height(Dimension.Spacing.l))

        Row(
            Modifier
                .height(50.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            repeat(pageCount) { iteration ->
                val color =
                    if (pagerState.currentPage == iteration) MaterialTheme.colorScheme.onBackground
                    else MaterialTheme.colorScheme.onBackground.copy(alpha = 0.3f)
                Box(
                    modifier = Modifier
                        .padding(4.dp)
                        .clip(CircleShape)
                        .background(color)
                        .size(10.dp)
                )
            }
        }
    }
}

@Composable
private fun OnboardingPageItem(page: OnboardingPage) {

    val backgroundColor = MaterialTheme.colorScheme.background

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
        ) {
            Image(
                painter = painterResource(id = page.imageRes),
                contentDescription = stringResource(id = page.title),
                modifier = Modifier
                    .fillMaxSize(),
                contentScale = ContentScale.Crop
            )

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colorStops = arrayOf(
                                0.6f to Color.Transparent,
                                1.0f to backgroundColor
                            )
                        )
                    )
            )
        }

        Spacer(modifier = Modifier.height(36.dp))
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(horizontal = Dimension.Spacing.m)
        ) {
            Text(
                text = stringResource(id = page.title),
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = stringResource(id = page.description),
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center
            )
        }
    }
}

private data class OnboardingPage(
    val title: Int,
    val description: Int,
    val imageRes: Int
)

private val onboardingPages = listOf(
    OnboardingPage(
        title = R.string.onboarding_title_1,
        description = R.string.onboarding_desc_1,
        imageRes = CoreR.drawable.color_blocks
    ),
    OnboardingPage(
        title =  R.string.onboarding_title_2,
        description = R.string.onboarding_desc_2,
        imageRes = CoreR.drawable.digital_gate
    ),
    OnboardingPage(
        title = R.string.onboarding_title_3,
        description = R.string.onboarding_desc_3,
        imageRes = CoreR.drawable.digital_hand
    )
)