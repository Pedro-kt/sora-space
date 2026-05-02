package com.yumedev.soraspace.presentation.home

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.togetherWith
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.OpenInNew
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ElevatedFilterChip
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.yumedev.soraspace.domain.model.ActivityLevel
import com.yumedev.soraspace.domain.model.SpaceArticle
import com.yumedev.soraspace.domain.model.SpaceLaunch
import com.yumedev.soraspace.domain.model.SpaceWeather
import com.yumedev.soraspace.ui.strings.LocalStrings
import com.yumedev.soraspace.ui.strings.Strings
import com.yumedev.soraspace.ui.theme.SoraColors
import com.yumedev.soraspace.ui.theme.SoraFonts
import com.yumedev.soraspace.ui.theme.SoraType
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
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
    val uiState              by viewModel.uiState.collectAsState()
    val favoriteArticleIds   by viewModel.favoriteArticleIds.collectAsState()
    val favoriteLaunchIds    by viewModel.favoriteLaunchIds.collectAsState()
    var selectedArticle by remember { mutableStateOf<SpaceArticle?>(null) }
    var selectedLaunch  by remember { mutableStateOf<SpaceLaunch?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(SoraColors.Background)
            .windowInsetsPadding(WindowInsets.safeDrawing.only(WindowInsetsSides.Vertical))
            .verticalScroll(rememberScrollState())
            .padding(top = 36.dp, bottom = 24.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Column(modifier = Modifier.padding(horizontal = 24.dp)) {
            Text(text = "SORA SPACE", style = SoraType.Label)
            Spacer(Modifier.height(8.dp))
            Text(text = today, style = SoraType.Title)
            Spacer(Modifier.height(4.dp))
            Text(text = s.homeTagline, style = SoraType.Body)
        }

        Spacer(Modifier.height(4.dp))

        val weather = uiState.spaceWeather
        AnimatedContent(
            targetState = uiState.isLoading,
            transitionSpec = {
                if (targetState) {
                    fadeIn(tween(200)) togetherWith fadeOut(tween(200))
                } else {
                    (fadeIn(tween(400)) + slideInVertically(tween(400)) { it / 6 }) togetherWith
                    fadeOut(tween(150))
                }
            },
            modifier = Modifier.padding(horizontal = 16.dp)
        ) { isLoading ->
            if (isLoading) {
                SpaceWeatherCardSkeleton()
            } else if (uiState.hasError) {
                SpaceWeatherCardError(onRetry = { viewModel.loadSpaceWeather() })
            } else if (weather != null) {
                SpaceWeatherCard(weather = weather)
            }
        }

        val featured = uiState.featuredArticle
        if (featured != null) {
            FeaturedStoryCard(
                article          = featured,
                isFavorited      = featured.id in favoriteArticleIds,
                onToggleFavorite = { viewModel.toggleArticleFavorite(featured) },
                modifier         = Modifier.padding(horizontal = 16.dp),
                onArticleClick   = { selectedArticle = it }
            )
        }

        AnimatedContent(
            targetState = uiState.isNewsLoading,
            transitionSpec = {
                if (targetState) {
                    fadeIn(tween(200)) togetherWith fadeOut(tween(200))
                } else {
                    (fadeIn(tween(400)) + slideInVertically(tween(400)) { it / 6 }) togetherWith
                    fadeOut(tween(150))
                }
            }
        ) { isLoading ->
            if (isLoading) {
                LatestNewsStripSkeleton()
            } else if (uiState.hasNewsError) {
                LatestNewsStripError(onRetry = { viewModel.loadNews() })
            } else if (uiState.latestNews.isNotEmpty()) {
                LatestNewsStrip(
                    articles         = uiState.latestNews,
                    favoriteIds      = favoriteArticleIds,
                    onArticleClick   = { selectedArticle = it },
                    onToggleFavorite = viewModel::toggleArticleFavorite
                )
            }
        }

        AnimatedContent(
            targetState = uiState.isLaunchesLoading,
            transitionSpec = {
                if (targetState) {
                    fadeIn(tween(200)) togetherWith fadeOut(tween(200))
                } else {
                    (fadeIn(tween(400)) + slideInVertically(tween(400)) { it / 6 }) togetherWith
                    fadeOut(tween(150))
                }
            }
        ) { isLoading ->
            if (isLoading) {
                LaunchesSectionSkeleton()
            } else if (uiState.hasLaunchesError) {
                LaunchesSectionError(onRetry = { viewModel.loadLaunches() })
            } else if (uiState.launches.isNotEmpty()) {
                LaunchesSection(
                    launches         = uiState.launches,
                    favoriteIds      = favoriteLaunchIds,
                    onLaunchClick    = { selectedLaunch = it },
                    onToggleFavorite = viewModel::toggleLaunchFavorite
                )
            }
        }

        Text(
            text = s.homeExploreLabel,
            style = SoraType.Label,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        NavigationTile(
            label = s.apodLabel,
            subtitle = s.apodSubtitle,
            backgroundImage = Res.drawable.apod,
            modifier = Modifier.height(190.dp).padding(horizontal = 16.dp),
            onClick = onNavigateToApod
        )

        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
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
            modifier = Modifier.height(130.dp).padding(horizontal = 16.dp),
            onClick = onNavigateToFavorites
        )
    }

    selectedArticle?.let { article ->
        ArticleDetailSheet(
            article          = article,
            isFavorited      = article.id in favoriteArticleIds,
            onToggleFavorite = { viewModel.toggleArticleFavorite(article) },
            onDismiss        = { selectedArticle = null }
        )
    }

    selectedLaunch?.let { launch ->
        LaunchDetailSheet(
            launch           = launch,
            isFavorited      = launch.id in favoriteLaunchIds,
            onToggleFavorite = { viewModel.toggleLaunchFavorite(launch) },
            onDismiss        = { selectedLaunch = null }
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
                SoraColors.Background,
            )
            .border(
                color = SoraColors.TextPrimary.copy(0.3f),
                width = 1.dp,
                shape = RoundedCornerShape(16.dp)
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

// ─── Featured Story Card ──────────────────────────────────────────────────────

@Composable
private fun FeaturedStoryCard(
    article: SpaceArticle,
    isFavorited: Boolean,
    onToggleFavorite: () -> Unit,
    onArticleClick: (SpaceArticle) -> Unit,
    modifier: Modifier = Modifier
) {
    val s = LocalStrings.current

    ElevatedCard(
        onClick = { onArticleClick(article) },
        modifier = modifier.fillMaxWidth().height(220.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 4.dp)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            AsyncImage(
                model = article.imageUrl,
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
                                0.4f to Color.Black.copy(alpha = 0.6f),
                                1.0f to Color.Black.copy(alpha = 0.92f)
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
                    text = article.newsSite.uppercase(),
                    style = SoraType.Label
                )
                Spacer(Modifier.height(6.dp))
                Text(
                    text = article.title,
                    style = SoraType.Title.copy(fontSize = 18.sp, lineHeight = 24.sp),
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    text = timeAgo(article.publishedAt, s),
                    style = SoraType.Caption
                )
            }
            IconButton(
                onClick  = onToggleFavorite,
                modifier = Modifier.align(Alignment.TopEnd).padding(4.dp)
            ) {
                Icon(
                    imageVector        = if (isFavorited) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                    contentDescription = "Favorite",
                    tint               = if (isFavorited) SoraColors.Accent else Color.White.copy(alpha = 0.8f),
                    modifier           = Modifier.size(20.dp)
                )
            }
        }
    }
}

// ─── Latest News Strip ────────────────────────────────────────────────────────

@Composable
private fun LatestNewsStrip(
    articles: List<SpaceArticle>,
    favoriteIds: Set<Int>,
    onArticleClick: (SpaceArticle) -> Unit,
    onToggleFavorite: (SpaceArticle) -> Unit,
    modifier: Modifier = Modifier
) {
    val s = LocalStrings.current
    Column(modifier = modifier) {
        Text(
            text = s.newsLatestLabel,
            style = SoraType.Label,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        Spacer(Modifier.height(10.dp))
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            contentPadding = PaddingValues(horizontal = 16.dp)
        ) {
            items(articles) { article ->
                NewsCard(
                    article          = article,
                    isFavorited      = article.id in favoriteIds,
                    onToggleFavorite = { onToggleFavorite(article) },
                    onArticleClick   = onArticleClick
                )
            }
        }
    }
}

@Composable
private fun NewsCard(
    article: SpaceArticle,
    isFavorited: Boolean,
    onToggleFavorite: () -> Unit,
    onArticleClick: (SpaceArticle) -> Unit,
    modifier: Modifier = Modifier
) {
    val s = LocalStrings.current

    ElevatedCard(
        onClick = { onArticleClick(article) },
        modifier = modifier.width(160.dp).height(180.dp),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 3.dp)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            AsyncImage(
                model = article.imageUrl,
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
                                0.45f to Color.Black.copy(alpha = 0.65f),
                                1.0f to Color.Black.copy(alpha = 0.92f)
                            )
                        )
                    )
            )
            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(10.dp)
            ) {
                Text(
                    text = article.newsSite.uppercase(),
                    style = SoraType.Label.copy(fontSize = 8.sp, letterSpacing = 1.sp)
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    text = article.title,
                    style = SoraType.Caption.copy(
                        color = Color.White,
                        fontWeight = FontWeight.SemiBold,
                        lineHeight = 16.sp
                    ),
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(Modifier.height(3.dp))
                Text(
                    text = timeAgo(article.publishedAt, s),
                    style = SoraType.Caption.copy(fontSize = 10.sp)
                )
            }
            IconButton(
                onClick  = onToggleFavorite,
                modifier = Modifier.align(Alignment.TopEnd).size(32.dp)
            ) {
                Icon(
                    imageVector        = if (isFavorited) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                    contentDescription = "Favorite",
                    tint               = if (isFavorited) SoraColors.Accent else Color.White.copy(alpha = 0.7f),
                    modifier           = Modifier.size(14.dp)
                )
            }
        }
    }
}

