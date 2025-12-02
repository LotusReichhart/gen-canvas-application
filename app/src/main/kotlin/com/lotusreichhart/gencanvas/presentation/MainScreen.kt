package com.lotusreichhart.gencanvas.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults.bottomAppBarFabElevation
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.lotusreichhart.gencanvas.core.ui.util.primaryGradient
import com.lotusreichhart.gencanvas.feature.account.navigation.accountTabGraph
import com.lotusreichhart.gencanvas.feature.home.navigation.homeTabGraph
import com.lotusreichhart.gencanvas.presentation.components.CutoutBottomAppBar
import com.lotusreichhart.gencanvas.presentation.model.BottomNavItem
import com.lotusreichhart.gencanvas.presentation.model.bottomNavItems

@Composable
fun MainScreen(
    onNavigateToAuth: () -> Unit,
    onNavigateToProfile: () -> Unit,
) {
    val mainContentNavController = rememberNavController()

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {

                },
                shape = CircleShape,
                containerColor = Color.Transparent,
                contentColor = Color.White,
                elevation = bottomAppBarFabElevation(0.dp),
                modifier = Modifier
                    .offset(y = 40.dp)
                    .background(
                        brush = primaryGradient(),
                        shape = CircleShape
                    )
            ) {
                Icon(Icons.Default.Menu, contentDescription = "Mở rộng", tint = Color.White)
            }
        },
        floatingActionButtonPosition = FabPosition.Center,
        bottomBar = {
            CutoutBottomAppBar {
                val navBackStackEntry by mainContentNavController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination

                BottomBarTabItem(
                    item = bottomNavItems[0],
                    isSelected = currentDestination?.hierarchy?.any { it.route == bottomNavItems[0].route } == true,
                    onClick = {
                        navigateToTab(
                            mainContentNavController,
                            bottomNavItems[0].route
                        )
                    }
                )
                BottomBarTabItem(
                    item = bottomNavItems[1],
                    isSelected = currentDestination?.hierarchy?.any { it.route == bottomNavItems[1].route } == true,
                    onClick = {
                        navigateToTab(
                            mainContentNavController,
                            bottomNavItems[1].route
                        )
                    }
                )

                Spacer(Modifier.width(48.dp))

                BottomBarTabItem(
                    item = bottomNavItems[2],
                    isSelected = currentDestination?.hierarchy?.any { it.route == bottomNavItems[2].route } == true,
                    onClick = {
                        navigateToTab(
                            mainContentNavController,
                            bottomNavItems[2].route
                        )
                    }
                )

                BottomBarTabItem(
                    item = bottomNavItems[3],
                    isSelected = currentDestination?.hierarchy?.any { it.route == bottomNavItems[3].route } == true,
                    onClick = {
                        navigateToTab(
                            mainContentNavController,
                            bottomNavItems[3].route
                        )
                    }
                )
            }
        }
    ) { innerPadding ->

        NavHost(
            navController = mainContentNavController,
            startDestination = bottomNavItems[0].route,
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = innerPadding.calculateBottomPadding())
        ) {
            homeTabGraph(
                onNavigateToAuth = onNavigateToAuth,
                onNavigateToProfile = onNavigateToProfile
            )

            composable(bottomNavItems[1].route) {
                DefaultScreen(bottomNavItems[1].title.toString())
            }
            composable(bottomNavItems[2].route) {
                DefaultScreen(bottomNavItems[2].title.toString())
            }

            accountTabGraph(
                onNavigateToAuth = onNavigateToAuth,
                onNavigateToProfile = onNavigateToProfile
            )
        }
    }
}

@Composable
private fun DefaultScreen(screenTitle: String) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Đây là màn hình $screenTitle",
            style = MaterialTheme.typography.headlineMedium
        )
    }
}

@Composable
private fun BottomBarTabItem(
    item: BottomNavItem,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val title = stringResource(id = item.title)

    val color =
        if (isSelected) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurface.copy(
            alpha = 0.5f
        )

    Column(
        modifier = Modifier
            .clickable(
                onClick = onClick,
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            )
            .padding(vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(imageVector = item.icon, contentDescription = title, tint = color)
        Spacer(Modifier.height(4.dp))
        Text(
            text = title,
            color = color,
            fontSize = 11.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
            textAlign = TextAlign.Center,
            maxLines = 1
        )
    }
}

private fun navigateToTab(navController: NavController, route: String) {
    navController.navigate(route) {
        popUpTo(navController.graph.findStartDestination().id) {
            saveState = true
        }
        launchSingleTop = true
        restoreState = true
    }
}