package com.lotusreichhart.gencanvas.feature.account.presentation.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.lotusreichhart.gencanvas.core.model.user.User
import com.lotusreichhart.gencanvas.core.ui.components.GenCanvasIconButton
import com.lotusreichhart.gencanvas.core.ui.components.GenCanvasTextButton
import com.lotusreichhart.gencanvas.core.ui.components.UserAvatar
import com.lotusreichhart.gencanvas.core.ui.constant.Dimension
import com.lotusreichhart.gencanvas.feature.account.presentation.profile.components.CreditCard
import com.lotusreichhart.gencanvas.feature.account.presentation.profile.components.UserInformationCard
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

import com.lotusreichhart.gencanvas.feature.account.R
import com.lotusreichhart.gencanvas.core.common.R as CoreR

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun ProfileScreen(
    viewModel: ProfileViewModel = hiltViewModel(),
    onDetailAvatar: (String?) -> Unit,
    onNavigateToEditProfile: () -> Unit,
    onDismiss: () -> Unit,
) {
    val scrollState = rememberScrollState()

    val user by viewModel.user.collectAsStateWithLifecycle()

    Scaffold(
        modifier = Modifier.background(MaterialTheme.colorScheme.background),
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = stringResource(id = R.string.profile_title),
                        fontSize = Dimension.TextSize.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                },
                navigationIcon = {
                    GenCanvasIconButton(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Profile Back Icon",
                        iconSize = Dimension.Icon.m,
                        onClick = onDismiss,
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    MaterialTheme.colorScheme.surfaceVariant
                ),
                actions = {
                    GenCanvasTextButton(
                        text = stringResource(id = CoreR.string.core_action_edit),
                        textColor = MaterialTheme.colorScheme.onSurface,
                        textStyle = MaterialTheme.typography.labelMedium.copy(
                            fontSize = Dimension.TextSize.titleSmall
                        ),
                        onClick = onNavigateToEditProfile
                    )
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = Dimension.Spacing.m)
                .verticalScroll(scrollState)
        ) {
            ProfileHeader(
                user = user,
                onClick = {
                    val encodedUrl =
                        URLEncoder.encode(user?.avatarUrl, StandardCharsets.UTF_8.toString())
                    onDetailAvatar(encodedUrl)
                }
            )

            UserInformationCard(
                user = user
            )

            CreditCard(
                user = user,
                onAddCreditClick = {}
            )
        }
    }
}

@Composable
private fun ProfileHeader(
    user: User? = null,
    onClick: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = Dimension.Spacing.l),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        UserAvatar(
            modifier = Modifier.size(100.dp),
            user = user,
            showAccessory = false,
            onClick = onClick
        )
        Spacer(modifier = Modifier.height(Dimension.Spacing.l))
        Text(
            text = user?.name ?: "",
            style = MaterialTheme.typography.titleLarge.copy(
                color = MaterialTheme.colorScheme.onBackground,
                fontWeight = FontWeight.Bold
            ),
        )
        Text(
            text = user?.email ?: "",
            fontSize = Dimension.TextSize.bodyMedium,
            color = MaterialTheme.colorScheme.onBackground
        )
    }
}