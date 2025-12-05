package com.lotusreichhart.gencanvas.core.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import com.lotusreichhart.gencanvas.core.ui.constant.Dimension

@Composable
fun GradientButton(
    text: String,
    gradient: Brush,
    modifier: Modifier = Modifier,
    isLoading: Boolean = false,
    enabled: Boolean = true,
    onClick: () -> Unit
) {

    val keyboardController = LocalSoftwareKeyboardController.current

    Button(
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Transparent,
            disabledContainerColor = Color.Gray.copy(alpha = 0.5f),
            disabledContentColor = Color.White.copy(alpha = 0.7f)
        ),
        contentPadding = PaddingValues(),
        modifier = modifier,
        enabled = enabled,
        onClick = {
            keyboardController?.hide()
            onClick()
        }
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .then(
                    if (enabled) Modifier.background(gradient)
                    else Modifier.background(Color.Transparent)
                )
                .padding(horizontal = Dimension.Spacing.xxl, vertical = Dimension.Spacing.l),
            contentAlignment = Alignment.Center
        ) {
            if (isLoading) {
                BouncingDotsIndicator()
            } else {
                Text(text = text, fontSize = Dimension.TextSize.titleMedium)
            }
        }
    }
}