package com.lotusreichhart.gencanvas.feature.auth.presentation.verify

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.lotusreichhart.gencanvas.core.ui.components.GenCanvasTextButton
import com.lotusreichhart.gencanvas.core.ui.components.GradientButton
import com.lotusreichhart.gencanvas.core.ui.constant.Dimension
import com.lotusreichhart.gencanvas.core.ui.util.primaryGradient
import com.lotusreichhart.gencanvas.core.ui.util.tertiaryGradient
import com.lotusreichhart.gencanvas.feature.auth.presentation.AuthSharedViewModel
import com.lotusreichhart.gencanvas.feature.auth.presentation.model.OtpFlowType
import kotlinx.coroutines.delay

import com.lotusreichhart.gencanvas.feature.auth.presentation.components.AuthBackground
import com.lotusreichhart.gencanvas.feature.auth.presentation.components.OtpInputField

import com.lotusreichhart.gencanvas.feature.auth.R
import com.lotusreichhart.gencanvas.core.common.R as CoreR
import com.lotusreichhart.gencanvas.core.ui.R as CoreUiR

@Composable
internal fun VerifyOtpScreen(
    sharedViewModel: AuthSharedViewModel,
    viewModel: VerifyViewModel = hiltViewModel(),
    flowType: OtpFlowType,
    onVerifySuccess: (String?) -> Unit,
    onDismiss: () -> Unit
) {
    val sharedUiState by sharedViewModel.uiState.collectAsStateWithLifecycle()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val isOnline by viewModel.isOnline.collectAsStateWithLifecycle()

    val isLoading = uiState.isLoading

    val canClick = !isLoading
    val canSubmit = isOnline && !isLoading

    var ticks by remember { mutableIntStateOf(59) }
    var isTimerRunning by remember { mutableStateOf(true) }

    LaunchedEffect(isTimerRunning, ticks) {
        if (isTimerRunning && ticks > 0) {
            delay(1000)
            ticks--
        } else if (ticks == 0) {
            isTimerRunning = false
        }
    }

    LaunchedEffect(uiState.isResendSuccess) {
        if (uiState.isResendSuccess) {
            ticks = 59
            isTimerRunning = true
            viewModel.onOtpChange("")
        }
    }

    LaunchedEffect(uiState.navigationTarget) {
        when (uiState.navigationTarget) {
            is NavigationTarget.MainScreen -> {
                if (flowType == OtpFlowType.SIGN_UP) {
                    onVerifySuccess(null)
                }
            }

            is NavigationTarget.ResetPasswordScreen -> {
                if (flowType == OtpFlowType.FORGOT_PASSWORD) {
                    onVerifySuccess(uiState.resetToken)
                }
            }

            else -> {}
        }
    }

    val titleResId = when (flowType) {
        OtpFlowType.SIGN_UP -> R.string.verify_account_title
        OtpFlowType.FORGOT_PASSWORD -> R.string.reset_password_title
    }

    val backgroundImageRes = when (flowType) {
        OtpFlowType.SIGN_UP -> CoreUiR.drawable.digital_hand_vertical
        OtpFlowType.FORGOT_PASSWORD -> CoreUiR.drawable.future_city_lock
    }

    val gradient = when (flowType) {
        OtpFlowType.SIGN_UP -> tertiaryGradient()
        OtpFlowType.FORGOT_PASSWORD -> primaryGradient()
    }

    val description = stringResource(id = R.string.otp_sent_description, sharedUiState.email)

    AuthBackground(backgroundImageRes = backgroundImageRes) {
        Text(
            text = stringResource(id = titleResId),
            color = MaterialTheme.colorScheme.onBackground,
            fontSize = Dimension.xxxlFontSize,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = Dimension.largePadding)
        )

        Text(
            text = description,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
            fontSize = Dimension.largeFontSize,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.padding(bottom = Dimension.xlPadding)
        )

        OtpInputField(
            otpValue = uiState.otp,
            onOtpChange = { viewModel.onOtpChange(it) },
            length = 6,
            isError = uiState.otpErrorMessage != null,
            errorMessage = uiState.otpErrorMessage
        )

        GradientButton(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.CenterHorizontally)
                .padding(top = Dimension.xlPadding, bottom = Dimension.smallPadding),
            text = stringResource(id = CoreR.string.core_action_verify),
            gradient = gradient,
            isLoading = isLoading,
            enabled = canSubmit,
            onClick = {
                when (flowType) {
                    OtpFlowType.SIGN_UP -> {
                        viewModel.onVerifySignUpClick(email = sharedUiState.email)
                    }

                    OtpFlowType.FORGOT_PASSWORD -> {
                        viewModel.onVerifyForgotPasswordClick(email = sharedUiState.email)
                    }
                }
            }
        )

        if (isTimerRunning) {
            Text(
                text = stringResource(id = R.string.resend_timer, ticks),
                color = Color.White.copy(alpha = 0.6f),
                fontSize = Dimension.mediumFontSize
            )
        } else {
            GenCanvasTextButton(
                text = stringResource(id = R.string.action_resend_otp),
                enabled = canSubmit,
                onClick = {
                    viewModel.onResendOtpClick(sharedUiState.email)
                }
            )
        }

        GenCanvasTextButton(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(top = Dimension.mediumPadding),
            text = stringResource(id = CoreR.string.core_action_back),
            textStyle = TextStyle(
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = Dimension.mediumButtonFontSize,
                fontWeight = FontWeight.SemiBold
            ),
            enabled = canClick,
            onClick = onDismiss
        )
    }
}