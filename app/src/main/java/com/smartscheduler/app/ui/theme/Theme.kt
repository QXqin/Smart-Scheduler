package com.smartscheduler.app.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

// Custom color palette
val DeepNavy = Color(0xFF0D1B2A)
val DarkSlate = Color(0xFF1B2838)
val MidnightBlue = Color(0xFF243447)
val AccentCyan = Color(0xFF00D4AA)
val AccentCyanLight = Color(0xFF33E8C4)
val SubtlePurple = Color(0xFF7B68EE)
val WarmOrange = Color(0xFFFF8C42)
val SoftWhite = Color(0xFFF0F2F5)
val MutedGray = Color(0xFF8B95A5)
val CardDark = Color(0xFF1E2D3D)
val SurfaceDark = Color(0xFF152232)
val ErrorRed = Color(0xFFFF6B6B)

// Event type colors
val FixedEventColor = Color(0xFF5B8DEF)       // Blue - fixed events
val ScheduledEventColor = Color(0xFF00D4AA)    // Cyan/green - AI scheduled

private val DarkColorScheme = darkColorScheme(
    primary = AccentCyan,
    onPrimary = DeepNavy,
    primaryContainer = MidnightBlue,
    onPrimaryContainer = AccentCyanLight,
    secondary = SubtlePurple,
    onSecondary = SoftWhite,
    secondaryContainer = DarkSlate,
    onSecondaryContainer = SoftWhite,
    tertiary = WarmOrange,
    onTertiary = DeepNavy,
    background = DeepNavy,
    onBackground = SoftWhite,
    surface = SurfaceDark,
    onSurface = SoftWhite,
    surfaceVariant = CardDark,
    onSurfaceVariant = MutedGray,
    error = ErrorRed,
    onError = SoftWhite,
    outline = MutedGray
)

private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF00897B),
    onPrimary = Color.White,
    primaryContainer = Color(0xFFB2DFDB),
    onPrimaryContainer = Color(0xFF004D40),
    secondary = Color(0xFF5C6BC0),
    onSecondary = Color.White,
    tertiary = WarmOrange,
    background = Color(0xFFF8F9FA),
    onBackground = Color(0xFF1A1A2E),
    surface = Color.White,
    onSurface = Color(0xFF1A1A2E),
    surfaceVariant = Color(0xFFEEF0F4),
    onSurfaceVariant = Color(0xFF5A6170),
    error = ErrorRed,
    outline = Color(0xFFBFC5D0)
)

@Composable
fun SmartSchedulerTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.background.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography(),
        content = content
    )
}
