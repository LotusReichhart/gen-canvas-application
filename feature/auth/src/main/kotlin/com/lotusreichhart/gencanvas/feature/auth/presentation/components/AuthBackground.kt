package com.lotusreichhart.gencanvas.feature.auth.presentation.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.lotusreichhart.gencanvas.core.ui.constant.Dimension
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.random.Random

private enum class BackgroundEffect {
    IDLE,
    PAN_LEFT_RIGHT,
    ZOOM_IN_OUT
}

@Composable
internal fun AuthBackground(
    modifier: Modifier = Modifier,
    backgroundImageRes: Int,
    content: @Composable ColumnScope.() -> Unit
) {
    val animScale = remember { Animatable(1.1f) }
    val animTranslationX = remember { Animatable(0f) }
    val animTranslationY = remember { Animatable(0f) }
    val animRotation = remember { Animatable(0f) }

    val scrollState = rememberScrollState()

    LaunchedEffect(Unit) {
        while (true) {
            val waitTime = Random.nextLong(2000, 5000)
            delay(waitTime)

            val nextEffect = BackgroundEffect.entries[Random.nextInt(BackgroundEffect.entries.size)]

            launch {
                when (nextEffect) {
                    BackgroundEffect.IDLE -> {
                        animScale.animateTo(1.1f, tween(3000, easing = LinearEasing))
                        animTranslationX.animateTo(0f, tween(3000, easing = LinearEasing))
                        animRotation.animateTo(0f, tween(3000, easing = LinearEasing))
                    }

                    BackgroundEffect.PAN_LEFT_RIGHT -> {
                        val targetX = Random.nextInt(-50, 50).toFloat()
                        animTranslationX.animateTo(
                            targetValue = targetX,
                            animationSpec = tween(
                                durationMillis = 6000,
                                easing = CubicBezierEasing(0.4f, 0.0f, 0.2f, 1.0f)
                            )
                        )
                    }

                    BackgroundEffect.ZOOM_IN_OUT -> {
                        val targetScale = Random.nextDouble(1.1, 1.25).toFloat()
                        animScale.animateTo(
                            targetValue = targetScale,
                            animationSpec = tween(
                                durationMillis = 5000,
                                easing = FastOutSlowInEasing
                            )
                        )
                        animScale.animateTo(
                            targetValue = 1.1f,
                            animationSpec = tween(durationMillis = 5000, easing = LinearEasing)
                        )
                    }
                }
            }
        }
    }

    Box(
        modifier = modifier.fillMaxSize()
    ) {
        Image(
            painter = painterResource(id = backgroundImageRes),
            contentDescription = null,
            modifier = Modifier
                .fillMaxSize()
                .graphicsLayer {
                    scaleX = animScale.value
                    scaleY = animScale.value
                    translationX = animTranslationX.value
                    translationY = animTranslationY.value
                    rotationZ = animRotation.value
                    transformOrigin = TransformOrigin.Center
                },
            contentScale = ContentScale.Crop
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background.copy(alpha = 0.6f))
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .padding(horizontal = Dimension.Spacing.m)
                .imePadding()
                .verticalScroll(scrollState),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Center
        ) {
            content()
        }
    }
}