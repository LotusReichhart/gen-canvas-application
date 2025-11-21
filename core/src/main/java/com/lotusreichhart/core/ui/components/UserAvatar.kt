package com.lotusreichhart.core.ui.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.SubcomposeAsyncImage
import coil3.compose.SubcomposeAsyncImageContent
import coil3.request.ImageRequest
import coil3.request.crossfade
import coil3.compose.AsyncImagePainter
import com.lotusreichhart.core.ui.theme.primaryGradient
import com.lotusreichhart.core.utils.toInitials
import com.lotusreichhart.domain.entity.UserEntity
import com.lotusreichhart.domain.entity.UserTier

import com.lotusreichhart.core.R

@Composable
fun UserAvatar(
    modifier: Modifier = Modifier,
    userEntity: UserEntity? = null
) {
    Box(modifier = modifier) {
        val avatarTextContent: @Composable () -> Unit = {
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .clip(CircleShape)
                    .background(brush = primaryGradient()),
                contentAlignment = Alignment.Center
            ) {
                val textToShow = if (userEntity == null) {
                    "G"
                } else {
                    userEntity.name.toInitials() ?: "G"
                }

                Text(
                    text = textToShow,
                    color = MaterialTheme.colorScheme.onPrimary,
                    fontWeight = FontWeight.Bold,
                    fontSize = if (textToShow.length > 1) 14.sp else 16.sp
                )
            }
        }

        val avatarUrl = userEntity?.avatarUrl

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
                                1.dp,
                                MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f),
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

        if (userEntity?.tier == UserTier.PRO) {
            Icon(
                painter = painterResource(id = R.drawable.ic_vip_crown),
                contentDescription = "Pro User",
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .size(14.dp)
                    .offset(x = 2.dp, y = (-2).dp)
                    .padding(1.dp)
                    .shadow(2.dp, CircleShape)
            )
        }
    }
}