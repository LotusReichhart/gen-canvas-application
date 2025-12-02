package com.lotusreichhart.gencanvas.core.ui.components


import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LocalRippleConfiguration
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.lotusreichhart.gencanvas.core.ui.constant.Dimension

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GenCanvasTextButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    textColor: Color = MaterialTheme.colorScheme.secondary,
    textStyle: TextStyle = MaterialTheme.typography.labelLarge.copy(
        fontSize = Dimension.mediumButtonFontSize,
        fontWeight = FontWeight.Medium
    ),
    enabled: Boolean = true,
) {

    val keyboardController = LocalSoftwareKeyboardController.current

    CompositionLocalProvider(
        LocalRippleConfiguration provides null
    ) {
        TextButton(
            onClick = {
                keyboardController?.hide()
                onClick()
            },
            modifier = modifier,
            enabled = enabled,
            colors = ButtonDefaults.textButtonColors(
                contentColor = textColor,
                disabledContentColor = textColor.copy(alpha = 0.5f)
            ),
            contentPadding = PaddingValues(0.dp),
        ) {
            Text(
                text = text,
                style = textStyle
            )
        }
    }
}