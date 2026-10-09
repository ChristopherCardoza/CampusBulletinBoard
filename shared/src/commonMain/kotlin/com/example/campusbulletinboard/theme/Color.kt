package com.example.campusbulletinboard.theme

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

val Background = Color(0xFF061A23)
val Surface = Color(0xFF06373A)
val Secondary = Color(0xFF1F5F5B)
val Primary = Color(0xFF159947)
val Accent = Color(0xFF49B265)

val SurfaceToBackground = Brush.verticalGradient(
    colors = listOf(Surface, Background),
)
val PrimaryToSecondary = Brush.verticalGradient(
    colors = listOf(Primary, Secondary),
)
val AccentToPrimary = Brush.verticalGradient(
    colors = listOf(Accent, Primary),
)