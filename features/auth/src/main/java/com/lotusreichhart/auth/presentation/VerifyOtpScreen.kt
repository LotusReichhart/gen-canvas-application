package com.lotusreichhart.auth.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.lotusreichhart.auth.presentation.components.AuthBackground
import com.lotusreichhart.auth.presentation.components.OtpInputField
import com.lotusreichhart.auth.presentation.model.OtpFlowType
import com.lotusreichhart.core.R
import com.lotusreichhart.core.ui.components.AppGradientButton
import com.lotusreichhart.core.ui.components.AppTextButton
import com.lotusreichhart.core.ui.constant.Dimension
import com.lotusreichhart.core.ui.theme.primaryGradient
import com.lotusreichhart.core.ui.theme.tertiaryGradient
import kotlinx.coroutines.delay

@Composable
fun VerifyOtpScreen(
    viewModel: AuthViewModel,
    flowType: OtpFlowType,
    onVerifySuccess: () -> Unit,
    onBackClick: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val isAnyLoading = uiState.isLoading

    val canClick = !isAnyLoading
    val canSubmit = !uiState.isOffline && !isAnyLoading

    var ticks by remember { mutableIntStateOf(60) }
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
            ticks = 60
            isTimerRunning = true
            viewModel.consumeResendSuccess()
        }
    }

    LaunchedEffect(uiState.navigationTarget) {
        when (uiState.navigationTarget) {
            is AuthNavigationTarget.MainScreen -> {
                if (flowType == OtpFlowType.SIGN_UP) {
                    onVerifySuccess()
                    viewModel.clearAllState()
                }
            }

            is AuthNavigationTarget.ResetPasswordScreen -> {
                if (flowType == OtpFlowType.FORGOT_PASSWORD) {
                    onVerifySuccess()
                    viewModel.consumeEvents()
                }
            }

            else -> {}
        }
    }

    val title = when (flowType) {
        OtpFlowType.SIGN_UP -> "Xác thực tài khoản"
        OtpFlowType.FORGOT_PASSWORD -> "Khôi phục mật khẩu"
    }

    val backgroundImageRes = when (flowType) {
        OtpFlowType.SIGN_UP -> R.drawable.digital_hand_vertical
        OtpFlowType.FORGOT_PASSWORD -> R.drawable.future_city_lock
    }

    val gradient = when (flowType) {
        OtpFlowType.SIGN_UP -> tertiaryGradient()
        OtpFlowType.FORGOT_PASSWORD -> primaryGradient()
    }

    val description = "Chúng tôi đã gửi mã xác thực gồm 6 chữ số đến email: ${uiState.email}"

    AuthBackground(backgroundImageRes = backgroundImageRes) {
        Text(
            text = title,
            color = MaterialTheme.colorScheme.onBackground,
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        Text(
            text = description,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.padding(bottom = 32.dp)
        )

        OtpInputField(
            otpValue = uiState.otpValue,
            onOtpChange = { viewModel.onOtpChange(it) },
            length = 6,
            isError = uiState.otpError != null,
            errorMessage = uiState.otpError
        )

        AppGradientButton(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.CenterHorizontally)
                .padding(top = 32.dp, bottom = 16.dp),
            text = "Xác thực",
            gradient = gradient,
            isLoading = (uiState.loadingType == AuthLoadingType.VERIFY_OTP),
            enabled = canSubmit,
            onClick = {
                when (flowType) {
                    OtpFlowType.SIGN_UP -> viewModel.onVerifySignUpClick()
                    OtpFlowType.FORGOT_PASSWORD -> viewModel.onVerifyForgotPasswordClick()
                }
            }
        )

        if (isTimerRunning) {
            Text(
                text = "Gửi lại mã sau ${ticks}s",
                color = Color.White.copy(alpha = 0.6f),
                fontSize = 14.sp
            )
        } else {
            AppTextButton(
                text = "Gửi lại mã xác thực",
                enabled = canSubmit,
                onClick = {
                    viewModel.onResendOtpClick()
                }
            )
        }

        AppTextButton(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(top = 24.dp),
            text = "Quay lại",
            textStyle = TextStyle(
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = Dimension.ButtonTitleFontSize,
                fontWeight = FontWeight.SemiBold
            ),
            enabled = canClick,
            onClick = onBackClick
        )
    }
}