// ─── Launches Section ────────────────────────────────────────────────────────

@Composable
private fun LaunchesSection(
    launches: List<SpaceLaunch>,
    favoriteIds: Set<String>,
    onLaunchClick: (SpaceLaunch) -> Unit,
    onToggleFavorite: (SpaceLaunch) -> Unit,
    modifier: Modifier = Modifier
) {
    val s = LocalStrings.current
    Column(modifier = modifier) {
        Text(
            text     = s.launchesLabel,
            style    = SoraType.Label,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        Spacer(Modifier.height(10.dp))
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            contentPadding        = PaddingValues(horizontal = 16.dp)
        ) {
            items(launches, key = { it.id }) { launch ->
                LaunchCard(
                    launch           = launch,
                    isFavorited      = launch.id in favoriteIds,
                    onToggleFavorite = { onToggleFavorite(launch) },
                    onClick          = { onLaunchClick(launch) }
                )
            }
        }
    }
}

@Composable
private fun LaunchCard(
    launch: SpaceLaunch,
    isFavorited: Boolean,
    onToggleFavorite: () -> Unit,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val s = LocalStrings.current
    val statusColor = when (launch.statusAbbrev) {
        "Go"      -> Color(0xFF4CAF50)
        "TBD"     -> Color(0xFFFFB300)
        "Hold"    -> Color(0xFFFF6D00)
        "Success" -> SoraColors.Accent
        else      -> SoraColors.TextSecondary
    }

    ElevatedCard(
        onClick   = onClick,
        modifier  = modifier.width(190.dp).height(180.dp),
        shape     = RoundedCornerShape(12.dp),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 3.dp)
    ) {
        Box(Modifier.fillMaxSize()) {
            if (launch.imageUrl != null) {
                AsyncImage(
                    model              = launch.imageUrl,
                    contentDescription = null,
                    modifier           = Modifier.fillMaxSize(),
                    contentScale       = ContentScale.Crop
                )
            } else {
                Box(Modifier.fillMaxSize().background(SoraColors.SurfaceHigh))
            }

            Box(
                Modifier.fillMaxSize().background(
                    Brush.verticalGradient(
                        colorStops = arrayOf(
                            0.0f to Color.Transparent,
                            0.4f to Color.Black.copy(alpha = 0.55f),
                            1.0f to Color.Black.copy(alpha = 0.93f)
                        )
                    )
                )
            )

            // Status badge + favorite — top
            Row(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(6.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalAlignment     = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick  = onToggleFavorite,
                    modifier = Modifier.size(24.dp)
                ) {
                    Icon(
                        imageVector        = if (isFavorited) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                        contentDescription = "Favorite",
                        tint               = if (isFavorited) SoraColors.Accent else Color.White.copy(alpha = 0.7f),
                        modifier           = Modifier.size(14.dp)
                    )
                }
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(statusColor.copy(alpha = 0.18f))
                        .padding(horizontal = 8.dp, vertical = 3.dp)
                ) {
                    Text(
                        text  = launch.statusAbbrev,
                        style = SoraType.Label.copy(color = statusColor, fontSize = 9.sp)
                    )
                }
            }

            // Content — bottom
            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(10.dp)
            ) {
                if (launch.provider.isNotEmpty()) {
                    Text(
                        text  = launch.provider.uppercase(),
                        style = SoraType.Label.copy(fontSize = 8.sp, letterSpacing = 1.sp)
                    )
                    Spacer(Modifier.height(4.dp))
                }
                Text(
                    text     = launch.name,
                    style    = SoraType.Caption.copy(
                        color      = Color.White,
                        fontWeight = FontWeight.SemiBold,
                        lineHeight = 16.sp
                    ),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(Modifier.height(3.dp))
                Text(
                    text  = launchCountdown(launch.net, s),
                    style = SoraType.Caption.copy(fontSize = 10.sp)
                )
            }
        }
    }
}

