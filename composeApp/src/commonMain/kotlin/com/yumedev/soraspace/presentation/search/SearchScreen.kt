package com.yumedev.soraspace.presentation.search

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yumedev.soraspace.domain.model.Asteroid
import com.yumedev.soraspace.ui.theme.SoraColors
import com.yumedev.soraspace.ui.theme.SoraType
import kotlin.math.roundToInt

@Composable
fun SearchScreen(
    viewModel: SearchViewModel,
    onBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(SoraColors.Background)
            .windowInsetsPadding(WindowInsets.safeDrawing.only(WindowInsetsSides.Vertical))
    ) {
        // Header
        Row(
            modifier              = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 16.dp),
            verticalAlignment     = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            FilledIconButton(
                onClick  = onBack,
                modifier = Modifier.size(36.dp),
                colors   = IconButtonDefaults.filledIconButtonColors(
                    containerColor = SoraColors.Surface,
                    contentColor   = SoraColors.TextPrimary
                )
            ) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", modifier = Modifier.size(18.dp))
            }
            Column {
                Text(text = "SPACE SEARCH", style = SoraType.Label)
                if (uiState is SearchUiState.Success) {
                    Text(
                        text  = (uiState as SearchUiState.Success).dateRange,
                        style = SoraType.Caption
                    )
                }
            }
        }

        when (val state = uiState) {
            is SearchUiState.Loading -> LoadingContent()
            is SearchUiState.Error   -> ErrorContent(state.message, viewModel::retry)
            is SearchUiState.Success -> AsteroidList(state.asteroids)
        }
    }
}

// ─── Lista ────────────────────────────────────────────────────────────────────

@Composable
private fun AsteroidList(asteroids: List<Asteroid>) {
    LazyColumn(
        modifier            = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding      = androidx.compose.foundation.layout.PaddingValues(
            horizontal = 16.dp, vertical = 4.dp
        )
    ) {
        item {
            Row(
                modifier              = Modifier.fillMaxWidth().padding(bottom = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment     = Alignment.CenterVertically
            ) {
                Text(
                    text  = "NEAR-EARTH OBJECTS",
                    style = SoraType.Label
                )
                Text(
                    text  = "${asteroids.size} objects",
                    style = SoraType.Caption
                )
            }
        }
        items(asteroids, key = { it.id }) { asteroid ->
            AsteroidCard(asteroid)
        }
        item { Spacer(Modifier.height(24.dp)) }
    }
}

@Composable
private fun AsteroidCard(asteroid: Asteroid) {
    val borderColor = if (asteroid.isPotentiallyHazardous)
        Color(0xFFFF4444).copy(alpha = 0.6f)
    else
        Color.Transparent

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, borderColor, RoundedCornerShape(10.dp))
            .background(SoraColors.Surface, RoundedCornerShape(10.dp))
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        // Nombre + badge peligroso
        Row(
            modifier              = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment     = Alignment.CenterVertically
        ) {
            Text(
                text     = asteroid.name,
                style    = SoraType.Body.copy(
                    color      = SoraColors.TextPrimary,
                    fontWeight = FontWeight.SemiBold,
                    fontSize   = 15.sp
                ),
                modifier = Modifier.weight(1f)
            )
            if (asteroid.isPotentiallyHazardous) {
                Row(
                    verticalAlignment     = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    modifier              = Modifier
                        .background(Color(0xFFFF4444).copy(alpha = 0.15f), RoundedCornerShape(4.dp))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Icon(
                        Icons.Filled.Warning,
                        contentDescription = null,
                        tint     = Color(0xFFFF4444),
                        modifier = Modifier.size(11.dp)
                    )
                    Text(
                        text  = "HAZARDOUS",
                        style = SoraType.Caption.copy(
                            color      = Color(0xFFFF4444),
                            fontWeight = FontWeight.Bold,
                            fontSize   = 10.sp
                        )
                    )
                }
            }
        }

        // Fecha de aproximación
        Text(
            text  = "Closest approach: ${asteroid.closestApproachDate}",
            style = SoraType.Caption.copy(color = SoraColors.Accent)
        )

        // Stats en grid 2x2
        Row(
            modifier              = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            StatBox(
                label = "DISTANCE",
                value = "${"%.2f".format(asteroid.missDistanceLunar)} LD",
                modifier = Modifier.weight(1f)
            )
            StatBox(
                label = "DIAMETER",
                value = "${"%.0f".format(asteroid.diameterMinKm * 1000)}–${"%.0f".format(asteroid.diameterMaxKm * 1000)} m",
                modifier = Modifier.weight(1f)
            )
        }
        Row(
            modifier              = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            StatBox(
                label = "VELOCITY",
                value = "${(asteroid.velocityKmH / 1000).roundToInt()} km/s",
                modifier = Modifier.weight(1f)
            )
            StatBox(
                label = "MISS DIST.",
                value = "${"%.0f".format(asteroid.missDistanceKm / 1000)} k km",
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun StatBox(label: String, value: String, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .background(SoraColors.SurfaceHigh, RoundedCornerShape(6.dp))
            .padding(horizontal = 10.dp, vertical = 8.dp)
    ) {
        Text(text = label, style = SoraType.Caption.copy(fontSize = 9.sp, letterSpacing = 1.sp))
        Spacer(Modifier.height(2.dp))
        Text(
            text  = value,
            style = SoraType.Body.copy(
                color      = SoraColors.TextPrimary,
                fontWeight = FontWeight.SemiBold,
                fontSize   = 13.sp
            )
        )
    }
}

// ─── Estados ─────────────────────────────────────────────────────────────────

@Composable
private fun LoadingContent() {
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator(color = SoraColors.Accent)
    }
}

@Composable
private fun ErrorContent(message: String, onRetry: () -> Unit) {
    Column(
        modifier            = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            Icons.Outlined.Search,
            contentDescription = null,
            tint     = SoraColors.TextTertiary,
            modifier = Modifier.size(52.dp)
        )
        Spacer(Modifier.height(16.dp))
        Text(text = message, style = SoraType.Body)
        Spacer(Modifier.height(16.dp))
        TextButton(onClick = onRetry) {
            Text("Retry", color = SoraColors.Accent)
        }
    }
}