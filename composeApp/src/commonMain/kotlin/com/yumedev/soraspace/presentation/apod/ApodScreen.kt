package com.yumedev.soraspace.presentation.apod

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.material.icons.filled.WifiOff
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.yumedev.soraspace.domain.model.Apod
import com.yumedev.soraspace.ui.strings.LocalStrings
import com.yumedev.soraspace.ui.theme.SoraColors
import com.yumedev.soraspace.ui.theme.SoraType

@Composable
fun ApodScreen(viewModel: ApodViewModel, onBack: () -> Unit) {
    val uiState   by viewModel.uiState.collectAsStateWithLifecycle()
    val favorites by viewModel.favorites.collectAsStateWithLifecycle()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(SoraColors.Background)
    ) {
        when (val state = uiState) {
            is ApodUiState.Loading -> LoadingContent()
            is ApodUiState.Success -> ApodContent(
                featured         = state.featured,
                feed             = state.feed,
                favorites        = favorites,
                onToggleFavorite = viewModel::toggleFavorite,
                onBack           = onBack
            )
            is ApodUiState.Error -> ErrorContent(
                message = state.message,
                onRetry = viewModel::loadHome
            )
        }
    }
}

// ─── Contenido principal ──────────────────────────────────────────────────────

@Composable
private fun ApodContent(
    featured: Apod,
    feed: List<Apod>,
    favorites: Set<String>,
    onToggleFavorite: (String) -> Unit,
    onBack: () -> Unit
) {
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        item {
            HeroSection(
                apod             = featured,
                isFavorited      = featured.date in favorites,
                onToggleFavorite = { onToggleFavorite(featured.date) },
                onBack           = onBack
            )
        }
        item { FeedSectionHeader(count = feed.size) }
        items(items = feed, key = { it.date }) { apod ->
            FeedItem(
                apod             = apod,
                isFavorited      = apod.date in favorites,
                onToggleFavorite = { onToggleFavorite(apod.date) }
            )
        }
        item { Spacer(Modifier.height(40.dp)) }
    }
}

// ─── Hero ─────────────────────────────────────────────────────────────────────

@Composable
private fun HeroSection(
    apod: Apod,
    isFavorited: Boolean,
    onToggleFavorite: () -> Unit,
    onBack: () -> Unit
) {
    Column {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(440.dp)
        ) {
            if (apod.mediaType == "image") {
                AsyncImage(
                    model              = apod.imageUrl,
                    contentDescription = null,
                    modifier           = Modifier.fillMaxSize(),
                    contentScale       = ContentScale.Crop
                )
            } else {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(SoraColors.Surface),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector        = Icons.Filled.PlayCircle,
                        contentDescription = null,
                        tint               = SoraColors.TextSecondary,
                        modifier           = Modifier.size(48.dp)
                    )
                }
            }

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            0f    to Color.Black.copy(alpha = 0.3f),
                            0.25f to Color.Transparent,
                            0.55f to Color.Transparent,
                            1f    to SoraColors.Background
                        )
                    )
            )

            // Botón volver — top left
            FilledIconButton(
                onClick  = onBack,
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .statusBarsPadding()
                    .padding(12.dp)
                    .size(36.dp),
                colors = IconButtonDefaults.filledIconButtonColors(
                    containerColor = Color.Black.copy(alpha = 0.5f),
                    contentColor   = SoraColors.TextPrimary
                )
            ) {
                Icon(
                    imageVector        = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    modifier           = Modifier.size(18.dp)
                )
            }

            // Botón favorito — top right
            FavoriteButton(
                isFavorited = isFavorited,
                onClick     = onToggleFavorite,
                modifier    = Modifier
                    .align(Alignment.TopEnd)
                    .statusBarsPadding()
                    .padding(12.dp)
            )
        }

        val s = LocalStrings.current
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .padding(bottom = 8.dp)
        ) {
            Text(text = s.apodSectionLabel, style = SoraType.Label)
            Spacer(Modifier.height(10.dp))
            Text(text = apod.title, style = SoraType.Title)
            Spacer(Modifier.height(10.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                MetaTag(apod.date)
                apod.copyright?.let { MetaTag("© $it") }
            }
            Spacer(Modifier.height(20.dp))
            Box(Modifier.width(28.dp).height(1.dp).background(SoraColors.Accent.copy(alpha = 0.5f)))
            Spacer(Modifier.height(20.dp))
            Text(text = apod.explanation, style = SoraType.Body)
            Spacer(Modifier.height(32.dp))
        }
    }
}

