package com.yumedev.soraspace.presentation.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yumedev.soraspace.ui.theme.SoraColors
import com.yumedev.soraspace.ui.theme.SoraType
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import soraspace.composeapp.generated.resources.Res
import soraspace.composeapp.generated.resources.apod
import soraspace.composeapp.generated.resources.mars
import soraspace.composeapp.generated.resources.space_search

@Composable
fun HomeScreen(
    onNavigateToApod: () -> Unit,
    onNavigateToMars: () -> Unit,
    onNavigateToSearch: () -> Unit,
    onNavigateToFavorites: () -> Unit
) {
    val today = remember { formattedToday() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(SoraColors.Background)
            .windowInsetsPadding(WindowInsets.safeDrawing.only(WindowInsetsSides.Vertical))
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp)
            .padding(top = 36.dp, bottom = 24.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Column(modifier = Modifier.padding(horizontal = 8.dp)) {
            Text(text = "SORA SPACE", style = SoraType.Label)
            Spacer(Modifier.height(8.dp))
            Text(text = today, style = SoraType.Title)
            Spacer(Modifier.height(4.dp))
            Text(text = "Explore the universe", style = SoraType.Body)
        }

        Spacer(Modifier.height(12.dp))

        NavigationTile(
            icon            = Icons.Filled.AutoAwesome,
            label           = "ASTRONOMY PICTURE\nOF THE DAY",
            subtitle        = "Daily cosmos imagery",
            backgroundImage = Res.drawable.apod,
            modifier        = Modifier.height(190.dp),
            onClick         = onNavigateToApod
        )

        Row(
            modifier              = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            NavigationTile(
                icon            = Icons.Filled.Explore,
                label           = "MEDIA\nEXPLORER",
                subtitle        = "NASA image & video library",
                backgroundImage = Res.drawable.mars,
                modifier        = Modifier.weight(1f).height(155.dp),
                onClick         = onNavigateToMars
            )
            NavigationTile(
                icon            = Icons.Filled.Search,
                label           = "SPACE\nSEARCH",
                subtitle        = "NASA library",
                backgroundImage = Res.drawable.space_search,
                modifier        = Modifier.weight(1f).height(155.dp),
                onClick         = onNavigateToSearch
            )
        }

        NavigationTile(
            icon     = Icons.Filled.Favorite,
            label    = "FAVORITES",
            subtitle = "Your saved collection",
            gradient = Brush.linearGradient(
                colorStops = arrayOf(
                    0.0f to Color(0xFF3D0066),
                    0.6f to Color(0xFF1A0040),
                    1.0f to Color(0xFF0D001A)
                )
            ),
            modifier = Modifier.height(115.dp),
            onClick  = onNavigateToFavorites
        )
    }
}

// ─── Componentes ─────────────────────────────────────────────────────────────

@Composable
private fun NavigationTile(
    icon: ImageVector,
    label: String,
    subtitle: String,
    backgroundImage: DrawableResource? = null,
    gradient: Brush = Brush.linearGradient(listOf(Color(0xFF0A0F22), Color(0xFF162040))),
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.96f else 1f,
        animationSpec = tween(durationMillis = 100),
        label = "tile_scale"
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .scale(scale)
            .clip(RoundedCornerShape(10.dp))
            .clickable(
                interactionSource = interactionSource,
                indication        = null,
                onClick           = onClick
            )
    ) {
        // Fondo: imagen real o gradiente sólido
        if (backgroundImage != null) {
            Image(
                painter            = painterResource(backgroundImage),
                contentDescription = null,
                modifier           = Modifier.fillMaxSize(),
                contentScale       = ContentScale.Crop
            )
            // Overlay oscuro para que el texto sea legible sobre la foto
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            listOf(
                                Color.Black.copy(alpha = 0.45f),
                                Color.Black.copy(alpha = 0.70f)
                            )
                        )
                    )
            )
        } else {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(gradient)
            )
        }

        // Contenido
        Column(modifier = Modifier.padding(20.dp)) {
            Icon(
                imageVector        = icon,
                contentDescription = null,
                tint               = SoraColors.Accent,
                modifier           = Modifier.size(22.dp)
            )
            Spacer(Modifier.height(12.dp))
            Text(
                text  = label,
                style = SoraType.Caption.copy(
                    color      = SoraColors.TextPrimary,
                    fontWeight = FontWeight.Bold,
                    lineHeight = 19.sp
                )
            )
            Spacer(Modifier.height(5.dp))
            Text(text = subtitle, style = SoraType.Caption)
        }

        Icon(
            imageVector        = Icons.Filled.ChevronRight,
            contentDescription = null,
            tint               = SoraColors.Accent,
            modifier           = Modifier
                .align(Alignment.BottomEnd)
                .padding(20.dp)
                .size(16.dp)
        )
    }
}

// ─── Helpers ─────────────────────────────────────────────────────────────────

private fun formattedToday(): String {
    val date = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
    val month = when (date.monthNumber) {
        1 -> "January";  2 -> "February"; 3 -> "March"
        4 -> "April";    5 -> "May";      6 -> "June"
        7 -> "July";     8 -> "August";   9 -> "September"
        10 -> "October"; 11 -> "November"; else -> "December"
    }
    return "$month ${date.dayOfMonth}, ${date.year}"
}