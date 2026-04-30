package com.yumedev.soraspace.presentation.search

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yumedev.soraspace.domain.model.Asteroid
import com.yumedev.soraspace.domain.model.EonetEvent
import com.yumedev.soraspace.ui.components.SoraErrorCard
import com.yumedev.soraspace.ui.strings.LocalStrings
import com.yumedev.soraspace.ui.strings.Strings
import com.yumedev.soraspace.ui.theme.SoraColors
import com.yumedev.soraspace.ui.theme.SoraType
import kotlin.math.roundToInt

@Composable
fun SearchScreen(
    viewModel: SearchViewModel,
    onBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val s = LocalStrings.current

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
                Icon(
                    Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    modifier           = Modifier.size(18.dp)
                )
            }
            Text(text = s.screenSpaceSearch, style = SoraType.Label)
        }

        // Tab selector
        TabSelector(
            selectedTab   = uiState.selectedTab,
            onTabSelected = viewModel::selectTab,
            strings       = s
        )

        Spacer(Modifier.height(8.dp))

        // Content
        when (uiState.selectedTab) {
            SearchTab.ASTEROIDS    -> AsteroidsContent(
                state   = uiState.asteroidsState,
                onRetry = viewModel::retryAsteroids,
                s       = s
            )
            SearchTab.EARTH_EVENTS -> EarthEventsContent(
                state            = uiState.eventsState,
                selectedCategory = uiState.selectedCategory,
                onCategorySelect = viewModel::selectCategory,
                onRetry          = viewModel::retryEvents,
                s                = s
            )
        }
    }
}

// ─── Tab selector ─────────────────────────────────────────────────────────────

@Composable
private fun TabSelector(
    selectedTab: SearchTab,
    onTabSelected: (SearchTab) -> Unit,
    strings: Strings
) {
    Row(
        modifier              = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        SearchTab.entries.forEach { tab ->
            val isSelected = tab == selectedTab
            val label = when (tab) {
                SearchTab.ASTEROIDS    -> strings.tabAsteroids
                SearchTab.EARTH_EVENTS -> strings.tabEarthEvents
            }
            Box(
                modifier = Modifier
                    .background(
                        color = if (isSelected) SoraColors.Accent else SoraColors.Surface,
                        shape = RoundedCornerShape(8.dp)
                    )
                    .clickable { onTabSelected(tab) }
                    .padding(horizontal = 16.dp, vertical = 10.dp)
            ) {
                Text(
                    text  = label,
                    style = SoraType.Label.copy(
                        color    = if (isSelected) SoraColors.Background else SoraColors.TextSecondary,
                        fontSize = 10.sp
                    )
                )
            }
        }
    }
}

// ─── Asteroids tab ────────────────────────────────────────────────────────────

@Composable
private fun AsteroidsContent(
    state: SearchUiState.AsteroidsState,
    onRetry: () -> Unit,
    s: Strings
) {
    when (state) {
        is SearchUiState.AsteroidsState.Loading -> LoadingContent()
        is SearchUiState.AsteroidsState.Error   -> SoraErrorCard(
            title    = s.apodErrorTitle,
            message  = s.errorNetworkMessage,
            onRetry  = onRetry,
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 24.dp)
        )
        is SearchUiState.AsteroidsState.Success -> AsteroidList(state.asteroids, state.dateRange, s)
    }
}

@Composable
private fun AsteroidList(asteroids: List<Asteroid>, dateRange: String, s: Strings) {
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
                Text(text = s.nearEarthObjects, style = SoraType.Label)
                Text(text = s.asteroidsCount(asteroids.size), style = SoraType.Caption)
            }
            Text(text = dateRange, style = SoraType.Caption.copy(fontSize = 11.sp))
            Spacer(Modifier.height(4.dp))
        }
        items(asteroids, key = { it.id }) { asteroid ->
            AsteroidCard(asteroid)
        }
        item { Spacer(Modifier.height(24.dp)) }
    }
}

@Composable
private fun AsteroidCard(asteroid: Asteroid) {
    val s = LocalStrings.current
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
                        text  = s.hazardous,
                        style = SoraType.Caption.copy(
                            color      = Color(0xFFFF4444),
                            fontWeight = FontWeight.Bold,
                            fontSize   = 10.sp
                        )
                    )
                }
            }
        }

        Text(
            text  = "${s.closestApproach} ${asteroid.closestApproachDate}",
            style = SoraType.Caption.copy(color = SoraColors.Accent)
        )

        Row(
            modifier              = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            StatBox(
                label    = s.labelDistance,
                value    = "${"%.2f".format(asteroid.missDistanceLunar)} LD",
                modifier = Modifier.weight(1f)
            )
            StatBox(
                label    = s.labelDiameter,
                value    = "${"%.0f".format(asteroid.diameterMinKm * 1000)}–${"%.0f".format(asteroid.diameterMaxKm * 1000)} m",
                modifier = Modifier.weight(1f)
            )
        }
        Row(
            modifier              = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            StatBox(
                label    = s.labelVelocity,
                value    = "${(asteroid.velocityKmH / 1000).roundToInt()} km/s",
                modifier = Modifier.weight(1f)
            )
            StatBox(
                label    = s.labelMissDist,
                value    = "${"%.0f".format(asteroid.missDistanceKm / 1000)} k km",
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

// ─── Earth Events tab ─────────────────────────────────────────────────────────

@Composable
private fun EarthEventsContent(
    state: SearchUiState.EventsState,
    selectedCategory: String?,
    onCategorySelect: (String?) -> Unit,
    onRetry: () -> Unit,
    s: Strings
) {
    when (state) {
        is SearchUiState.EventsState.Idle,
        is SearchUiState.EventsState.Loading -> LoadingContent()
        is SearchUiState.EventsState.Error   -> SoraErrorCard(
            title    = s.apodErrorTitle,
            message  = s.errorNetworkMessage,
            onRetry  = onRetry,
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 24.dp)
        )
        is SearchUiState.EventsState.Success -> {
            val categories = state.events
                .map { it.categoryId to s.eonetCategoryName(it.categoryId) }
                .distinctBy { it.first }
                .sortedBy { it.second }

            val filtered = if (selectedCategory == null) state.events
            else state.events.filter { it.categoryId == selectedCategory }

            Column {
                CategoryChips(
                    categories       = categories,
                    selectedCategory = selectedCategory,
                    onCategorySelect = onCategorySelect,
                    filterAllLabel   = s.filterAll
                )
                Spacer(Modifier.height(8.dp))
                EventList(events = filtered, total = state.events.size, s = s)
            }
        }
    }
}

@Composable
private fun CategoryChips(
    categories: List<Pair<String, String>>,
    selectedCategory: String?,
    onCategorySelect: (String?) -> Unit,
    filterAllLabel: String
) {
    Row(
        modifier            = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState())
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        CategoryChip(
            label      = filterAllLabel,
            color      = SoraColors.Accent,
            isSelected = selectedCategory == null,
            onClick    = { onCategorySelect(null) }
        )
        categories.forEach { (id, name) ->
            CategoryChip(
                label      = name.uppercase(),
                color      = categoryColor(id),
                isSelected = selectedCategory == id,
                onClick    = { onCategorySelect(id) }
            )
        }
    }
}

