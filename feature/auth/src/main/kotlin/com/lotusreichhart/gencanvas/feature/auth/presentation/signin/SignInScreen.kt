package com.lotusreichhart.gencanvas.feature.auth.presentation.signin

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.lotusreichhart.gencanvas.core.common.util.TextResource
import com.lotusreichhart.gencanvas.core.ui.components.GenCanvasIconButton
import com.lotusreichhart.gencanvas.core.ui.components.GenCanvasTextButton
import com.lotusreichhart.gencanvas.core.ui.components.GradientButton
import com.lotusreichhart.gencanvas.core.ui.constant.Dimension
import com.lotusreichhart.gencanvas.core.ui.util.tertiaryGradient
import com.lotusreichhart.gencanvas.feature.auth.presentation.AuthSharedViewModel
import com.lotusreichhart.gencanvas.feature.auth.presentation.components.AuthBackground
import com.lotusreichhart.gencanvas.feature.auth.presentation.components.AuthTextField
import com.lotusreichhart.gencanvas.feature.auth.presentation.components.DividerText
import com.lotusreichhart.gencanvas.feature.auth.presentation.components.GoogleButton
import com.lotusreichhart.gencanvas.feature.auth.presentation.components.TermsAndPrivacyText
import com.lotusreichhart.gencanvas.feature.auth.presentation.util.rememberGoogleSignInLauncher
import timber.log.Timber
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

import com.lotusreichhart.gencanvas.feature.auth.R
import com.lotusreichhart.gencanvas.core.common.R as CoreR
import com.lotusreichhart.gencanvas.core.ui.R as CoreUiR

@Composable
internal fun SignInScreen(
    sharedViewModel: AuthSharedViewModel,
    viewModel: SignInViewModel = hiltViewModel(),
    onNavigateToSignUp: () -> Unit,
    onNavigateToForgotPassword: () -> Unit,
    onNavigateToTerms: (String) -> Unit,
    onNavigateToPrivacy: (String) -> Unit,
    isAuthSuccessful: () -> Unit,
    onDismiss: () -> Unit
) {
    val sharedUiState by sharedViewModel.uiState.collectAsStateWithLifecycle()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val isOnline by viewModel.isOnline.collectAsStateWithLifecycle()

    val isLoading = uiState.isLoading

    val canClick = !isLoading
    val canSubmit = isOnline && !isLoading

    LaunchedEffect(uiState.isSuccess) {
        if (uiState.isSuccess) {
            isAuthSuccessful()
        }
    }

    val launchGoogleSignIn = rememberGoogleSignInLauncher(
        onSignInSuccess = { idToken ->
            viewModel.onSignInWithGoogleClick(idToken)
        },
        onSignInError = { errorResource ->
            viewModel.onGoogleSignInError(errorResource)
        }
    )

    AuthBackground(
        backgroundImageRes = CoreUiR.drawable.future_city
    ) {

        GenCanvasIconButton(
            modifier = Modifier.padding(bottom = Dimension.xlPadding),
            imageVector = Icons.Default.Close,
            onClick = onDismiss,
            tint = MaterialTheme.colorScheme.onBackground
        )

        Text(
            text = stringResource(id = R.string.sign_in_title),
            color = MaterialTheme.colorScheme.onBackground,
            fontSize = Dimension.xxxlFontSize,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = Dimension.largePadding)
        )

        Text(
            text = stringResource(id = R.string.sign_in_subtitle),
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
            fontSize = Dimension.largeFontSize,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.padding(bottom = Dimension.xlPadding)
        )

        AuthTextField(
            modifier = Modifier.padding(bottom = Dimension.smallPadding),
            value = sharedUiState.email,
            onValueChange = { newValue ->
                sharedViewModel.onEmailChange(newEmail = newValue)
                viewModel.clearEmailErrorMessage()
            },
            label = stringResource(id = CoreR.string.core_email),
            icon = Icons.Default.Email,
            isError = uiState.emailErrorMessage != null,
            errorMessage = uiState.emailErrorMessage
        )

        AuthTextField(
            modifier = Modifier.padding(bottom = Dimension.xsPadding),
            value = uiState.password,
            onValueChange = { viewModel.onPasswordChange(it) },
            label = stringResource(id = CoreR.string.core_password),
            icon = Icons.Default.Lock,
            isPassword = true,
            isError = uiState.passwordErrorMessage != null,
            errorMessage = uiState.passwordErrorMessage
        )

        GenCanvasTextButton(
            modifier = Modifier
                .align(Alignment.End)
                .padding(bottom = Dimension.smallPadding),
            text = stringResource(id = R.string.forgot_password),
            textStyle = TextStyle(
                fontSize = Dimension.mediumButtonFontSize,
                fontStyle = FontStyle.Italic
            ),
            enabled = canClick,
            onClick = onNavigateToForgotPassword
        )

        GradientButton(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.CenterHorizontally),
            text = stringResource(id = CoreR.string.core_action_sign_in),
            gradient = tertiaryGradient(),
            isLoading = (uiState.loadingType == SignInLoadingType.EMAIL_SIGN_IN),
            enabled = canSubmit,
            onClick = { viewModel.onSignInClick(email = sharedUiState.email) },
        )

        DividerText(modifier = Modifier.padding(vertical = Dimension.largePadding))

        GoogleButton(
            text =stringResource(id = R.string.sign_in_google),
            modifier = Modifier.padding(bottom = Dimension.mediumPadding),
            isLoading = (uiState.loadingType == SignInLoadingType.GOOGLE_SIGN_IN),
            enabled = canSubmit,
            onClick = { launchGoogleSignIn() }
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(id = R.string.no_account),
                fontSize = Dimension.mediumButtonFontSize,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
            )
            Spacer(modifier = Modifier.width(Dimension.xsPadding))
            GenCanvasTextButton(
                text = stringResource(id = R.string.register_now),
                textStyle = TextStyle(
                    color = MaterialTheme.colorScheme.onBackground,
                    fontSize = Dimension.mediumButtonFontSize,
                    fontWeight = FontWeight.SemiBold
                ),
                enabled = canClick,
                onClick = onNavigateToSignUp
            )
        }

        TermsAndPrivacyText(
            modifier = Modifier.padding(vertical = Dimension.smallPadding),
            textBeforeTerms = TextResource.Id(R.string.sign_in_terms_prefix),
            onTermsClick = {
                if (sharedUiState.termsUrl != null) {
                    val encodedUrl =
                        URLEncoder.encode(sharedUiState.termsUrl, StandardCharsets.UTF_8.toString())
                    onNavigateToTerms(encodedUrl)
                }
            },
            onPrivacyClick = {
                if (sharedUiState.privacyUrl != null) {
                    val encodedUrl =
                        URLEncoder.encode(sharedUiState.privacyUrl, StandardCharsets.UTF_8.toString())
                    onNavigateToPrivacy(encodedUrl)
                }
            }
        )
    }
}