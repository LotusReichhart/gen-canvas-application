package com.lotusreichhart.auth.presentation

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLinkStyles
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withLink
import androidx.compose.ui.text.withStyle
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
import com.lotusreichhart.core.ui.components.AppTextButton
import com.lotusreichhart.core.ui.constant.Dimension
import com.lotusreichhart.core.ui.theme.primaryGradient
import com.lotusreichhart.core.utils.logD
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@Composable
fun SignUpScreen(
    viewModel: AuthViewModel,
    onNavigateToSignIn: () -> Unit,
    onNavigateToVerifyOtp: () -> Unit,
    onNavigateToMain: () -> Unit,
    onNavigateToTerms: (String) -> Unit,
    onNavigateToPrivacy: (String) -> Unit
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

            is AuthNavigationTarget.VerifyOtpScreen -> {
                onNavigateToVerifyOtp()
                viewModel.consumeEvents()
            }

            else -> {}
        }
    }

    val launchGoogleSignIn = rememberGoogleSignInLauncher(
        onSignInSuccess = { idToken ->
            viewModel.onSignInAndSignUpWithGoogleClick(idToken)
        },
        onSignInError = { errorMessage ->
        }
    )

    AuthBackground(
        backgroundImageRes = R.drawable.digital_hand_vertical
    ) {
        Text(
            text = "Đăng ký tài khoản",
            color = MaterialTheme.colorScheme.onBackground,
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        Text(
            text = "Tạo tài khoản miễn phí để khởi đầu ngay.",
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.padding(bottom = 32.dp)
        )

        AuthTextField(
            modifier = Modifier.padding(bottom = 12.dp),
            value = uiState.name,
            onValueChange = { viewModel.onNameChange(it) },
            label = "Họ Tên",
            icon = Icons.Default.Person,
            isError = uiState.nameError != null,
            errorMessage = uiState.nameError
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
            modifier = Modifier.padding(bottom = Dimension.PaddingNormal),
            value = uiState.password,
            onValueChange = { viewModel.onPasswordChange(it) },
            label = "Mật khẩu",
            icon = Icons.Default.Lock,
            isPassword = true,
            isError = uiState.passwordError != null,
            errorMessage = uiState.passwordError
        )

        TermsAndPrivacyText(
            modifier = Modifier.padding(bottom = 28.dp),
            onTermsClick = {
                val encodedUrl =
                    URLEncoder.encode(uiState.termsUrl, StandardCharsets.UTF_8.toString())
                onNavigateToTerms(encodedUrl)
            },
            onPrivacyClick = {
                val encodedUrl =
                    URLEncoder.encode(uiState.privacyUrl, StandardCharsets.UTF_8.toString())
                onNavigateToPrivacy(encodedUrl)
            }
        )

        AppGradientButton(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.CenterHorizontally),
            text = "Tiếp tục",
            gradient = primaryGradient(),
            isLoading = (uiState.loadingType == AuthLoadingType.REQUEST_SIGN_UP),
            enabled = canSubmit,
            onClick = { viewModel.onRequestSignUpClick() },
        )

        DividerText(modifier = Modifier.padding(vertical = 24.dp))

        GoogleButton(
            text = "Đăng ký bằng Google",
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
                text = "Bạn đã có tài khoản?",
                fontSize = 17.sp,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
            )
            Spacer(modifier = Modifier.width(6.dp))
            AppTextButton(
                text = "Đăng nhập",
                textStyle = TextStyle(
                    color = MaterialTheme.colorScheme.onBackground,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.SemiBold
                ),
                enabled = canClick,
                onClick = onNavigateToSignIn
            )
        }
    }
}

@Composable
private fun TermsAndPrivacyText(
    onTermsClick: () -> Unit,
    onPrivacyClick: () -> Unit,
    modifier: Modifier = Modifier,
    baseTextColor: Color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
    linkColor: Color = MaterialTheme.colorScheme.onBackground
) {
    val linkStyle = SpanStyle(
        color = linkColor,
        fontWeight = FontWeight.Bold,
        textDecoration = TextDecoration.Underline
    )

    val annotatedString = buildAnnotatedString {
        withStyle(style = SpanStyle(color = baseTextColor)) {
            append("Bằng việc tiếp tục, bạn đồng ý với ")
        }

        val termsLink = LinkAnnotation.Clickable(
            tag = "terms",
            styles = TextLinkStyles(style = linkStyle),
            linkInteractionListener = {
                logD("Click vào Điều khoản dịch vụ")
                onTermsClick()
            }
        )
        withLink(termsLink) {
            append("Điều khoản dịch vụ")
        }

        withStyle(style = SpanStyle(color = baseTextColor)) {
            append(" và ")
        }

        val privacyLink = LinkAnnotation.Clickable(
            tag = "privacy",
            styles = TextLinkStyles(style = linkStyle),
            linkInteractionListener = {
                logD("Click vào Chính sách bảo mật")
                onPrivacyClick()
            }
        )
        withLink(privacyLink) {
            append("Chính sách bảo mật")
        }

        withStyle(style = SpanStyle(color = baseTextColor)) {
            append(" của chúng tôi.")
        }
    }

    Text(
        text = annotatedString,
        modifier = modifier,
        style = MaterialTheme.typography.bodySmall.copy(
            textAlign = TextAlign.Start,
            fontSize = Dimension.NormalFontSize,
            lineHeight = 23.sp
        )
    )
}