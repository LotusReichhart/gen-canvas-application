package com.lotusreichhart.gencanvas.feature.account.presentation.profile.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.lotusreichhart.compose.sparkleborder.sparkleBorder
import com.lotusreichhart.gencanvas.core.model.user.User
import com.lotusreichhart.gencanvas.core.model.user.enums.UserTier
import com.lotusreichhart.gencanvas.core.ui.constant.Dimension
import com.lotusreichhart.gencanvas.core.ui.util.primaryGradient

import com.lotusreichhart.gencanvas.core.ui.R as CoreUiR
import com.lotusreichhart.gencanvas.feature.account.R
import com.lotusreichhart.gencanvas.core.common.R as CoreR

@Composable
internal fun CreditCard(
    user: User?,
    onAddCreditClick: () -> Unit
) {
    val isPro = user?.tier == UserTier.PRO
    val balance = user?.balance ?: 0

    val cardModifier = if (isPro) {
        Modifier
            .fillMaxWidth()
            .sparkleBorder(
                brush = primaryGradient(),
                backgroundColor = MaterialTheme.colorScheme.surfaceVariant,
                shape = RoundedCornerShape(Dimension.cornerRadius),
                borderWidth = 3.dp,
                animationDurationInMillis = 2000
            )
    } else {
        Modifier
            .fillMaxWidth()
            .background(
                MaterialTheme.colorScheme.surfaceVariant,
                RoundedCornerShape(Dimension.cornerRadius)
            )
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.outlineVariant,
                shape = RoundedCornerShape(Dimension.cornerRadius)
            )
    }

    Box(
        modifier = cardModifier
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(Dimension.mediumPadding),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(id = CoreUiR.drawable.ic_credit_coin),
                    contentDescription = "Coin",
                    tint = Color.Unspecified,
                    modifier = Modifier.size(40.dp)
                )

                Spacer(modifier = Modifier.width(Dimension.mediumPadding))

                Column(
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = stringResource(id = R.string.profile_label_current_balance),
                        style = MaterialTheme.typography.labelMedium.copy(
                            fontSize = Dimension.mediumFontSize
                        ),
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(Dimension.xsPadding))
                    Text(
                        text = "$balance ${stringResource(id = CoreR.string.core_unit_coins)}",
                        fontSize = Dimension.largeFontSize,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }

            Button(
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                ),
                shape = RoundedCornerShape(Dimension.cornerRadius),
                onClick = onAddCreditClick
            ) {
                Text(
                    text = stringResource(id = R.string.profile_action_get_credits),
                    fontSize = Dimension.smallFontSize,
                    fontWeight = FontWeight.SemiBold,
                )
            }
        }
    }
}