package com.lotusreichhart.gencanvas.presentation.model

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
import androidx.compose.ui.graphics.vector.ImageVector
import com.lotusreichhart.gencanvas.core.ui.navigation.routes.MainTabRoute

data class BottomNavItem(
    val title: String,
    val icon: ImageVector,
    val route: String
)

val bottomNavItems = listOf(
    BottomNavItem("Trang chủ", Icons.Default.Home, MainTabRoute.HOME_TAB),
    BottomNavItem("Tạo Ảnh", Icons.Default.Star, MainTabRoute.AI_GENERATE_TAB),
    BottomNavItem("Canvas", Icons.Default.Favorite, MainTabRoute.CANVAS_TAB),
    BottomNavItem("Tài khoản", Icons.Default.Person, MainTabRoute.ACCOUNT_TAB)
)