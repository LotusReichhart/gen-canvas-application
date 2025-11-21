package com.lotusreichhart.auth.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.lotusreichhart.auth.presentation.components.AuthBackground
import com.lotusreichhart.auth.presentation.components.AuthTextField
import com.lotusreichhart.auth.presentation.components.DividerText
import com.lotusreichhart.auth.presentation.components.GoogleButton
import com.lotusreichhart.auth.presentation.utils.rememberGoogleSignInLauncher
import com.lotusreichhart.core.R
import com.lotusreichhart.core.ui.components.AppGradientButton
import com.lotusreichhart.core.ui.components.AppIconButton
import com.lotusreichhart.core.ui.components.AppTextButton
import com.lotusreichhart.core.ui.constant.Dimension
import com.lotusreichhart.core.ui.theme.tertiaryGradient
import com.lotusreichhart.core.utils.logE

@Composable
fun SignInScreen(
    viewModel: AuthViewModel,
    onNavigateToSignUp: () -> Unit,
    onNavigateToForgotPassword: () -> Unit,
    onNavigateToMain: () -> Unit,
    onBackClick: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val isAnyLoading = uiState.isLoading

    val canClick = !isAnyLoading
    val canSubmit = !uiState.isOffline && !isAnyLoading

    LaunchedEffect(uiState.navigationTarget) {
        when (uiState.navigationTarget) {
            is AuthNavigationTarget.MainScreen -> {
                onNavigateToMain()
                viewModel.clearAllState()
            }

            else -> {}
        }
    }

    val launchGoogleSignIn = rememberGoogleSignInLauncher(
        onSignInSuccess = { idToken ->
            viewModel.onSignInAndSignUpWithGoogleClick(idToken)
        },
        onSignInError = { errorMessage ->
            logE("Google sign in error: ", errorMessage)
        }
    )

    AuthBackground(
        backgroundImageRes = R.drawable.future_city
    ) {

        AppIconButton(
            modifier = Modifier.padding(bottom = Dimension.PaddingLarge),
            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
            onClick = onBackClick,
            tint = MaterialTheme.colorScheme.onBackground
        )

        Text(
            text = "Đăng nhập tài khoản",
            color = MaterialTheme.colorScheme.onBackground,
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        Text(
            text = "Bắt đầu hành trình sáng tạo bất tận của bạn.",
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.padding(bottom = 32.dp)
        )

        AuthTextField(
            modifier = Modifier.padding(bottom = 12.dp),
            value = uiState.email,
            onValueChange = { viewModel.onEmailChange(it) },
            label = "Email",
            icon = Icons.Default.Email,
            isError = uiState.emailError != null,
            errorMessage = uiState.emailError
        )

        AuthTextField(
            modifier = Modifier.padding(bottom = 4.dp),
            value = uiState.password,
            onValueChange = { viewModel.onPasswordChange(it) },
            label = "Mật khẩu",
            icon = Icons.Default.Lock,
            isPassword = true,
            isError = uiState.passwordError != null,
            errorMessage = uiState.passwordError
        )

        AppTextButton(
            modifier = Modifier
                .align(Alignment.End)
                .padding(bottom = 28.dp),
            text = "Quên mật khẩu?",
            textStyle = TextStyle(
                fontSize = Dimension.ButtonTitleFontSize,
                fontStyle = FontStyle.Italic
            ),
            enabled = canClick,
            onClick = onNavigateToForgotPassword
        )

        AppGradientButton(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.CenterHorizontally),
            text = "Đăng nhập",
            gradient = tertiaryGradient(),
            isLoading = (uiState.loadingType == AuthLoadingType.EMAIL_SIGN_IN),
            enabled = canSubmit,
            onClick = { viewModel.onSignInClick() },
        )

        DividerText(modifier = Modifier.padding(vertical = 24.dp))

        GoogleButton(
            text = "Đăng nhập bằng Google",
            modifier = Modifier.padding(bottom = 24.dp),
            isLoading = (uiState.loadingType == AuthLoadingType.GOOGLE_SIGN_IN),
            enabled = canSubmit,
            onClick = { launchGoogleSignIn() }
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Chưa có tài khoản?",
                fontSize = Dimension.ButtonTitleFontSize,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
            )
            Spacer(modifier = Modifier.width(6.dp))
            AppTextButton(
                text = "Đăng ký ngay",
                textStyle = TextStyle(
                    color = MaterialTheme.colorScheme.onBackground,
                    fontSize = Dimension.ButtonTitleFontSize,
                    fontWeight = FontWeight.SemiBold
                ),
                enabled = canClick,
                onClick = onNavigateToSignUp
            )
        }
    }
}