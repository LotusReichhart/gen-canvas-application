package com.lotusreichhart.gencanvas.core.ui.util

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Brush

@Composable
fun primaryGradient(): Brush {
    val secondary = MaterialTheme.colorScheme.secondary
    val primaryContainer = MaterialTheme.colorScheme.primaryContainer
    val primary = MaterialTheme.colorScheme.primary

    return remember(secondary, primaryContainer, primary) {
        Brush.linearGradient(
            colors = listOf(
                secondary,
                primaryContainer,
                primary
            )
        )
    }
}

@Composable
fun tertiaryGradient(): Brush {
    val tertiary = MaterialTheme.colorScheme.tertiary
    val errorContainer = MaterialTheme.colorScheme.errorContainer
    val error = MaterialTheme.colorScheme.error

    return remember(tertiary, errorContainer, error) {
        Brush.linearGradient(
            colors = listOf(
                tertiary,
                errorContainer,
                error
            )
        )
    }
}