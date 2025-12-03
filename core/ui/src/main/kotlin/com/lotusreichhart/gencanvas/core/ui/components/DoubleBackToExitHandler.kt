package com.lotusreichhart.gencanvas.core.ui.components

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import com.lotusreichhart.gencanvas.core.common.util.findActivity

@Composable
fun DoubleBackToExitHandler(
    enabled: Boolean = true,
    delayMillis: Long = 2000,
    onShowMessage: () -> Unit
) {
    val context = LocalContext.current
    var lastBackPressTime by remember { mutableLongStateOf(0L) }

    BackHandler(enabled = enabled) {
        val currentTime = System.currentTimeMillis()
        if (currentTime - lastBackPressTime <= delayMillis) {
            context.findActivity()?.finish()
        } else {
            lastBackPressTime = currentTime
            onShowMessage()
        }
    }
}