@Composable
private fun CategoryChip(
    label: String,
    color: Color,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .background(
                color = if (isSelected) color.copy(alpha = 0.2f) else SoraColors.Surface,
                shape = RoundedCornerShape(20.dp)
            )
            .border(
                width = 1.dp,
                color = if (isSelected) color else SoraColors.SurfaceHigh,
                shape = RoundedCornerShape(20.dp)
            )
            .clickable(onClick = onClick)
            .padding(horizontal = 12.dp, vertical = 6.dp)
    ) {
        Text(
            text  = label,
            style = SoraType.Caption.copy(
                color      = if (isSelected) color else SoraColors.TextSecondary,
                fontSize   = 10.sp,
                fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
                letterSpacing = 0.8.sp
            )
        )
    }
}

@Composable
private fun EventList(events: List<EonetEvent>, total: Int, s: Strings) {
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
                Text(text = s.activeEvents, style = SoraType.Label)
                Text(
                    text  = if (events.size == total) s.asteroidsCount(total)
                            else s.eventsCount(events.size, total),
                    style = SoraType.Caption
                )
            }
        }
        items(events, key = { it.id }) { event ->
            EonetEventCard(event)
        }
        item { Spacer(Modifier.height(24.dp)) }
    }
}

@Composable
private fun EonetEventCard(event: EonetEvent) {
    val s     = LocalStrings.current
    val color = categoryColor(event.categoryId)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(SoraColors.Surface, RoundedCornerShape(10.dp))
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Row(
            modifier              = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment     = Alignment.Top
        ) {
            Text(
                text     = event.title,
                style    = SoraType.Body.copy(
                    color      = SoraColors.TextPrimary,
                    fontWeight = FontWeight.SemiBold,
                    fontSize   = 15.sp
                ),
                modifier = Modifier.weight(1f)
            )
            Box(
                modifier = Modifier
                    .background(Color(0xFF2E7D32).copy(alpha = 0.2f), RoundedCornerShape(4.dp))
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Text(
                    text  = s.active,
                    style = SoraType.Caption.copy(
                        color      = Color(0xFF66BB6A),
                        fontWeight = FontWeight.Bold,
                        fontSize   = 10.sp
                    )
                )
            }
        }

        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment     = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .background(color.copy(alpha = 0.15f), RoundedCornerShape(4.dp))
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Text(
                    text  = s.eonetCategoryName(event.categoryId).uppercase(),
                    style = SoraType.Caption.copy(
                        color         = color,
                        fontWeight    = FontWeight.Medium,
                        fontSize      = 10.sp,
                        letterSpacing = 0.8.sp
                    )
                )
            }

            if (event.date.isNotEmpty()) {
                Text(
                    text  = "${s.lastUpdate} ${event.date}",
                    style = SoraType.Caption.copy(fontSize = 11.sp)
                )
            }
        }
    }
}

// ─── Shared states ────────────────────────────────────────────────────────────

@Composable
private fun LoadingContent() {
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator(color = SoraColors.Accent)
    }
}


// ─── Category colors ──────────────────────────────────────────────────────────

private fun categoryColor(id: String): Color = when (id) {
    "wildfires"     -> Color(0xFFFF6B35)
    "severeStorms"  -> Color(0xFF64B5F6)
    "volcanoes"     -> Color(0xFFFF7043)
    "seaAndLakeIce" -> Color(0xFF80DEEA)
    "floods"        -> Color(0xFF4DB6AC)
    "earthquakes"   -> Color(0xFFFFD54F)
    "drought"       -> Color(0xFFFFCC02)
    "snow"          -> Color(0xFFE0E0E0)
    "dustHaze"      -> Color(0xFFBCAAA4)
    "landslides"    -> Color(0xFFA1887F)
    "manmade"       -> Color(0xFFCE93D8)
    "waterColor"    -> Color(0xFF26C6DA)
    "tempExtremes"  -> Color(0xFFEF9A9A)
    else            -> SoraColors.Accent
}