@Composable
private fun LaunchesSectionError(onRetry: () -> Unit) {
    val s = LocalStrings.current
    Column {
        Text(
            text     = s.launchesLabel,
            style    = SoraType.Label,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        Spacer(Modifier.height(10.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(SoraColors.SurfaceHigh)
                .padding(horizontal = 16.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text     = s.errorNetworkMessage,
                style    = SoraType.Caption,
                modifier = Modifier.weight(1f)
            )
            Spacer(Modifier.width(12.dp))
            androidx.compose.material3.TextButton(onClick = onRetry) {
                Text(s.retry, style = SoraType.Label)
            }
        }
    }
}

@Composable
private fun LaunchesSectionSkeleton(modifier: Modifier = Modifier) {
    val shimmer = shimmerBrush()
    Column(modifier = modifier) {
        Box(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .height(10.dp)
                .width(150.dp)
                .clip(RoundedCornerShape(4.dp))
                .background(shimmer)
        )
        Spacer(Modifier.height(10.dp))
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            contentPadding        = PaddingValues(horizontal = 16.dp),
            userScrollEnabled     = false
        ) {
            items(4) {
                Box(
                    modifier = Modifier
                        .width(190.dp)
                        .height(180.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(shimmer)
                )
            }
        }
    }
}

// ─── Inline error states ─────────────────────────────────────────────────────

@Composable
private fun SpaceWeatherCardError(onRetry: () -> Unit) {
    val s = LocalStrings.current
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(SoraColors.SurfaceHigh)
            .padding(horizontal = 20.dp, vertical = 18.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = s.errorNetworkMessage,
                style = SoraType.Caption,
                modifier = Modifier.weight(1f)
            )
            Spacer(Modifier.width(12.dp))
            androidx.compose.material3.TextButton(onClick = onRetry) {
                Text(s.retry, style = SoraType.Label)
            }
        }
    }
}

