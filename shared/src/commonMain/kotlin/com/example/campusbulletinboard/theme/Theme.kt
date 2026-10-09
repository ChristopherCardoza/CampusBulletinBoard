package com.example.campusbulletinboard.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val CampusBulletinBoardColorScheme = darkColorScheme(
    primary = Primary,
    onPrimary = Background,
    primaryContainer = Secondary,
    onPrimaryContainer = Color.White,
    secondary = Secondary,
    onSecondary = Color.White,
    secondaryContainer = Secondary,
    onSecondaryContainer = Color.White,
    tertiary = Accent,
    onTertiary = Background,
    tertiaryContainer = Secondary,
    onTertiaryContainer = Color.White,
    background = Background,
    onBackground = Color.White,
    surface = Surface,
    onSurface = Color.White,
    surfaceVariant = Surface,
    onSurfaceVariant = Color.White.copy(alpha = 0.78f),
    outline = Secondary,
    outlineVariant = Secondary,
    surfaceTint = Color.Transparent,
    surfaceBright = Surface,
    surfaceContainer = Surface,
    surfaceContainerHigh = Surface,
    surfaceContainerHighest = Surface,
    surfaceContainerLow = Surface,
    surfaceContainerLowest = Surface,
)

@Composable
fun CampusBulletinBoardTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = CampusBulletinBoardColorScheme,
        content = content,
    )
}