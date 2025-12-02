package com.lotusreichhart.gencanvas.presentation.model

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
import androidx.compose.ui.graphics.vector.ImageVector
import com.lotusreichhart.gencanvas.R
import com.lotusreichhart.gencanvas.core.ui.navigation.routes.MainTabRoute

internal data class BottomNavItem(
    val title: Int,
    val icon: ImageVector,
    val route: String
)

internal val bottomNavItems = listOf(
    BottomNavItem(R.string.nav_home, Icons.Default.Home, MainTabRoute.HOME_TAB_ROUTE),
    BottomNavItem(R.string.nav_ai_gen, Icons.Default.Star, MainTabRoute.AI_GENERATE_TAB_ROUTE),
    BottomNavItem(R.string.nav_canvas, Icons.Default.Favorite, MainTabRoute.CANVAS_TAB_ROUTE),
    BottomNavItem(R.string.nav_account, Icons.Default.Person, MainTabRoute.ACCOUNT_TAB_ROUTE)
)