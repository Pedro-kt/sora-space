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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ElevatedFilterChip
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
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
    val uiState by viewModel.uiState.collectAsState()
    var selectedArticle by remember { mutableStateOf<SpaceArticle?>(null) }

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
            } else if (!uiState.hasError && weather != null) {
                SpaceWeatherCard(weather = weather)
            }
        }

        val featured = uiState.featuredArticle
        if (featured != null) {
            FeaturedStoryCard(
                article = featured,
                modifier = Modifier.padding(horizontal = 16.dp),
                onArticleClick = { selectedArticle = it }
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
            } else if (uiState.latestNews.isNotEmpty()) {
                LatestNewsStrip(
                    articles = uiState.latestNews,
                    onArticleClick = { selectedArticle = it }
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
            article = article,
            onDismiss = { selectedArticle = null }
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

// ─── Featured Story Card ──────────────────────────────────────────────────────

@Composable
private fun FeaturedStoryCard(
    article: SpaceArticle,
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
        }
    }
}

// ─── Latest News Strip ────────────────────────────────────────────────────────

@Composable
private fun LatestNewsStrip(
    articles: List<SpaceArticle>,
    onArticleClick: (SpaceArticle) -> Unit,
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
                NewsCard(article = article, onArticleClick = onArticleClick)
            }
        }
    }
}

@Composable
private fun NewsCard(
    article: SpaceArticle,
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

// ─── Helpers ─────────────────────────────────────────────────────────────────

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