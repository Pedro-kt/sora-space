package com.yumedev.soraspace

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import com.yumedev.soraspace.presentation.navigation.SoraNavGraph
import com.yumedev.soraspace.ui.theme.SoraColors

private val SoraColorScheme = darkColorScheme(
    background     = SoraColors.Background,
    onBackground   = SoraColors.TextPrimary,
    surface        = SoraColors.Surface,
    onSurface      = SoraColors.TextPrimary,
    surfaceVariant = SoraColors.SurfaceHigh,
    primary        = SoraColors.Accent,
    onPrimary      = SoraColors.TextPrimary,
    secondary      = SoraColors.Accent,
    onSecondary    = SoraColors.TextPrimary,
    outline        = SoraColors.TextTertiary
)

@Composable
fun App() {
    MaterialTheme(colorScheme = SoraColorScheme) {
        SoraNavGraph()
    }
}