package com.yumedev.soraspace.presentation.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ElevatedFilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yumedev.soraspace.domain.model.ActivityLevel
import com.yumedev.soraspace.domain.model.SpaceWeather
import com.yumedev.soraspace.ui.strings.LocalStrings
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
import soraspace.composeapp.generated.resources.nebulous
import soraspace.composeapp.generated.resources.space_search

@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    onNavigateToApod: () -> Unit,
    onNavigateToMars: () -> Unit,
    onNavigateToSearch: () -> Unit,
    onNavigateToFavorites: () -> Unit
) {
    val s = LocalStrings.current
    val today = remember(s) { formattedToday(s.monthNames) }
    val uiState by viewModel.uiState.collectAsState()

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
            Text(text = s.homeTagline, style = SoraType.Body)
        }

        Spacer(Modifier.height(4.dp))

        val weather = uiState.spaceWeather
        if (!uiState.isLoading && !uiState.hasError && weather != null) {
            SpaceWeatherCard(weather = weather)
        }

        Spacer(Modifier.height(4.dp))

        NavigationTile(
            label = s.apodLabel,
            subtitle = s.apodSubtitle,
            backgroundImage = Res.drawable.apod,
            modifier = Modifier.height(190.dp),
            onClick = onNavigateToApod
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            NavigationTile(
                label = s.mediaExplorerLabel,
                subtitle = s.mediaExplorerSubtitle,
                backgroundImage = Res.drawable.mars,
                modifier = Modifier.weight(1f).height(155.dp),
                onClick = onNavigateToMars
            )
            NavigationTile(
                label = s.spaceSearchLabel,
                subtitle = s.spaceSearchSubtitle,
                backgroundImage = Res.drawable.space_search,
                modifier = Modifier.weight(1f).height(155.dp),
                onClick = onNavigateToSearch
            )
        }

        NavigationTile(
            label = s.favoritesLabel,
            subtitle = s.favoritesSubtitle,
            backgroundImage = Res.drawable.nebulous,
            modifier = Modifier.height(130.dp),
            onClick = onNavigateToFavorites
        )
    }
}

// ─── Space Weather Card ───────────────────────────────────────────────────────

@Composable
private fun SpaceWeatherCard(
    weather: SpaceWeather,
    modifier: Modifier = Modifier
) {
    val s = LocalStrings.current

    val activityColor = when (weather.activityLevel) {
        ActivityLevel.QUIET -> Color(0xFF4CAF50)
        ActivityLevel.MINOR -> Color(0xFFCDDC39)
        ActivityLevel.MODERATE -> Color(0xFFFF9800)
        ActivityLevel.SEVERE -> Color(0xFFF44336)
    }

    val activityLabel = when (weather.activityLevel) {
        ActivityLevel.QUIET -> s.spaceWeatherQuiet
        ActivityLevel.MINOR -> s.spaceWeatherMinor
        ActivityLevel.MODERATE -> s.spaceWeatherModerate
        ActivityLevel.SEVERE -> s.spaceWeatherSevere
    }

    val subtext = if (weather.latestFlare != null) {
        "${s.spaceWeatherLatest}: ${weather.latestFlare.classType} · ${s.spaceWeatherFlareCount(weather.flareCount)}"
    } else {
        s.spaceWeatherNoFlares
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(
                SoraColors.SurfaceHigh,
            )
            .padding(horizontal = 20.dp, vertical = 18.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = s.spaceWeatherLabel, style = SoraType.Label)
                Spacer(Modifier.height(6.dp))
                Text(text = subtext, style = SoraType.Caption)
            }
            ElevatedFilterChip(
                onClick = { /* No-op */ },
                label = { Text(activityLabel) },
                colors = FilterChipDefaults.elevatedFilterChipColors(
                    containerColor = activityColor.copy(alpha = 0.15f),
                    labelColor = activityColor
                ),
                shape = CircleShape,
                modifier = Modifier.height(32.dp),
                selected = false
            )
        }
    }
}

// ─── Navigation Tile ─────────────────────────────────────────────────────────

@Composable
private fun NavigationTile(
    label: String,
    subtitle: String,
    backgroundImage: DrawableResource,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    ElevatedCard(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 4.dp),
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Image(
                painter = painterResource(backgroundImage),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colorStops = arrayOf(
                                0.0f to Color.Transparent,
                                0.45f to Color.Black.copy(alpha = 0.7f),
                                1.0f to Color.Black.copy(0.9f)
                            )
                        )
                    )
            )
            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(horizontal = 16.dp, vertical = 14.dp)
            ) {
                Text(
                    text = label,
                    style = SoraType.Caption.copy(
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        lineHeight = 20.sp
                    )
                )
                Spacer(Modifier.height(3.dp))
                Text(
                    text = subtitle,
                    style = SoraType.Caption.copy(
                        color = Color.White.copy(alpha = 0.70f),
                        fontSize = 12.sp
                    )
                )
            }
        }
    }
}

// ─── Helpers ─────────────────────────────────────────────────────────────────

private fun formattedToday(monthNames: List<String>): String {
    val date = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
    val month = monthNames.getOrElse(date.monthNumber - 1) { "" }
    return "$month ${date.dayOfMonth}, ${date.year}"
}