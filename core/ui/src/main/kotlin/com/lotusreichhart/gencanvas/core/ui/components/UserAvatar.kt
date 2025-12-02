package com.lotusreichhart.gencanvas.core.ui.components

import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.indication
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.SubcomposeAsyncImage
import coil3.compose.SubcomposeAsyncImageContent
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.lotusreichhart.compose.sparkleborder.sparkleBorder
import com.lotusreichhart.gencanvas.core.common.util.toInitials
import com.lotusreichhart.gencanvas.core.model.user.User
import com.lotusreichhart.gencanvas.core.model.user.enums.UserTier
import com.lotusreichhart.gencanvas.core.ui.R
import com.lotusreichhart.gencanvas.core.ui.util.primaryGradient

@Suppress("COMPOSE_APPLIER_CALL_MISMATCH")
@Composable
fun UserAvatar(
    modifier: Modifier = Modifier,
    user: User? = null,
    showAccessory: Boolean = true,
    onClick: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPro = user?.tier == UserTier.PRO

    Box(
        modifier = modifier
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick
            )
    ) {
        Box(
            modifier = Modifier
                .matchParentSize()
                .then(
                    if (isPro) {
                        Modifier
                            .sparkleBorder(
                                brush = primaryGradient(),
                                backgroundColor = Color.Transparent,
                                shape = CircleShape,
                                borderWidth = 2.dp,
                                animationDurationInMillis = 5000
                            )
                            .padding(2.dp)
                    } else {
                        Modifier
                    }
                )
                .clip(CircleShape)
                .indication(interactionSource, LocalIndication.current)
        ) {
            val avatarTextContent: @Composable () -> Unit = {
                BoxWithConstraints(
                    modifier = Modifier
                        .matchParentSize()
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.8f)),
                    contentAlignment = Alignment.Center
                ) {
                    val textToShow = if (user == null) {
                        "G"
                    } else {
                        user.name.toInitials() ?: "G"
                    }

                    val density = LocalDensity.current
                    val dynamicFontSize = with(density) {
                        (maxHeight.toPx() * 0.3f).toSp()
                    }

                    Text(
                        text = textToShow,
                        color = MaterialTheme.colorScheme.onBackground,
                        fontWeight = FontWeight.Bold,
                        fontSize = dynamicFontSize
                    )
                }
            }

            val avatarUrl = user?.avatarUrl

            if (!avatarUrl.isNullOrBlank()) {
                SubcomposeAsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(avatarUrl)
                        .crossfade(true)
                        .build(),
                    contentDescription = "Avatar",
                    modifier = Modifier.matchParentSize()
                ) {
                    val state = painter.state

                    if (state is Error) {
                        avatarTextContent()
                    } else {
                        Box(
                            modifier = Modifier
                                .matchParentSize()
                                .clip(CircleShape)
                                .shadow(4.dp, CircleShape)
                                .border(
                                    width = 0.1.dp,
                                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.2f),
                                    CircleShape
                                )
                        ) {
                            this@SubcomposeAsyncImage.SubcomposeAsyncImageContent(
                                modifier = Modifier.matchParentSize(),
                                contentScale = ContentScale.Crop
                            )
                        }
                    }
                }
            } else {
                avatarTextContent()
            }
        }


        if (showAccessory && user != null) {
            CoinBadge(
                balance = user.balance,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .offset(x = 1.dp, y = 1.dp)
            )
        }
    }
}

@Composable
private fun CoinBadge(
    balance: Int,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_credit_coin),
            contentDescription = "Coins",
            tint = Color.Unspecified,
            modifier = Modifier
                .size(22.dp)
                .shadow(4.dp, CircleShape)
        )

        Text(
            text = balance.toString(),
            style = MaterialTheme.typography.labelSmall.copy(
                fontSize = 9.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color.White.copy(alpha = 0.8f)
            )
        )
    }
}