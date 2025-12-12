package com.lotusreichhart.gencanvas.feature.account.navigation

import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.navigation
import com.lotusreichhart.gencanvas.core.ui.navigation.genCanvasComposable
import com.lotusreichhart.gencanvas.core.ui.navigation.routes.AccountRoute
import com.lotusreichhart.gencanvas.core.ui.navigation.routes.StudioRoute
import com.lotusreichhart.gencanvas.core.ui.navigation.routes.GenCanvasRoute
import com.lotusreichhart.gencanvas.core.ui.navigation.routes.MainTabRoute
import com.lotusreichhart.gencanvas.feature.account.presentation.AccountTab
import com.lotusreichhart.gencanvas.feature.account.presentation.profile.ProfileScreen
import com.lotusreichhart.gencanvas.feature.account.presentation.profile.edit.EditProfileScreen

fun NavGraphBuilder.accountTabGraph(
    onNavigateToAuth: () -> Unit,
    onNavigateToProfile: () -> Unit
) {
    navigation(
        startDestination = AccountRoute.ACCOUNT_TAB,
        route = MainTabRoute.ACCOUNT_TAB_ROUTE
    ) {
        genCanvasComposable(route = AccountRoute.ACCOUNT_TAB) {
            AccountTab(
                onNavigateToAuth = onNavigateToAuth,
                onNavigateToProfile = onNavigateToProfile
            )
        }
    }
}

fun NavGraphBuilder.accountGraph(
    navController: NavController
) {
    navigation(
        startDestination = AccountRoute.PROFILE_SCREEN,
        route = GenCanvasRoute.ACCOUNT_FLOW_ROUTE
    ) {
        genCanvasComposable(AccountRoute.PROFILE_SCREEN) {
            ProfileScreen(
                onDetailAvatar = { encodedUrl ->
                    if (encodedUrl != null) {
                        navController.navigate(
                            route = GenCanvasRoute.createImageViewerRoute(encodedUrl)
                        )
                    }
                },
                onNavigateToEditProfile = {
                    navController.navigate(AccountRoute.EDIT_PROFILE_SCREEN)
                },
                onDismiss = { navController.popBackStack() }
            )
        }

        genCanvasComposable(AccountRoute.EDIT_PROFILE_SCREEN) { backStackEntry ->

            val editedResult by backStackEntry.savedStateHandle
                .getStateFlow<String?>(StudioRoute.KEY_EDITED_IMAGE_RESULT, null)
                .collectAsStateWithLifecycle()

            EditProfileScreen(
                onDismiss = { navController.popBackStack() },
                editedImageResult = editedResult,
                onConsumeEditedImageResult = {
                    backStackEntry.savedStateHandle.remove<String>(StudioRoute.KEY_EDITED_IMAGE_RESULT)
                },
                onNavigateToCamera = {
                    navController.navigate(GenCanvasRoute.CAMERA_FLOW_ROUTE)
                },
                onNavigateToEditor = { uri ->
                    navController.navigate(
                        route = StudioRoute.createStudioRoute(uri)
                    )
                }
            )
        }
    }
}