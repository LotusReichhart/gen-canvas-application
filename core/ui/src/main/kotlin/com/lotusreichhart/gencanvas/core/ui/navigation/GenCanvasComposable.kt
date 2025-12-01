package com.lotusreichhart.gencanvas.core.ui.navigation

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.runtime.Composable
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavDeepLink
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.lotusreichhart.gencanvas.core.ui.anim.NavigationAnimations

fun NavGraphBuilder.genCanvasComposable(
    route: String,
    arguments: List<NamedNavArgument> = emptyList(),
    deepLinks: List<NavDeepLink> = emptyList(),

    // Chuyển vào ( Đè lên màn khác )
    enterTransition: (AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition?)? = {
        when {
            RouteTransition.isZoomRoute(targetState.destination.route) ->
                NavigationAnimations.enterZoomIn().invoke(this)

            RouteTransition.isSlideVerticalRoute(targetState.destination.route) ->
                NavigationAnimations.enterSlideInFromBottom().invoke(this)

            else -> NavigationAnimations.enterSlideInFromRight().invoke(this)
        }
    },

    // Chuyển ra ( Bị màn khác đè lên )
    exitTransition: (AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition?)? = {
        when {
            RouteTransition.isZoomRoute(targetState.destination.route) ->
                NavigationAnimations.exitFadeOut().invoke(this)

            RouteTransition.isSlideVerticalRoute(targetState.destination.route) ->
                NavigationAnimations.exitFadeOut().invoke(this)

            else -> NavigationAnimations.exitSlideOutToLeft().invoke(this)
        }
    },

    // Chuyển vào ( Màn đè lên bị hủy )
    popEnterTransition: (AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition?)? = {
        when {
            RouteTransition.isZoomRoute(initialState.destination.route) ->
                NavigationAnimations.enterFadeIn().invoke(this)

            RouteTransition.isSlideVerticalRoute(initialState.destination.route) ->
                NavigationAnimations.enterFadeIn().invoke(this)

            else -> NavigationAnimations.popEnterSlideInFromLeft().invoke(this)
        }
    },

    // Chuyển ra ( Hủy màn hình )
    popExitTransition: (AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition?)? = {
        when {
            RouteTransition.isZoomRoute(initialState.destination.route) ->
                NavigationAnimations.exitZoomOut().invoke(this)

            RouteTransition.isSlideVerticalRoute(initialState.destination.route) ->
                NavigationAnimations.exitSlideOutToBottom().invoke(this)

            else -> NavigationAnimations.popExitSlideOutToRight().invoke(this)
        }
    },

    content: @Composable AnimatedContentScope.(NavBackStackEntry) -> Unit
) {
    composable(
        route = route,
        arguments = arguments,
        deepLinks = deepLinks,
        enterTransition = enterTransition,
        exitTransition = exitTransition,
        popEnterTransition = popEnterTransition,
        popExitTransition = popExitTransition,
        content = content
    )
}