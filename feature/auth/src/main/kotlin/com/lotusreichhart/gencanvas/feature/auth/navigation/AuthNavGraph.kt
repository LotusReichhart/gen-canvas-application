package com.lotusreichhart.gencanvas.feature.auth.navigation

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
import com.lotusreichhart.gencanvas.core.ui.navigation.genCanvasComposable
import com.lotusreichhart.gencanvas.core.ui.navigation.routes.AuthRoute
import com.lotusreichhart.gencanvas.core.ui.navigation.routes.GenCanvasRoute
import com.lotusreichhart.gencanvas.feature.auth.presentation.AuthSharedViewModel
import com.lotusreichhart.gencanvas.feature.auth.presentation.forgot.ForgotPasswordScreen
import com.lotusreichhart.gencanvas.feature.auth.presentation.model.OtpFlowType
import com.lotusreichhart.gencanvas.feature.auth.presentation.reset.ResetPasswordScreen
import com.lotusreichhart.gencanvas.feature.auth.presentation.signin.SignInScreen
import com.lotusreichhart.gencanvas.feature.auth.presentation.signup.SignUpScreen
import com.lotusreichhart.gencanvas.feature.auth.presentation.verify.VerifyOtpScreen

import com.lotusreichhart.gencanvas.core.common.R

fun NavGraphBuilder.authGraph(
    navController: NavController,
    isAuthSuccessful: () -> Unit
) {
    navigation(
        startDestination = AuthRoute.SIGN_IN_SCREEN,
        route = GenCanvasRoute.AUTH_FLOW_ROUTE
    ) {
        genCanvasComposable(route = AuthRoute.SIGN_IN_SCREEN) { backStackEntry ->
            val sharedViewModel = backStackEntry.sharedViewModel<AuthSharedViewModel>(navController)

            SignInScreen(
                sharedViewModel = sharedViewModel,
                onNavigateToSignUp = {
                    navController.navigate(AuthRoute.SIGN_UP_SCREEN)
                },
                onNavigateToForgotPassword = {
                    navController.navigate(AuthRoute.FORGOT_PASSWORD_SCREEN)
                },
                isAuthSuccessful = isAuthSuccessful,
                onNavigateToTerms = { encodedUrl ->
                    navController.navigate(
                        GenCanvasRoute.createWebViewRoute(
                            encodedUrl,
                            titleResId = R.string.core_label_terms_of_service
                        )
                    )
                },
                onNavigateToPrivacy = { encodedUrl ->
                    navController.navigate(
                        GenCanvasRoute.createWebViewRoute(
                            encodedUrl,
                            titleResId = R.string.core_label_privacy_policy
                        )
                    )
                },
                onDismiss = {
                    navController.popBackStack()
                }
            )
        }

        genCanvasComposable(route = AuthRoute.SIGN_UP_SCREEN) { backStackEntry ->
            val sharedViewModel = backStackEntry.sharedViewModel<AuthSharedViewModel>(navController)
            SignUpScreen(
                sharedViewModel = sharedViewModel,
                onNavigateToSignIn = {
                    navController.popBackStack()
                },
                onNavigateToVerifyOtp = {
                    navController.navigate(
                        AuthRoute.createVerifyOtpRoute(OtpFlowType.SIGN_UP.name)
                    )
                },
                onNavigateToTerms = { encodedUrl ->
                    navController.navigate(
                        GenCanvasRoute.createWebViewRoute(
                            encodedUrl,
                            titleResId = R.string.core_label_terms_of_service
                        )
                    )
                },
                onNavigateToPrivacy = { encodedUrl ->
                    navController.navigate(
                        GenCanvasRoute.createWebViewRoute(
                            encodedUrl,
                            titleResId = R.string.core_label_privacy_policy
                        )
                    )
                }
            )
        }

        genCanvasComposable(route = AuthRoute.FORGOT_PASSWORD_SCREEN) { backStackEntry ->
            val sharedViewModel = backStackEntry.sharedViewModel<AuthSharedViewModel>(navController)
            ForgotPasswordScreen(
                sharedViewModel = sharedViewModel,
                onNavigateToSignIn = {
                    navController.popBackStack()
                },
                onNavigateToVerifyOtp = {
                    navController.navigate(
                        AuthRoute.createVerifyOtpRoute(OtpFlowType.FORGOT_PASSWORD.name)
                    )
                }
            )
        }

        genCanvasComposable(
            route = AuthRoute.VERIFY_OTP_SCREEN,
            arguments = listOf(
                navArgument(AuthRoute.ARG_FLOW_TYPE) { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val sharedViewModel = backStackEntry.sharedViewModel<AuthSharedViewModel>(navController)

            val flowTypeString =
                backStackEntry.arguments?.getString(AuthRoute.ARG_FLOW_TYPE)
                    ?: OtpFlowType.SIGN_UP.name
            val flowType = OtpFlowType.valueOf(flowTypeString)

            VerifyOtpScreen(
                sharedViewModel = sharedViewModel,
                flowType = flowType,
                onVerifySuccess = { token ->
                    when (flowType) {
                        OtpFlowType.SIGN_UP -> isAuthSuccessful()
                        OtpFlowType.FORGOT_PASSWORD -> {
                            if (token != null) {
                                sharedViewModel.onResetTokenChange(token)
                            }
                            navController.navigate(AuthRoute.RESET_PASSWORD_SCREEN) {
                                popUpTo(AuthRoute.VERIFY_OTP_SCREEN) {
                                    inclusive = true
                                }
                            }
                        }
                    }
                },
                onDismiss = {
                    navController.popBackStack()
                }
            )
        }

        genCanvasComposable(route = AuthRoute.RESET_PASSWORD_SCREEN) { backStackEntry ->
            val sharedViewModel = backStackEntry.sharedViewModel<AuthSharedViewModel>(navController)
            ResetPasswordScreen(
                sharedViewModel = sharedViewModel,
                onNavigateToSignIn = {
                    navController.navigate(AuthRoute.SIGN_IN_SCREEN) {
                        popUpTo(AuthRoute.SIGN_IN_SCREEN) {
                            inclusive = false
                        }
                        launchSingleTop = true
                    }
                },
                onDismiss = {
                    navController.popBackStack()
                }
            )
        }
    }
}

@Composable
inline fun <reified T : ViewModel> NavBackStackEntry.sharedViewModel(
    navController: NavController,
    parentRoute: String = GenCanvasRoute.AUTH_FLOW_ROUTE
): T {
    val navGraphRoute = destination.parent?.route ?: parentRoute
    val parentEntry = remember(this) {
        navController.getBackStackEntry(navGraphRoute)
    }
    return hiltViewModel(parentEntry)
}