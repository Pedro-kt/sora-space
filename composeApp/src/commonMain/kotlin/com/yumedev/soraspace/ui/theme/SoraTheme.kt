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
    // Base colors
    val Background    = Color(0xFF000000)
    val Surface       = Color(0xFF0F0F0F)
    val SurfaceHigh   = Color(0xFF1A1A1A)
    val Accent        = Color(0xFF7C9EFF)
    val AccentSubtle  = Color(0x1A7C9EFF)
    val TextPrimary   = Color(0xFFFFFFFF)
    val TextSecondary = Color(0xFF888888)
    val TextTertiary  = Color(0xFF3A3A3A)

    // Semantic colors
    object Status {
        val Success = Color(0xFF4CAF50)
        val Warning = Color(0xFFFFB300)
        val Error   = Color(0xFFF44336)
        val Info    = Color(0xFF2196F3)
    }

    // Space Weather Activity Levels
    object Activity {
        val Quiet    = Status.Success      // Green
        val Minor    = Color(0xFFCDDC39)   // Yellow-green
        val Moderate = Color(0xFFFF9800)   // Orange
        val Severe   = Status.Error        // Red
    }

    // EONET Event Categories
    object Categories {
        val Wildfires     = Color(0xFFFF6B35)
        val Volcanoes     = Color(0xFFFF7043)
        val SevereStorms  = Color(0xFF64B5F6)
        val Drought       = Color(0xFFD4A373)
        val Earthquakes   = Color(0xFF8B4513)
        val Floods        = Color(0xFF4FC3F7)
        val Landslides    = Color(0xFF6D4C41)
        val ManMade       = Color(0xFF90A4AE)
        val SeaLakeIce    = Color(0xFFB3E5FC)
        val Snow          = Color(0xFFE1F5FE)
        val TemperatureEx = Color(0xFFFF8A65)
        val WaterColor    = Color(0xFF26C6DA)
        val Dust          = Color(0xFFBCAAA4)
    }

    // Launch Status Colors
    object LaunchStatus {
        val Go      = Status.Success
        val TBD     = Status.Warning
        val Hold    = Color(0xFFFF6D00)  // Dark orange
        val Success = Accent
        val Failed  = Status.Error
    }
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