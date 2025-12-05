package com.lotusreichhart.gencanvas.feature.account.presentation

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.lotusreichhart.gencanvas.core.model.user.User
import com.lotusreichhart.gencanvas.core.ui.components.UserAvatar
import com.lotusreichhart.gencanvas.core.ui.constant.Dimension
import com.lotusreichhart.gencanvas.feature.account.presentation.components.AccountOptionItem

import com.lotusreichhart.gencanvas.core.common.R as CoreR
import com.lotusreichhart.gencanvas.feature.account.R

@Composable
internal fun AccountTab(
    viewModel: AccountTabViewModel = hiltViewModel(),
    onNavigateToAuth: () -> Unit,
    onNavigateToProfile: () -> Unit,
) {
    val scrollState = rememberScrollState()
    val user by viewModel.user.collectAsStateWithLifecycle()
    val isOnline by viewModel.isOnline.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        UserHeader(
            user = user,
            onClick = {
                if (viewModel.isSignIn) {
                    onNavigateToProfile()
                } else {
                    onNavigateToAuth()
                }
            }
        )

        AccountOptionItem(
            title = stringResource(id = R.string.menu_account_security),
            showBottomBorder = true,
            onClick = {
                if (viewModel.isSignIn) {
                    Unit
                } else {
                    onNavigateToAuth()
                }
            }
        )

        AccountOptionItem(
            title = stringResource(id = R.string.menu_personalization),
            description = stringResource(id = R.string.menu_personalization_desc),
            onClick = { }
        )

        Spacer(modifier = Modifier.height(5.dp))
        AccountOptionItem(
            title = stringResource(id = R.string.menu_help_feedback),
            onClick = { }
        )
        Spacer(modifier = Modifier.height(5.dp))

        AccountOptionItem(
            title = stringResource(id = R.string.menu_about_app),
            showBottomBorder = true,
            onClick = { }
        )

        AccountOptionItem(
            title = stringResource(id = CoreR.string.core_label_terms_of_service),
            showBottomBorder = true,
            onClick = { }
        )

        AccountOptionItem(
            title = stringResource(id = R.string.menu_network_diagnostics),
            onClick = { }
        )

        Spacer(modifier = Modifier.height(Dimension.Spacing.xxl))

        SignInSignOutButton(
            isSignIn = viewModel.isSignIn,
            enabled = isOnline || !viewModel.isSignIn,
            onClick = {
                if (viewModel.isSignIn) {
                    viewModel.onSignOutClick()
                } else {
                    onNavigateToAuth()
                }
            }
        )

        Spacer(modifier = Modifier.height(Dimension.Spacing.xxl))
    }
}


@Composable
private fun UserHeader(
    user: User? = null,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .clickable(onClick = onClick)
            .padding(vertical = Dimension.Spacing.xxxl)
            .padding(horizontal = Dimension.Spacing.m),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        UserAvatar(
            modifier = Modifier.size(60.dp),
            user = user,
            onClick = onClick
        )
        Spacer(modifier = Modifier.width(Dimension.Spacing.l))
        Text(
            text = user?.name ?: stringResource(CoreR.string.core_action_sign_in),
            style = MaterialTheme.typography.titleMedium,
            fontSize = Dimension.TextSize.headlineSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun SignInSignOutButton(
    isSignIn: Boolean = false,
    enabled: Boolean = true,
    onClick: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val title = if (isSignIn) {
        stringResource(CoreR.string.core_action_sign_out)
    } else {
        stringResource(CoreR.string.core_action_sign_in)
    }

    val baseColor =
        if (isSignIn) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.secondary

    val borderColor =
        if (enabled) baseColor else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f)

    val containerColor = if (isPressed) baseColor else Color.Transparent
    val contentColor = if (isPressed) Color.White else baseColor

    OutlinedButton(
        onClick = onClick,
        modifier = Modifier
            .width(200.dp)
            .height(50.dp),

        shape = RoundedCornerShape(Dimension.Radius.m),

        border = BorderStroke(1.dp, borderColor),

        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = containerColor,
            contentColor = contentColor
        ),
        enabled = enabled,
        interactionSource = interactionSource
    ) {
        Text(
            text = title,
            fontSize = Dimension.TextSize.titleMedium,
            fontWeight = FontWeight.SemiBold
        )
    }
}
