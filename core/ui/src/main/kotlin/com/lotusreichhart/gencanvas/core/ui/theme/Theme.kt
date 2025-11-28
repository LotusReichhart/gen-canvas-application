package com.lotusreichhart.gencanvas.core.ui.theme

import android.os.Build
import android.util.Log
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

enum class AppTheme {
    PURPLE_GRADIENT,
    BRIGHT_SKY,
    VIBRANT_CONTRAST,
    MINIMAL_DARK
}

private val PurpleGradientTheme = darkColorScheme(
    primary = Color(0xFF6A0DAD),
    onPrimary = Color.White,
    primaryContainer = Color(0xFFC080FF),
    onPrimaryContainer = Color(0xFF2E004F),

    secondary = Color(0xFF00C8FF),
    onSecondary = Color.Black,
    secondaryContainer = Color(0xFF80E7FF),
    onSecondaryContainer = Color(0xFF00303F),

    background = Color(0xFF1A1A2E),
    onBackground = Color.White,

    surface = Color(0xFF2C2C4A),
    onSurface = Color.White,
    surfaceVariant = Color(0xFF121220),
    onSurfaceVariant = Color(0xFFCAC4D0),
    surfaceContainer = Color(0xFF2C2C4A),

    error = Color(0xFFCF6679),
    onError = Color.Black,
    errorContainer = Color(0xFFF2B8B5),
    onErrorContainer = Color(0xFF410E0B),

    tertiary = Color(0xFFFFD700),
    onTertiary = Color.Black,
    tertiaryContainer = Color(0xFFFFFAD4),
    onTertiaryContainer = Color(0xFF2B2500),
)

private val BrightSkyTheme = lightColorScheme(
    primary = Color(0xFF007BFF),
    onPrimary = Color.White,
    primaryContainer = Color(0xFFD0E4FF),
    onPrimaryContainer = Color(0xFF001D36),

    secondary = Color(0xFF8A2BE2),
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFE2CFFF),
    onSecondaryContainer = Color(0xFF3B0068),

    background = Color(0xFFF0F8FF),
    onBackground = Color(0xFF1A1C1E),

    surface = Color(0xFFFFFFFF),
    onSurface = Color(0xFF1A1C1E),
    surfaceVariant = Color(0xFFE7E0EC),
    onSurfaceVariant = Color(0xFF49454F),

    error = Color(0xFFB00020),
    onError = Color.White,
    errorContainer = Color(0xFFF2B8B5),
    onErrorContainer = Color(0xFF410E0B),

    tertiary = Color(0xFFFF8C00),
    onTertiary = Color.White,
    tertiaryContainer = Color(0xFFFFDBCF),
    onTertiaryContainer = Color(0xFF2C1600),
)

private val VibrantContrastTheme = darkColorScheme(
    primary = Color(0xFF7B24FF),
    onPrimary = Color.White,
    primaryContainer = Color(0xFFBB86FC),
    onPrimaryContainer = Color(0xFF000000),

    secondary = Color(0xFF00FFFF),
    onSecondary = Color.Black,
    secondaryContainer = Color(0xFF66FFFF),
    onSecondaryContainer = Color(0xFF000000),

    background = Color(0xFF000000),
    onBackground = Color.White,

    surface = Color(0xFF121212),
    onSurface = Color.White,
    surfaceVariant = Color(0xFF333333),
    onSurfaceVariant = Color(0xFFCCCCCC),

    error = Color(0xFFEF5350),
    onError = Color.White,
    errorContainer = Color(0xFFFFCDD2),
    onErrorContainer = Color(0xFFC62828),

    tertiary = Color(0xFFFF007F),
    onTertiary = Color.White,
    tertiaryContainer = Color(0xFFFFC0CB),
    onTertiaryContainer = Color(0xFF8B0048),
)

private val MinimalDarkTheme = darkColorScheme(
    primary = Color(0xFFBB86FC),
    onPrimary = Color.Black,
    primaryContainer = Color(0xFF3700B3),
    onPrimaryContainer = Color.White,

    secondary = Color(0xFF03DAC6),
    onSecondary = Color.Black,
    secondaryContainer = Color(0xFF018786),
    onSecondaryContainer = Color.White,

    background = Color(0xFF121212),
    onBackground = Color.White,

    surface = Color(0xFF1E1E1E),
    onSurface = Color.White,
    surfaceVariant = Color(0xFF424242),
    onSurfaceVariant = Color(0xFFE0E0E0),

    error = Color(0xFFCF6679),
    onError = Color.Black,
    errorContainer = Color(0xFFF2B8B5),
    onErrorContainer = Color(0xFF410E0B),

    tertiary = Color(0xFFFF9800),
    onTertiary = Color.Black,
    tertiaryContainer = Color(0xFFFFCC80),
    onTertiaryContainer = Color(0xFF421C00),
)

private const val THEME_TAG = "GenCanvasTheme"

@Composable
fun GenCanvasTheme(
    appTheme: AppTheme = AppTheme.PURPLE_GRADIENT,
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val context = LocalContext.current
    val isSystemDark = isSystemInDarkTheme()

    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            Log.d(THEME_TAG, "Sử dụng Dynamic Color. System Dark = $isSystemDark")
            if (isSystemDark) dynamicDarkColorScheme(context)
            else dynamicLightColorScheme(context)
        }

        appTheme == AppTheme.PURPLE_GRADIENT -> {
            Log.d(THEME_TAG, "Sử dụng AppTheme: PURPLE_GRADIENT")
            PurpleGradientTheme
        }

        appTheme == AppTheme.BRIGHT_SKY -> {
            Log.d(THEME_TAG, "Sử dụng AppTheme: BRIGHT_SKY")
            BrightSkyTheme
        }

        appTheme == AppTheme.VIBRANT_CONTRAST -> {
            Log.d(THEME_TAG, "Sử dụng AppTheme: VIBRANT_CONTRAST")
            VibrantContrastTheme
        }

        appTheme == AppTheme.MINIMAL_DARK -> {
            Log.d(THEME_TAG, "Sử dụng AppTheme: MINIMAL_DARK")
            MinimalDarkTheme
        }

        else -> {
            Log.d(THEME_TAG, "Sử dụng AppTheme: DEFAULT (PURPLE_GRADIENT)")
            PurpleGradientTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}