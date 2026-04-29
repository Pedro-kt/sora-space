package com.yumedev.soraspace.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.resources.Font
import soraspace.composeapp.generated.resources.Res
import soraspace.composeapp.generated.resources.orbitron_semibold
import soraspace.composeapp.generated.resources.space_grotesk_bold
import soraspace.composeapp.generated.resources.space_grotesk_medium
import soraspace.composeapp.generated.resources.space_grotesk_regular
import soraspace.composeapp.generated.resources.space_grotesk_semibold

object SoraColors {
    val Background    = Color(0xFF000000)
    val Surface       = Color(0xFF0F0F0F)
    val SurfaceHigh   = Color(0xFF1A1A1A)
    val Accent        = Color(0xFF7C9EFF)
    val AccentSubtle  = Color(0x1A7C9EFF)
    val TextPrimary   = Color(0xFFFFFFFF)
    val TextSecondary = Color(0xFF888888)
    val TextTertiary  = Color(0xFF3A3A3A)
}

object SoraFonts {
    val SpaceGrotesk: FontFamily
        @Composable get() = FontFamily(
            Font(Res.font.space_grotesk_regular, FontWeight.Normal),
            Font(Res.font.space_grotesk_medium, FontWeight.Medium),
            Font(Res.font.space_grotesk_semibold, FontWeight.SemiBold),
            Font(Res.font.space_grotesk_bold, FontWeight.Bold),
        )

    val Orbitron: FontFamily
        @Composable get() = FontFamily(
            Font(Res.font.orbitron_semibold, FontWeight.SemiBold),
        )
}

object SoraType {
    val Label: TextStyle
        @Composable get() = TextStyle(
            fontFamily = SoraFonts.Orbitron,
            fontSize = 10.sp,
            fontWeight = FontWeight.SemiBold,
            letterSpacing = 2.sp,
            color = SoraColors.Accent
        )
    val Title: TextStyle
        @Composable get() = TextStyle(
            fontFamily = SoraFonts.SpaceGrotesk,
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold,
            lineHeight = 32.sp,
            color = SoraColors.TextPrimary
        )
    val Body: TextStyle
        @Composable get() = TextStyle(
            fontFamily = SoraFonts.SpaceGrotesk,
            fontSize = 14.sp,
            fontWeight = FontWeight.Normal,
            lineHeight = 23.sp,
            color = SoraColors.TextSecondary
        )
    val Caption: TextStyle
        @Composable get() = TextStyle(
            fontFamily = SoraFonts.SpaceGrotesk,
            fontSize = 12.sp,
            fontWeight = FontWeight.Normal,
            letterSpacing = 0.3.sp,
            color = SoraColors.TextSecondary
        )
    val Tag: TextStyle
        @Composable get() = TextStyle(
            fontFamily = SoraFonts.SpaceGrotesk,
            fontSize = 11.sp,
            fontWeight = FontWeight.Medium,
            letterSpacing = 0.5.sp,
            color = SoraColors.Accent
        )
}