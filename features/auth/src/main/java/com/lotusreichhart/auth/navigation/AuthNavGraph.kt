package com.lotusreichhart.auth.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.lotusreichhart.auth.presentation.AuthViewModel
import com.lotusreichhart.auth.presentation.ForgotPasswordScreen
import com.lotusreichhart.auth.presentation.ResetPasswordScreen
import com.lotusreichhart.auth.presentation.SignInScreen
import com.lotusreichhart.auth.presentation.SignUpScreen
import com.lotusreichhart.auth.presentation.VerifyOtpScreen
import com.lotusreichhart.auth.presentation.model.OtpFlowType
import com.lotusreichhart.core.navigation.Route
import com.lotusreichhart.core.navigation.appComposable

fun NavGraphBuilder.authGraph(
    navController: NavController,
    onNavigateToMain: () -> Unit
) {

    navigation(
        startDestination = Route.SIGN_IN_SCREEN,
        route = Route.AUTH_FLOW_ROUTE
    ) {
        appComposable(route = Route.SIGN_IN_SCREEN) { backStackEntry ->

            val viewModel = backStackEntry.sharedViewModel<AuthViewModel>(navController)

            SignInScreen(
                viewModel = viewModel,
                onNavigateToSignUp = {
                    navController.navigate(Route.SIGN_UP_SCREEN)
                },
                onNavigateToForgotPassword = {
                    navController.navigate(Route.FORGOT_PASSWORD_SCREEN)
                },
                onNavigateToMain = onNavigateToMain
            )
        }

        appComposable(route = Route.SIGN_UP_SCREEN) { backStackEntry ->
            val viewModel = backStackEntry.sharedViewModel<AuthViewModel>(navController)
            SignUpScreen(
                viewModel = viewModel,
                onNavigateToSignIn = {
                    navController.popBackStack()
                },
                onNavigateToVerifyOtp = {
                    navController.navigate(
                        Route.createVerifyOtpRoute(OtpFlowType.SIGN_UP.name)
                    )
                },
                onNavigateToMain = onNavigateToMain,
                onNavigateToTerms = { encodedUrl ->
                    navController.navigate(Route.createWebViewRoute(encodedUrl, "Điều khoản dịch vụ"))
                },
                onNavigateToPrivacy = { encodedUrl ->
                    navController.navigate(Route.createWebViewRoute(encodedUrl, "Chính sách bảo mật"))
                }
            )
        }

        appComposable(route = Route.FORGOT_PASSWORD_SCREEN) { backStackEntry ->
            val viewModel = backStackEntry.sharedViewModel<AuthViewModel>(navController)
            ForgotPasswordScreen(
                viewModel = viewModel,
                onNavigateToSignIn = {
                    navController.popBackStack()
                },
                onNavigateToVerifyOtp = {
                    navController.navigate(
                        Route.createVerifyOtpRoute(OtpFlowType.FORGOT_PASSWORD.name)
                    )
                }
            )
        }

        appComposable(
            route = Route.VERIFY_OTP_SCREEN_ROUTE,
            arguments = listOf(
                navArgument(Route.ARG_FLOW_TYPE) { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val viewModel = backStackEntry.sharedViewModel<AuthViewModel>(navController)

            val flowTypeString =
                backStackEntry.arguments?.getString(Route.ARG_FLOW_TYPE) ?: OtpFlowType.SIGN_UP.name
            val flowType = OtpFlowType.valueOf(flowTypeString)

            VerifyOtpScreen(
                viewModel = viewModel,
                flowType = flowType,
                onVerifySuccess = {
                    when (flowType) {
                        OtpFlowType.SIGN_UP -> onNavigateToMain()
                        OtpFlowType.FORGOT_PASSWORD -> navController.navigate(Route.RESET_PASSWORD_SCREEN)
                    }
                },
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }

        appComposable(route = Route.RESET_PASSWORD_SCREEN) { backStackEntry ->
            val viewModel = backStackEntry.sharedViewModel<AuthViewModel>(navController)
            ResetPasswordScreen(
                viewModel = viewModel,
                onNavigateToSignIn = {
                    navController.navigate(Route.SIGN_IN_SCREEN)
                },
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }
    }
}

@Composable
inline fun <reified T : ViewModel> NavBackStackEntry.sharedViewModel(
    navController: NavController,
    parentRoute: String = Route.AUTH_FLOW_ROUTE
): T {
    val navGraphRoute = destination.parent?.route ?: parentRoute
    val parentEntry = remember(this) {
        navController.getBackStackEntry(navGraphRoute)
    }
    return hiltViewModel(parentEntry)
}