@Composable
private fun LatestNewsStripError(onRetry: () -> Unit) {
    val s = LocalStrings.current
    Column {
        Text(
            text = s.newsLatestLabel,
            style = SoraType.Label,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        Spacer(Modifier.height(10.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(SoraColors.SurfaceHigh)
                .padding(horizontal = 16.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = s.errorNetworkMessage,
                style = SoraType.Caption,
                modifier = Modifier.weight(1f)
            )
            Spacer(Modifier.width(12.dp))
            androidx.compose.material3.TextButton(onClick = onRetry) {
                Text(s.retry, style = SoraType.Label)
            }
        }
    }
}

// ─── Skeletons ───────────────────────────────────────────────────────────────

@Composable
private fun shimmerBrush(): Brush {
    val transition = rememberInfiniteTransition()
    val offset by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1400, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        )
    )
    return Brush.linearGradient(
        colors = listOf(
            SoraColors.SurfaceHigh,
            Color(0xFF2E2E2E),
            SoraColors.SurfaceHigh,
        ),
        start = Offset(offset - 400f, 0f),
        end = Offset(offset + 400f, 0f)
    )
}

@Composable
private fun SpaceWeatherCardSkeleton(modifier: Modifier = Modifier) {
    val shimmer = shimmerBrush()
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(SoraColors.SurfaceHigh)
            .padding(horizontal = 20.dp, vertical = 18.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Box(
                    modifier = Modifier
                        .height(10.dp)
                        .width(90.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(shimmer)
                )
                Spacer(Modifier.height(10.dp))
                Box(
                    modifier = Modifier
                        .height(10.dp)
                        .width(180.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(shimmer)
                )
            }
            Box(
                modifier = Modifier
                    .height(32.dp)
                    .width(80.dp)
                    .clip(CircleShape)
                    .background(shimmer)
            )
        }
    }
}