// ─── Feed ─────────────────────────────────────────────────────────────────────

@Composable
private fun FeedSectionHeader(count: Int) {
    val s = LocalStrings.current
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
            .padding(bottom = 16.dp),
        verticalAlignment     = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(text = s.apodRecentDays, style = SoraType.Label)
        Box(
            modifier = Modifier
                .weight(1f)
                .height(1.dp)
                .background(SoraColors.TextTertiary)
        )
        Text(
            text  = "$count",
            style = SoraType.Label.copy(color = SoraColors.TextSecondary)
        )
    }
}

@Composable
private fun FeedItem(
    apod: Apod,
    isFavorited: Boolean,
    onToggleFavorite: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .padding(bottom = 12.dp)
            .clip(RoundedCornerShape(8.dp))
            .height(220.dp)
    ) {
        if (apod.mediaType == "image") {
            AsyncImage(
                model              = apod.imageUrl,
                contentDescription = null,
                modifier           = Modifier.fillMaxSize(),
                contentScale       = ContentScale.Crop
            )
        } else {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(SoraColors.Surface),
                contentAlignment = Alignment.Center
            ) {
                Text("▶", style = SoraType.Title, color = SoraColors.TextSecondary)
            }
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        0f    to Color.Transparent,
                        0.45f to Color.Transparent,
                        1f    to Color.Black.copy(alpha = 0.88f)
                    )
                )
        )

        Row(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment     = Alignment.Bottom
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text     = apod.title,
                    style    = SoraType.Caption.copy(
                        color      = SoraColors.TextPrimary,
                        fontWeight = FontWeight.SemiBold
                    ),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(Modifier.height(6.dp))
                MetaTag(apod.date)
            }
            Spacer(Modifier.width(8.dp))
            FavoriteButton(isFavorited = isFavorited, onClick = onToggleFavorite)
        }
    }
}

// ─── Estados ─────────────────────────────────────────────────────────────────

@Composable
private fun LoadingContent() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator(
            modifier    = Modifier.size(28.dp),
            color       = SoraColors.Accent,
            strokeWidth = 1.5.dp,
            trackColor  = SoraColors.TextTertiary.copy(alpha = 0.3f)
        )
    }
}

@Composable
private fun ErrorContent(message: String, onRetry: () -> Unit) {
    val s = LocalStrings.current
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(20.dp),
            modifier            = Modifier.padding(40.dp)
        ) {
            Icon(
                imageVector        = Icons.Filled.WifiOff,
                contentDescription = null,
                tint               = SoraColors.Accent,
                modifier           = Modifier.size(32.dp)
            )
            Text(text = s.apodErrorTitle, style = SoraType.Title)
            Text(
                text  = message,
                style = SoraType.Body.copy(textAlign = TextAlign.Center)
            )
            OutlinedButton(
                onClick         = onRetry,
                shape           = RoundedCornerShape(4.dp),
                contentPadding  = PaddingValues(horizontal = 32.dp, vertical = 12.dp),
                colors          = ButtonDefaults.outlinedButtonColors(
                    contentColor = SoraColors.Accent
                ),
                border = androidx.compose.foundation.BorderStroke(
                    1.dp, SoraColors.Accent.copy(alpha = 0.5f)
                )
            ) {
                Text(s.retry.uppercase(), style = SoraType.Label)
            }
        }
    }
}

// ─── Componentes compartidos ──────────────────────────────────────────────────

@Composable
private fun FavoriteButton(
    isFavorited: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    FilledIconButton(
        onClick  = onClick,
        modifier = modifier.size(36.dp),
        colors   = IconButtonDefaults.filledIconButtonColors(
            containerColor = Color.Black.copy(alpha = 0.5f),
            contentColor   = if (isFavorited) SoraColors.Accent else SoraColors.TextSecondary
        )
    ) {
        Icon(
            imageVector        = if (isFavorited) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
            contentDescription = null,
            modifier           = Modifier.size(16.dp)
        )
    }
}

@Composable
private fun MetaTag(text: String) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(3.dp))
            .background(SoraColors.AccentSubtle)
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Text(text = text, style = SoraType.Tag)
    }
}