package com.yumedev.soraspace.ui.theme

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

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

object SoraType {
    val Label = TextStyle(
        fontSize = 10.sp,
        fontWeight = FontWeight.SemiBold,
        letterSpacing = 2.sp,
        color = SoraColors.Accent
    )
    val Title = TextStyle(
        fontSize = 26.sp,
        fontWeight = FontWeight.Bold,
        lineHeight = 32.sp,
        color = SoraColors.TextPrimary
    )
    val Body = TextStyle(
        fontSize = 14.sp,
        fontWeight = FontWeight.Normal,
        lineHeight = 23.sp,
        color = SoraColors.TextSecondary
    )
    val Caption = TextStyle(
        fontSize = 12.sp,
        fontWeight = FontWeight.Normal,
        letterSpacing = 0.3.sp,
        color = SoraColors.TextSecondary
    )
    val Tag = TextStyle(
        fontSize = 11.sp,
        fontWeight = FontWeight.Medium,
        letterSpacing = 0.5.sp,
        color = SoraColors.Accent
    )
}