@Composable
private fun LatestNewsStripSkeleton(modifier: Modifier = Modifier) {
    val shimmer = shimmerBrush()
    Column(modifier = modifier) {
        Box(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .height(10.dp)
                .width(110.dp)
                .clip(RoundedCornerShape(4.dp))
                .background(shimmer)
        )
        Spacer(Modifier.height(10.dp))
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            contentPadding = PaddingValues(horizontal = 16.dp),
            userScrollEnabled = false
        ) {
            items(4) {
                Box(
                    modifier = Modifier
                        .width(160.dp)
                        .height(180.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(shimmer)
                )
            }
        }
    }
}

// ─── Article Detail Sheet ────────────────────────────────────────────────────

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ArticleDetailSheet(
    article: SpaceArticle,
    isFavorited: Boolean,
    onToggleFavorite: () -> Unit,
    onDismiss: () -> Unit
) {
    val s = LocalStrings.current
    val uriHandler = LocalUriHandler.current
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val spaceGrotesk = SoraFonts.SpaceGrotesk

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = SoraColors.Surface,
        contentColor = SoraColors.TextPrimary,
        tonalElevation = 0.dp,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .navigationBarsPadding()
        ) {
            // Hero image
            AsyncImage(
                model = article.imageUrl,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp)
                    .padding(horizontal = 16.dp)
                    .clip(RoundedCornerShape(16.dp))
            )

            Column(modifier = Modifier.padding(horizontal = 20.dp)) {
                Spacer(Modifier.height(20.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment     = Alignment.Top
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        // Source
                        Text(
                            text = article.newsSite.uppercase(),
                            style = SoraType.Label
                        )
                        Spacer(Modifier.height(8.dp))

                        // Title
                        Text(
                            text = article.title,
                            style = SoraType.Title.copy(fontSize = 20.sp, lineHeight = 27.sp)
                        )
                    }
                    FilledIconButton(
                        onClick  = onToggleFavorite,
                        modifier = Modifier.size(36.dp),
                        colors   = IconButtonDefaults.filledIconButtonColors(
                            containerColor = if (isFavorited) SoraColors.AccentSubtle else SoraColors.SurfaceHigh,
                            contentColor   = if (isFavorited) SoraColors.Accent else SoraColors.TextSecondary
                        )
                    ) {
                        Icon(
                            imageVector        = if (isFavorited) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                            contentDescription = "Favorite",
                            modifier           = Modifier.size(18.dp)
                        )
                    }
                }

                // Authors
                if (article.authors.isNotEmpty()) {
                    Spacer(Modifier.height(10.dp))
                    Text(
                        text = "${s.newsBy} ${article.authors.joinToString(", ")}",
                        style = SoraType.Caption.copy(fontFamily = spaceGrotesk)
                    )
                }

                Spacer(Modifier.height(16.dp))

                // Summary
                Text(
                    text = article.summary,
                    style = SoraType.Body
                )

                Spacer(Modifier.height(20.dp))

                // Dates row
                Row(horizontalArrangement = Arrangement.spacedBy(24.dp)) {
                    Column {
                        Text(
                            text = s.newsPublishedLabel.uppercase(),
                            style = SoraType.Label.copy(fontSize = 8.sp, letterSpacing = 1.5.sp)
                        )
                        Spacer(Modifier.height(3.dp))
                        Text(
                            text = formatSheetDate(article.publishedAt, s.monthNames),
                            style = SoraType.Caption
                        )
                    }
                    if (article.updatedAt.isNotEmpty() && article.updatedAt != article.publishedAt) {
                        Column {
                            Text(
                                text = s.newsUpdatedLabel.uppercase(),
                                style = SoraType.Label.copy(fontSize = 8.sp, letterSpacing = 1.5.sp)
                            )
                            Spacer(Modifier.height(3.dp))
                            Text(
                                text = formatSheetDate(article.updatedAt, s.monthNames),
                                style = SoraType.Caption
                            )
                        }
                    }
                }

                Spacer(Modifier.height(28.dp))

                // Read more button
                Button(
                    onClick = {
                        uriHandler.openUri(article.url)
                        onDismiss()
                    },
                    modifier = Modifier.fillMaxWidth().height(52.dp),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = SoraColors.Accent,
                        contentColor = Color.Black
                    )
                ) {
                    Text(
                        text = s.newsReadMore,
                        style = SoraType.Caption.copy(
                            fontFamily = spaceGrotesk,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 15.sp,
                            color = Color.Black
                        )
                    )
                    Spacer(Modifier.width(8.dp))
                    Icon(
                        imageVector = Icons.AutoMirrored.Rounded.OpenInNew,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp),
                        tint = Color.Black
                    )
                }

                Spacer(Modifier.height(8.dp))
            }
        }
    }
}

