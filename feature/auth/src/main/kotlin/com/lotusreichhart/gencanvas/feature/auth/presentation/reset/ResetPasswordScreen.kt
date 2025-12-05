package com.lotusreichhart.gencanvas.feature.auth.presentation.reset

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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.lotusreichhart.gencanvas.core.ui.components.GenCanvasTextButton
import com.lotusreichhart.gencanvas.core.ui.components.GradientButton
import com.lotusreichhart.gencanvas.core.ui.constant.Dimension
import com.lotusreichhart.gencanvas.core.ui.util.tertiaryGradient

import com.lotusreichhart.gencanvas.feature.auth.presentation.AuthSharedViewModel
import com.lotusreichhart.gencanvas.feature.auth.presentation.components.AuthBackground
import com.lotusreichhart.gencanvas.feature.auth.presentation.components.AuthTextField

import com.lotusreichhart.gencanvas.feature.auth.R
import com.lotusreichhart.gencanvas.core.common.R as CoreR
import com.lotusreichhart.gencanvas.core.ui.R as CoreUiR

@Composable
internal fun ResetPasswordScreen(
    sharedViewModel: AuthSharedViewModel,
    viewModel: ResetPasswordViewModel = hiltViewModel(),
    onNavigateToSignIn: () -> Unit,
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
            sharedViewModel.onResetTokenChange("")
            onNavigateToSignIn()
        }
    }

    AuthBackground(
        backgroundImageRes = CoreUiR.drawable.future_city_lock
    ) {
        Text(
            text = stringResource(id = R.string.set_new_password_title),
            color = MaterialTheme.colorScheme.onBackground,
            fontSize = Dimension.TextSize.headlineLarge,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = Dimension.Spacing.l)
        )

        Text(
            text = stringResource(id = R.string.set_new_password_subtitle),
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
            fontSize = Dimension.TextSize.bodyLarge,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.padding(bottom = Dimension.Spacing.xxl)
        )

        AuthTextField(
            modifier = Modifier.padding(bottom = Dimension.Spacing.l),
            value = uiState.password,
            onValueChange = { viewModel.onPasswordChange(it) },
            label = stringResource(id = CoreR.string.core_password),
            icon = Icons.Default.Lock,
            isPassword = true,
            isError = uiState.passwordErrorMessage != null,
            errorMessage = uiState.passwordErrorMessage
        )

        AuthTextField(
            modifier = Modifier.padding(bottom = Dimension.Spacing.l),
            value = uiState.confirmPassword,
            onValueChange = { viewModel.onConfirmPasswordChange(it) },
            label = stringResource(id = CoreR.string.core_confirm_password),
            icon = Icons.Default.Lock,
            isPassword = true,
            isError = uiState.confirmPasswordErrorMessage != null,
            errorMessage = uiState.confirmPasswordErrorMessage
        )

        GradientButton(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.CenterHorizontally)
                .padding(bottom = Dimension.Spacing.l),
            text = stringResource(id = R.string.action_save_password),
            gradient = tertiaryGradient(),
            isLoading = isLoading,
            enabled = canSubmit,
            onClick = {
                viewModel.onResetPasswordClick(sharedUiState.resetToken)
            },
        )

        GenCanvasTextButton(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            text = stringResource(id = CoreR.string.core_action_close),
            textStyle = TextStyle(
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = Dimension.TextSize.titleMedium,
                fontWeight = FontWeight.SemiBold
            ),
            enabled = canClick,
            onClick = {
                sharedViewModel.onResetTokenChange("")
                onDismiss()
            }
        )
    }
}