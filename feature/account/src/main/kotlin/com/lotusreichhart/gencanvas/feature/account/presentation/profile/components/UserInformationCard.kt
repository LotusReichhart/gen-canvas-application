package com.lotusreichhart.gencanvas.feature.account.presentation.profile.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.lotusreichhart.compose.sparkleborder.sparkleBorder
import com.lotusreichhart.gencanvas.core.model.user.User
import com.lotusreichhart.gencanvas.core.model.user.enums.AuthProvider
import com.lotusreichhart.gencanvas.core.model.user.enums.UserStatus
import com.lotusreichhart.gencanvas.core.model.user.enums.UserTier

import com.lotusreichhart.gencanvas.core.ui.constant.Dimension
import com.lotusreichhart.gencanvas.core.ui.theme.Error
import com.lotusreichhart.gencanvas.core.ui.theme.Info
import com.lotusreichhart.gencanvas.core.ui.theme.OnError
import com.lotusreichhart.gencanvas.core.ui.theme.OnInfo
import com.lotusreichhart.gencanvas.core.ui.theme.OnSuccess
import com.lotusreichhart.gencanvas.core.ui.theme.OnWarning
import com.lotusreichhart.gencanvas.core.ui.theme.Success
import com.lotusreichhart.gencanvas.core.ui.theme.Warning
import com.lotusreichhart.gencanvas.core.ui.util.primaryGradient

import com.lotusreichhart.gencanvas.core.ui.R as CoreUiR
import com.lotusreichhart.gencanvas.core.common.R as CoreR
import com.lotusreichhart.gencanvas.feature.account.R

@Composable
internal fun UserInformationCard(
    user: User? = null
) {

    val isPro = user?.tier == UserTier.PRO

    val containerModifier = if (isPro) {
        Modifier
            .fillMaxWidth()
            .padding(vertical = Dimension.Radius.m)
            .sparkleBorder(
                brush = primaryGradient(),
                backgroundColor = MaterialTheme.colorScheme.surfaceVariant,
                shape = RoundedCornerShape(Dimension.Radius.m),
                borderWidth = 3.dp,
                animationDurationInMillis = 2000
            )
    } else {
        Modifier
            .fillMaxWidth()
            .padding(vertical = Dimension.Spacing.l)
            .background(
                MaterialTheme.colorScheme.surfaceVariant,
                RoundedCornerShape(Dimension.Radius.m)
            )
            .border(
                1.dp,
                MaterialTheme.colorScheme.outlineVariant,
                RoundedCornerShape(Dimension.Radius.m)
            )
    }


    Box(modifier = containerModifier) {
        Column(
            modifier = Modifier.padding(Dimension.Spacing.l),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.spacedBy(Dimension.Spacing.l)
        ) {
            Text(
                text = stringResource(id = R.string.profile_info_title),
                fontSize = Dimension.TextSize.bodyLarge,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onBackground
            )
            InformationItem(
                title = stringResource(id = R.string.profile_label_account_type)
            ) {
                val (text, bgColor, textColor) = when (user?.tier) {
                    UserTier.FREE -> Triple(
                        stringResource(id = CoreR.string.core_tier_free),
                        MaterialTheme.colorScheme.secondaryContainer,
                        MaterialTheme.colorScheme.onSecondaryContainer
                    )

                    UserTier.PRO -> Triple(
                        stringResource(id = CoreR.string.core_tier_pro).uppercase(),
                        MaterialTheme.colorScheme.primaryContainer,
                        MaterialTheme.colorScheme.onPrimaryContainer
                    )

                    else -> {
                        Triple(
                            stringResource(id = CoreR.string.core_loading),
                            MaterialTheme.colorScheme.secondaryContainer,
                            MaterialTheme.colorScheme.onSecondaryContainer
                        )
                    }
                }

                InformationContentChip(
                    text = text,
                    bgColor = bgColor,
                    textColor = textColor
                )
            }

            InformationItem(
                title = stringResource(id = R.string.profile_label_login_method)
            ) {

                if (user?.authProvider == AuthProvider.GOOGLE) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(Dimension.Spacing.xs)
                    ) {
                        Icon(
                            painter = painterResource(id = CoreUiR.drawable.ic_google_icon),
                            contentDescription = "Google Logo",
                            tint = Color.Unspecified,
                            modifier = Modifier.size(Dimension.Icon.l)
                        )

                        InformationContentChip(
                            text = stringResource(id = CoreR.string.core_google).uppercase(),
                            bgColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f),
                            textColor = MaterialTheme.colorScheme.onSurface
                        )
                    }
                } else {
                    InformationContentChip(
                        text = stringResource(id = CoreR.string.core_email).uppercase(),
                        bgColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f),
                        textColor = MaterialTheme.colorScheme.onSurface
                    )
                }
            }

            InformationItem(
                title = stringResource(id = CoreR.string.core_status_label)
            ) {

                val (text, bgColor, textColor) = when (user?.status) {
                    UserStatus.ACTIVE -> Triple(
                        stringResource(id = CoreR.string.core_status_active),
                        Success,
                        OnSuccess
                    )

                    UserStatus.INACTIVE -> Triple(
                        stringResource(id = CoreR.string.core_status_inactive),
                        Warning,
                        OnWarning
                    )

                    UserStatus.BANNED -> Triple(
                        stringResource(id = CoreR.string.core_status_banned),
                        Error,
                        OnError
                    )

                    else -> {
                        Triple(
                            stringResource(id = CoreR.string.core_loading),
                            Info,
                            OnInfo
                        )
                    }
                }

                InformationContentChip(
                    text = text,
                    bgColor = bgColor,
                    textColor = textColor
                )
            }
        }
    }
}

@Composable
private fun InformationItem(
    title: String,
    content: @Composable () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleSmall.copy(
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
                fontSize = Dimension.TextSize.bodyMedium
            )
        )

        content()
    }
}

@Composable
private fun InformationContentChip(
    text: String,
    bgColor: Color,
    textColor: Color
) {
    Text(
        modifier = Modifier
            .clip(RoundedCornerShape(Dimension.Radius.m))
            .background(color = bgColor)
            .padding(vertical = Dimension.Spacing.xs)
            .padding(horizontal = Dimension.Spacing.l),
        text = text,
        fontSize = Dimension.TextSize.bodySmall,
        color = textColor,
    )
}