package com.lotusreichhart.auth.presentation

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.lotusreichhart.auth.presentation.components.AuthBackground
import com.lotusreichhart.auth.presentation.components.AuthTextField
import com.lotusreichhart.core.R
import com.lotusreichhart.core.ui.components.AppGradientButton
import com.lotusreichhart.core.ui.components.AppTextButton
import com.lotusreichhart.core.ui.constant.Dimension
import com.lotusreichhart.core.ui.theme.tertiaryGradient

@Composable
fun ResetPasswordScreen(
    viewModel: AuthViewModel,
    onNavigateToSignIn: () -> Unit,
    onBackClick: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val isAnyLoading = uiState.isLoading

    val canClick = !isAnyLoading
    val canSubmit = !uiState.isOffline && !isAnyLoading

    LaunchedEffect(uiState.navigationTarget) {
        when (uiState.navigationTarget) {
            is AuthNavigationTarget.SignInScreen -> {
                onNavigateToSignIn()
                viewModel.consumeEvents()
            }

            else -> {}
        }
    }

    AuthBackground(
        backgroundImageRes = R.drawable.future_city_lock
    ) {
        Text(
            text = "Đặt lại mật khẩu",
            color = MaterialTheme.colorScheme.onBackground,
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        Text(
            text = "Tạo lại mật khẩu mới cho tài khoản của bạn.",
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.padding(bottom = 32.dp)
        )

        AuthTextField(
            modifier = Modifier.padding(bottom = 12.dp),
            value = uiState.password,
            onValueChange = { viewModel.onPasswordChange(it) },
            label = "Mật khẩu",
            icon = Icons.Default.Lock,
            isPassword = true,
            isError = uiState.passwordError != null,
            errorMessage = uiState.passwordError
        )

        AuthTextField(
            modifier = Modifier.padding(bottom = 28.dp),
            value = uiState.confirmPassword,
            onValueChange = { viewModel.onConfirmPasswordChange(it) },
            label = "Xác nhận mật khẩu",
            icon = Icons.Default.Lock,
            isPassword = true,
            isError = uiState.confirmPasswordError != null,
            errorMessage = uiState.confirmPasswordError
        )

        AppGradientButton(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.CenterHorizontally)
                .padding(bottom = 24.dp),
            text = "Lưu mật khẩu",
            gradient = tertiaryGradient(),
            isLoading = (uiState.loadingType == AuthLoadingType.RESET_PASSWORD),
            enabled = canSubmit,
            onClick = { viewModel.onResetPasswordClick() },
        )

        AppTextButton(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            text = "Trở về đăng nhập",
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