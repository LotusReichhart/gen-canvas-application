package com.lotusreichhart.gencanvas.feature.auth.presentation.signup

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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.lotusreichhart.gencanvas.core.ui.components.GenCanvasTextButton
import com.lotusreichhart.gencanvas.core.ui.components.GradientButton
import com.lotusreichhart.gencanvas.core.ui.constant.Dimension
import com.lotusreichhart.gencanvas.core.ui.util.primaryGradient
import com.lotusreichhart.gencanvas.feature.auth.presentation.AuthSharedViewModel
import com.lotusreichhart.gencanvas.feature.auth.presentation.components.AuthBackground
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

import com.lotusreichhart.gencanvas.feature.auth.presentation.components.AuthTextField
import com.lotusreichhart.gencanvas.feature.auth.presentation.components.TermsAndPrivacyText

import com.lotusreichhart.gencanvas.feature.auth.R
import com.lotusreichhart.gencanvas.core.common.R as CoreR
import com.lotusreichhart.gencanvas.core.ui.R as CoreUiR

@Composable
internal fun SignUpScreen(
    sharedViewModel: AuthSharedViewModel,
    viewModel: SignUpViewModel = hiltViewModel(),
    onNavigateToSignIn: () -> Unit,
    onNavigateToVerifyOtp: () -> Unit,
    onNavigateToTerms: (String) -> Unit,
    onNavigateToPrivacy: (String) -> Unit
) {
    val sharedUiState by sharedViewModel.uiState.collectAsStateWithLifecycle()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val isOnline by viewModel.isOnline.collectAsStateWithLifecycle()

    val isLoading = uiState.isLoading

    val canClick = !isLoading
    val canSubmit = isOnline && !isLoading

    LaunchedEffect(uiState.isSuccess) {
        if (uiState.isSuccess) {
            onNavigateToVerifyOtp()
            viewModel.resetSuccess()
        }
    }

    AuthBackground(
        backgroundImageRes = CoreUiR.drawable.digital_hand_vertical
    ) {
        Text(
            text = stringResource(id = R.string.sign_up_title),
            color = MaterialTheme.colorScheme.onBackground,
            fontSize = Dimension.TextSize.headlineLarge,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = Dimension.Spacing.l)
        )

        Text(
            text = stringResource(id = R.string.sign_up_subtitle),
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
            fontSize = Dimension.TextSize.bodyLarge,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.padding(bottom = Dimension.Spacing.xxl)
        )

        AuthTextField(
            modifier = Modifier.padding(bottom = Dimension.Spacing.l),
            value = uiState.name,
            onValueChange = { viewModel.onNameChange(it) },
            label = stringResource(id = CoreR.string.core_name),
            icon = Icons.Default.Person,
            isError = uiState.nameErrorMessage != null,
            errorMessage = uiState.nameErrorMessage
        )

        AuthTextField(
            modifier = Modifier.padding(bottom = Dimension.Spacing.l),
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
            value = uiState.password,
            onValueChange = { viewModel.onPasswordChange(it) },
            label = stringResource(id = CoreR.string.core_password),
            icon = Icons.Default.Lock,
            isPassword = true,
            isError = uiState.passwordErrorMessage != null,
            errorMessage = uiState.passwordErrorMessage
        )

        TermsAndPrivacyText(
            modifier = Modifier.padding(vertical = Dimension.Spacing.l),
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
                        URLEncoder.encode(
                            sharedUiState.privacyUrl,
                            StandardCharsets.UTF_8.toString()
                        )
                    onNavigateToPrivacy(encodedUrl)
                }
            }
        )

        GradientButton(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.CenterHorizontally)
                .padding(bottom = Dimension.Spacing.l),
            text = stringResource(id = CoreR.string.core_action_continue),
            gradient = primaryGradient(),
            isLoading = isLoading,
            enabled = canSubmit,
            onClick = { viewModel.onRequestSignUpClick(email = sharedUiState.email) },
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(id = R.string.already_have_account),
                fontSize = Dimension.TextSize.titleMedium,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
            )
            Spacer(modifier = Modifier.width(Dimension.Spacing.xs))
            GenCanvasTextButton(
                text = stringResource(id = CoreR.string.core_action_sign_in),
                textStyle = TextStyle(
                    color = MaterialTheme.colorScheme.onBackground,
                    fontSize = Dimension.TextSize.titleMedium,
                    fontWeight = FontWeight.SemiBold
                ),
                enabled = canClick,
                onClick = onNavigateToSignIn
            )
        }
    }
}