// ─── Launch Detail Sheet ─────────────────────────────────────────────────────

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun LaunchDetailSheet(
    launch: SpaceLaunch,
    isFavorited: Boolean,
    onToggleFavorite: () -> Unit,
    onDismiss: () -> Unit
) {
    val s = LocalStrings.current
    val uriHandler = LocalUriHandler.current
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val spaceGrotesk = SoraFonts.SpaceGrotesk

    val statusColor = when (launch.statusAbbrev) {
        "Go"      -> Color(0xFF4CAF50)
        "TBD"     -> Color(0xFFFFB300)
        "Hold"    -> Color(0xFFFF6D00)
        "Success" -> SoraColors.Accent
        else      -> SoraColors.TextSecondary
    }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState       = sheetState,
        containerColor   = SoraColors.Surface,
        contentColor     = SoraColors.TextPrimary,
        tonalElevation   = 0.dp,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .navigationBarsPadding()
        ) {
            // Hero image / placeholder
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp)
                    .padding(horizontal = 16.dp)
                    .clip(RoundedCornerShape(16.dp))
            ) {
                if (launch.imageUrl != null) {
                    AsyncImage(
                        model              = launch.imageUrl,
                        contentDescription = null,
                        contentScale       = ContentScale.Crop,
                        modifier           = Modifier.fillMaxSize()
                    )
                } else {
                    Box(Modifier.fillMaxSize().background(SoraColors.SurfaceHigh))
                }
                Box(
                    Modifier.fillMaxSize().background(
                        Brush.verticalGradient(
                            colorStops = arrayOf(
                                0.0f to Color.Transparent,
                                0.55f to Color.Black.copy(alpha = 0.3f),
                                1.0f to Color.Black.copy(alpha = 0.5f)
                            )
                        )
                    )
                )
                // Status badge — top right
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(12.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(statusColor.copy(alpha = 0.20f))
                        .padding(horizontal = 10.dp, vertical = 5.dp)
                ) {
                    Text(
                        text  = launch.statusName,
                        style = SoraType.Label.copy(color = statusColor, fontSize = 10.sp)
                    )
                }
            }

            Column(modifier = Modifier.padding(horizontal = 20.dp)) {
                Spacer(Modifier.height(20.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment     = Alignment.Top
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        if (launch.provider.isNotEmpty()) {
                            Text(text = launch.provider.uppercase(), style = SoraType.Label)
                            Spacer(Modifier.height(8.dp))
                        }
                        Text(
                            text  = launch.name,
                            style = SoraType.Title.copy(fontSize = 20.sp, lineHeight = 27.sp)
                        )
                    }
                    FilledIconButton(
                        onClick  = onToggleFavorite,
                        modifier = Modifier.size(36.dp),
                        colors   = IconButtonDefaults.filledIconButtonColors(
                            containerColor = if (isFavorited) SoraColors.AccentSubtle else SoraColors.SurfaceHigh,
                            contentColor   = if (isFavorited) SoraColors.Accent else SoraColors.TextSecondary
                        )
                    ) {
                        Icon(
                            imageVector        = if (isFavorited) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                            contentDescription = "Favorite",
                            modifier           = Modifier.size(18.dp)
                        )
                    }
                }

                if (launch.missionDescription.isNotEmpty()) {
                    Spacer(Modifier.height(16.dp))
                    Text(text = launch.missionDescription, style = SoraType.Body)
                }

                Spacer(Modifier.height(20.dp))

                // Date + window row
                Row(horizontalArrangement = Arrangement.spacedBy(24.dp)) {
                    Column {
                        Text(
                            text  = s.launchNetLabel.uppercase(),
                            style = SoraType.Label.copy(fontSize = 8.sp, letterSpacing = 1.5.sp)
                        )
                        Spacer(Modifier.height(3.dp))
                        Text(
                            text  = formatSheetDate(launch.net, s.monthNames),
                            style = SoraType.Caption
                        )
                    }
                    if (!launch.windowStart.isNullOrEmpty()) {
                        Column {
                            Text(
                                text  = s.launchWindowLabel.uppercase(),
                                style = SoraType.Label.copy(fontSize = 8.sp, letterSpacing = 1.5.sp)
                            )
                            Spacer(Modifier.height(3.dp))
                            Text(
                                text  = formatSheetDate(launch.windowStart, s.monthNames),
                                style = SoraType.Caption
                            )
                        }
                    }
                }

                // Pad + location row
                if (launch.padName.isNotEmpty() || launch.location.isNotEmpty()) {
                    Spacer(Modifier.height(20.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(24.dp)) {
                        if (launch.padName.isNotEmpty()) {
                            Column {
                                Text(
                                    text  = s.launchPadLabel.uppercase(),
                                    style = SoraType.Label.copy(fontSize = 8.sp, letterSpacing = 1.5.sp)
                                )
                                Spacer(Modifier.height(3.dp))
                                Text(text = launch.padName, style = SoraType.Caption)
                            }
                        }
                        if (launch.location.isNotEmpty()) {
                            Column {
                                Text(
                                    text  = s.launchLocationLabel.uppercase(),
                                    style = SoraType.Label.copy(fontSize = 8.sp, letterSpacing = 1.5.sp)
                                )
                                Spacer(Modifier.height(3.dp))
                                Text(text = launch.location, style = SoraType.Caption)
                            }
                        }
                    }
                }

                // Mission type + orbit row
                if (launch.missionType.isNotEmpty() || launch.orbit.isNotEmpty()) {
                    Spacer(Modifier.height(20.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(24.dp)) {
                        if (launch.missionType.isNotEmpty()) {
                            Column {
                                Text(
                                    text  = s.launchMissionLabel.uppercase(),
                                    style = SoraType.Label.copy(fontSize = 8.sp, letterSpacing = 1.5.sp)
                                )
                                Spacer(Modifier.height(3.dp))
                                Text(text = launch.missionType, style = SoraType.Caption)
                            }
                        }
                        if (launch.orbit.isNotEmpty()) {
                            Column {
                                Text(
                                    text  = s.launchOrbitLabel.uppercase(),
                                    style = SoraType.Label.copy(fontSize = 8.sp, letterSpacing = 1.5.sp)
                                )
                                Spacer(Modifier.height(3.dp))
                                Text(text = launch.orbit, style = SoraType.Caption)
                            }
                        }
                    }
                }
/*
                if (launch.url.isNotEmpty()) {
                    Spacer(Modifier.height(28.dp))
                    Button(
                        onClick = {
                            uriHandler.openUri(launch.url)
                            onDismiss()
                        },
                        modifier = Modifier.fillMaxWidth().height(52.dp),
                        shape    = RoundedCornerShape(14.dp),
                        colors   = ButtonDefaults.buttonColors(
                            containerColor = SoraColors.Accent,
                            contentColor   = Color.Black
                        )
                    ) {
                        Text(
                            text  = s.newsReadMore,
                            style = SoraType.Caption.copy(
                                fontFamily  = spaceGrotesk,
                                fontWeight  = FontWeight.SemiBold,
                                fontSize    = 15.sp,
                                color       = Color.Black
                            )
                        )
                        Spacer(Modifier.width(8.dp))
                        Icon(
                            imageVector        = Icons.AutoMirrored.Rounded.OpenInNew,
                            contentDescription = null,
                            modifier           = Modifier.size(16.dp),
                            tint               = Color.Black
                        )
                    }
                }

 */

                Spacer(Modifier.height(8.dp))
            }
        }
    }
}

