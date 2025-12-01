package com.lotusreichhart.gencanvas.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.lotusreichhart.gencanvas.core.common.event.UiEvent
import com.lotusreichhart.gencanvas.core.ui.theme.Info
import com.lotusreichhart.gencanvas.core.ui.theme.Error
import com.lotusreichhart.gencanvas.core.ui.theme.OnError
import com.lotusreichhart.gencanvas.core.ui.theme.OnInfo
import com.lotusreichhart.gencanvas.core.ui.theme.OnSuccess
import com.lotusreichhart.gencanvas.core.ui.theme.OnWarning
import com.lotusreichhart.gencanvas.core.ui.theme.Success
import com.lotusreichhart.gencanvas.core.ui.theme.Warning

@Composable
fun GenCanvasSnackBar(
    message: String,
    type: UiEvent.SnackBarType,
    modifier: Modifier = Modifier
){
    val backgroundColor = when (type) {
        UiEvent.SnackBarType.INFO -> Info
        UiEvent.SnackBarType.SUCCESS -> Success
        UiEvent.SnackBarType.ERROR -> Error
        UiEvent.SnackBarType.WARNING -> Warning
    }

    val textColor = when (type) {
        UiEvent.SnackBarType.INFO -> OnInfo
        UiEvent.SnackBarType.SUCCESS -> OnSuccess
        UiEvent.SnackBarType.ERROR -> OnError
        UiEvent.SnackBarType.WARNING -> OnWarning
    }

    val icon = when (type) {
        UiEvent.SnackBarType.INFO -> Icons.Default.Info
        UiEvent.SnackBarType.SUCCESS -> Icons.Default.CheckCircle
        UiEvent.SnackBarType.ERROR -> Icons.Default.Warning
        UiEvent.SnackBarType.WARNING -> Icons.Default.Warning
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(backgroundColor)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = textColor
        )
        Text(
            text = message,
            style = MaterialTheme.typography.bodyMedium,
            color = textColor,
            modifier = Modifier.weight(1f)
        )
    }
}