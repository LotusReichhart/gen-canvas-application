package com.lotusreichhart.onboarding.presentation

import com.lotusreichhart.core.R

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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.lotusreichhart.core.ui.components.GradientButton
import kotlinx.coroutines.launch

private data class OnboardingPage(
    val title: String,
    val description: String,
    val imageRes: Int
)

private val onboardingPages = listOf(
    OnboardingPage(
        title = "Chào mừng đến GenCanvas",
        description = "Giải phóng sức sáng tạo của bạn với sức mạnh của AI. Tạo, chỉnh sửa và nâng tầm ảnh của bạn.",
        imageRes = R.drawable.first_onboarding
    ),
    OnboardingPage(
        title = "Chỉnh sửa Thông minh",
        description = "Dễ dàng xóa phông, tạo ảnh thẻ chuyên nghiệp, và ghép ảnh chỉ với vài cú chạm.",
        imageRes = R.drawable.second_onboarding
    ),
    OnboardingPage(
        title = "Sẵn sàng Khám phá",
        description = "Bắt đầu hành trình sáng tạo của bạn ngay bây giờ và biến ý tưởng thành hiện thực.",
        imageRes = R.drawable.last_onboarding
    )
)

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun OnboardingScreen(
    viewModel: OnboardingViewModel = hiltViewModel(),
    onNavigateToMain: () -> Unit
) {
    val pageCount = onboardingPages.size
    val pagerState = rememberPagerState(pageCount = { pageCount })
    val scope = rememberCoroutineScope()

    val nextButtonGradient = Brush.linearGradient(
        colors = listOf(
            MaterialTheme.colorScheme.secondary,
            MaterialTheme.colorScheme.primaryContainer,
            MaterialTheme.colorScheme.primary
        )
    )

    val startButtonGradient = Brush.linearGradient(
        colors = listOf(
            MaterialTheme.colorScheme.tertiary,
            MaterialTheme.colorScheme.errorContainer,
            MaterialTheme.colorScheme.error
        )
    )

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
                text = "Tiếp tục",
                gradient = nextButtonGradient,
                onClick = {
                    scope.launch {
                        pagerState.animateScrollToPage(pagerState.currentPage + 1)
                    }
                }
            )
        } else {
            GradientButton(
                text = "Bắt đầu ngay",
                gradient = startButtonGradient,
                onClick = {
                    scope.launch {
                        viewModel.onGetStartedClick()
                        onNavigateToMain()
                    }
                }
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            Modifier
                .height(50.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            repeat(pageCount) { iteration ->
                val color =
                    if (pagerState.currentPage == iteration) MaterialTheme.colorScheme.onPrimary
                    else MaterialTheme.colorScheme.onSurface.copy(
                        alpha = 0.3f
                    )
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
                contentDescription = page.title,
                modifier = Modifier
                    .fillMaxSize(),
                contentScale = ContentScale.Crop
            )

            // Gradient Overlay loại bỏ hard edge của Image
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
            modifier = Modifier.padding(horizontal = 16.dp)
        ) {
            Text(
                text = page.title,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = page.description,
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center
            )
        }
    }
}