// ─── Helpers ─────────────────────────────────────────────────────────────────

@Suppress("DEPRECATION")
private fun launchCountdown(netStr: String, strings: Strings): String {
    return try {
        val net  = Instant.parse(netStr)
        val diff = net - Clock.System.now()
        when {
            diff.inWholeHours  <  1 -> "< 1h"
            diff.inWholeHours  < 24 -> "in ${diff.inWholeHours}h"
            diff.inWholeDays   <  2 -> "Tomorrow"
            diff.inWholeDays   <  7 -> "in ${diff.inWholeDays}d"
            else                    -> formatSheetDate(netStr, strings.monthNames)
        }
    } catch (_: Exception) { "TBD" }
}

@Suppress("DEPRECATION")
private fun formatSheetDate(isoString: String, monthNames: List<String>): String {
    return try {
        val local = Instant.parse(isoString).toLocalDateTime(TimeZone.currentSystemDefault())
        val month = monthNames.getOrElse(local.monthNumber - 1) { "" }.take(3)
        "$month ${local.dayOfMonth}, ${local.year}"
    } catch (_: Exception) { isoString }
}

@Suppress("DEPRECATION")
private fun formattedToday(monthNames: List<String>): String {
    val date = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
    val month = monthNames.getOrElse(date.monthNumber - 1) { "" }
    return "$month ${date.dayOfMonth}, ${date.year}"
}

@Suppress("DEPRECATION")
private fun timeAgo(publishedAt: String, strings: Strings): String {
    return try {
        val published = Instant.parse(publishedAt)
        val diff = Clock.System.now() - published
        when {
            diff.inWholeMinutes < 1  -> strings.newsJustNow
            diff.inWholeMinutes < 60 -> strings.newsMinutesAgo(diff.inWholeMinutes.toInt())
            diff.inWholeHours < 24   -> strings.newsHoursAgo(diff.inWholeHours.toInt())
            else                     -> strings.newsDaysAgo(diff.inWholeDays.toInt())
        }
    } catch (_: Exception) { "" }
}