package com.lotusreichhart.gencanvas.core.ui.anim

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically

object NavigationAnimations {

    private const val STANDARD_DURATION = 350
    private const val EMPHASIZED_DURATION = 600

    fun enterSlideInFromRight(): (Any?) -> EnterTransition = {
        slideInHorizontally(
            initialOffsetX = { it },
            animationSpec = tween(STANDARD_DURATION)
        ) + fadeIn(animationSpec = tween(STANDARD_DURATION))
    }

    fun exitSlideOutToLeft(): (Any?) -> ExitTransition = {
        slideOutHorizontally(
            targetOffsetX = { -it },
            animationSpec = tween(STANDARD_DURATION)
        ) + fadeOut(animationSpec = tween(STANDARD_DURATION))
    }

    fun popEnterSlideInFromLeft(): (Any?) -> EnterTransition = {
        slideInHorizontally(
            initialOffsetX = { -it },
            animationSpec = tween(STANDARD_DURATION)
        ) + fadeIn(animationSpec = tween(STANDARD_DURATION))
    }

    fun popExitSlideOutToRight(): (Any?) -> ExitTransition = {
        slideOutHorizontally(
            targetOffsetX = { it },
            animationSpec = tween(STANDARD_DURATION)
        ) + fadeOut(animationSpec = tween(STANDARD_DURATION))
    }

    fun enterZoomIn(): (Any?) -> EnterTransition = {
        scaleIn(
            initialScale = 0.1f,
            animationSpec = tween(EMPHASIZED_DURATION)
        ) + fadeIn(animationSpec = tween(EMPHASIZED_DURATION))
    }

    fun exitZoomOut(): (Any?) -> ExitTransition = {
        scaleOut(
            targetScale = 0.6f,
            animationSpec = tween(EMPHASIZED_DURATION)
        ) + fadeOut(animationSpec = tween(EMPHASIZED_DURATION))
    }

    fun enterFadeIn(): (Any?) -> EnterTransition = {
        fadeIn(animationSpec = tween(STANDARD_DURATION))
    }

    fun exitFadeOut(): (Any?) -> ExitTransition = {
        fadeOut(animationSpec = tween(STANDARD_DURATION))
    }

    fun enterSlideInFromBottom(): (Any?) -> EnterTransition = {
        slideInVertically(
            initialOffsetY = { it },
            animationSpec = tween(EMPHASIZED_DURATION)
        ) + fadeIn(animationSpec = tween(EMPHASIZED_DURATION))
    }

    fun exitSlideOutToBottom(): (Any?) -> ExitTransition = {
        slideOutVertically(
            targetOffsetY = { it },
            animationSpec = tween(EMPHASIZED_DURATION)
        ) + fadeOut(animationSpec = tween(EMPHASIZED_DURATION))
    }
}