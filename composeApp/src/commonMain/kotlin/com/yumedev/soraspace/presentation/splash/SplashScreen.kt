package com.yumedev.soraspace.presentation.splash

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yumedev.soraspace.ui.strings.LocalStrings
import com.yumedev.soraspace.ui.theme.SoraColors
import com.yumedev.soraspace.ui.theme.SoraFonts
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource
import soraspace.composeapp.generated.resources.Res
import soraspace.composeapp.generated.resources.planeta_splash

@Composable
fun SplashScreen(onSplashFinished: () -> Unit) {
    val planetAlpha = remember { Animatable(0f) }
    val planetOffset = remember { Animatable(100f) }
    val textAlpha = remember { Animatable(0f) }
    val textOffset = remember { Animatable(36f) }
    val orbitron = SoraFonts.Orbitron
    val spaceGrotesk = SoraFonts.SpaceGrotesk
    val strings = LocalStrings.current

    LaunchedEffect(Unit) {
        launch { planetAlpha.animateTo(1f, tween(900, easing = FastOutSlowInEasing)) }
        launch { planetOffset.animateTo(0f, tween(950, easing = FastOutSlowInEasing)) }
        delay(280)
        launch { textAlpha.animateTo(1f, tween(700, easing = FastOutSlowInEasing)) }
        launch { textOffset.animateTo(0f, tween(700, easing = FastOutSlowInEasing)) }
        delay(3000)
        onSplashFinished()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(SoraColors.Background)
    ) {
        // Planet — fills the bottom 62% of the screen
        Image(
            painter = painterResource(Res.drawable.planeta_splash),
            contentDescription = null,
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.5f)
                .align(Alignment.BottomCenter)
                .graphicsLayer {
                    alpha = planetAlpha.value
                    translationY = planetOffset.value
                }
        )

        // Gradient from black (top) → transparent (mid) to blend text area with planet
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.58f)
                .background(
                    Brush.verticalGradient(
                        colorStops = arrayOf(
                            0f to SoraColors.Background,
                            0.65f to SoraColors.Background,
                            1f to Color.Transparent
                        )
                    )
                )
        )

        // Title "SORA / SPACE" — Orbitron, stacked, top-left
        Column(
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(start = 28.dp, top = 72.dp)
                .graphicsLayer {
                    alpha = textAlpha.value
                    translationY = textOffset.value
                }
        ) {
            Text(
                text = "SORA",
                style = TextStyle(
                    fontFamily = orbitron,
                    fontSize = 58.sp,
                    fontWeight = FontWeight.SemiBold,
                    letterSpacing = 6.sp,
                    color = SoraColors.TextPrimary
                )
            )
            Text(
                text = "SPACE",
                style = TextStyle(
                    fontFamily = orbitron,
                    fontSize = 58.sp,
                    fontWeight = FontWeight.SemiBold,
                    letterSpacing = 6.sp,
                    color = SoraColors.TextPrimary
                )
            )
            Spacer(Modifier.height(16.dp))
            Text(
                text = strings.homeTagline,
                style = TextStyle(
                    fontFamily = spaceGrotesk,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Normal,
                    letterSpacing = 0.5.sp,
                    color = SoraColors.TextSecondary
                )
            )
        }
    }
}