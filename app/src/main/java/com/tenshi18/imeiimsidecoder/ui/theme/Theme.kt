package com.tenshi18.imeiimsidecoder.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFF26236B),
    onPrimary = Color(0xFFFFFFFF),
    primaryContainer = Color(0xFF1C1D54),
    onPrimaryContainer = Color(0xFFFFFFFF),

    secondary = Color(0xFF150F38),
    onSecondary = Color(0xFFFFFFFF),
    secondaryContainer = Color(0xFF1C1D54),
    onSecondaryContainer = Color(0xFFFFFFFF),

    tertiary = Color(0xFF151345),
    onTertiary = Color(0xFFFFFFFF),
    tertiaryContainer = Color(0xFF1C1D54),
    onTertiaryContainer = Color(0xFFFFFFFF),

    background = Color(0xFF040622),
    onBackground = Color(0xFFFFFFFF),

    surface = Color(0xFF040622),
    onSurface = Color(0xFFFFFFFF),

    surfaceVariant = Color(0xFF151345),
    onSurfaceVariant = Color(0xFFFFFFFF),

    error = Color(0xFFCF6679),
    onError = Color(0xFFFFFFFF),
    errorContainer = Color(0xFF8C1D28),
    onErrorContainer = Color(0xFFFFFFFF),

    outline = Color(0xFF8A8A8A),

    inverseOnSurface = Color(0xFF151345),
    inverseSurface = Color(0xFFFFFFFF),
    inversePrimary = Color(0xFF26236B),

    surfaceTint = Color(0xFF26236B)
)

private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF003366),
    onPrimary = Color(0xFFFFFFFF),
    secondary = Color(0xFF336699),
    onSecondary = Color(0xFFFFFFFF),
    tertiary = Color(0xFF6688CC),
    onTertiary = Color(0xFFFFFFFF),
    error = Color(0xFFD00000),
    onError = Color(0xFFFFFFFF),
    background = Color(0xFF000022),
    onBackground = Color(0xFFFFFFFF),
    surface = Color(0xFF000033),
    onSurface = Color(0xFFFFFFFF)
)

@Composable
fun IMEIIMSIDecoderTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}