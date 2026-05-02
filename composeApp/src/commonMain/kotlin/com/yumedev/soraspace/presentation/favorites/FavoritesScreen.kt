package com.yumedev.soraspace.presentation.favorites

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.aspectRatio
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.ElevatedFilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.yumedev.soraspace.domain.model.Apod
import com.yumedev.soraspace.domain.model.NasaMedia
import com.yumedev.soraspace.domain.model.SpaceArticle
import com.yumedev.soraspace.domain.model.SpaceLaunch
import com.yumedev.soraspace.ui.strings.LocalStrings
import com.yumedev.soraspace.ui.theme.SoraColors
import com.yumedev.soraspace.ui.theme.SoraType
import io.ktor.client.request.invoke

@Composable
fun FavoritesScreen(viewModel: FavoritesViewModel, onBack: () -> Unit) {
    val s               = LocalStrings.current
    val selectedTab     by viewModel.selectedTab.collectAsState()
    val apodFavorites   by viewModel.apodFavorites.collectAsState()
    val mediaFavorites  by viewModel.mediaFavorites.collectAsState()
    val launchFavorites by viewModel.launchFavorites.collectAsState()
    val articleFavorites by viewModel.articleFavorites.collectAsState()

    val totalCount = apodFavorites.size + mediaFavorites.size + launchFavorites.size + articleFavorites.size

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(SoraColors.Background)
            .windowInsetsPadding(WindowInsets.safeDrawing.only(WindowInsetsSides.Vertical))
    ) {
        // Header
        Row(
            modifier = Modifier
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
                    imageVector        = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    modifier           = Modifier.size(18.dp)
                )
            }
            Text(text = s.screenFavorites, style = SoraType.Label)
        }

        // Filter chips
        LazyRow(
            contentPadding        = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier              = Modifier.fillMaxWidth()
        ) {
            val tabs = listOf(
                FavoriteTab.All      to s.favoritesTabAll,
                FavoriteTab.Apod     to "APOD",
                FavoriteTab.Media    to "Media",
                FavoriteTab.Launches to s.favoritesTabLaunches,
                FavoriteTab.News     to s.favoritesTabNews
            )
            items(tabs) { (tab, label) ->
                ElevatedFilterChip(
                    selected = selectedTab == tab,
                    onClick  = { viewModel.onTabSelected(tab) },
                    label    = { Text(label, style = SoraType.Caption) },
                    colors   = FilterChipDefaults.elevatedFilterChipColors(
                        selectedContainerColor = SoraColors.AccentSubtle,
                        selectedLabelColor     = SoraColors.Accent,
                        containerColor         = SoraColors.Surface,
                        labelColor             = SoraColors.TextSecondary
                    ),
                    elevation = FilterChipDefaults.filterChipElevation()
                )
            }
        }

        Spacer(Modifier.height(12.dp))

        if (totalCount == 0) {
            EmptyFavoritesContent(s.favoritesEmptyTitle, s.favoritesEmptySubtitle)
        } else {
            when (selectedTab) {
                FavoriteTab.All -> AllFavoritesContent(
                    apodList    = apodFavorites,
                    mediaList   = mediaFavorites,
                    launchList  = launchFavorites,
                    articleList = articleFavorites,
                    onRemoveApod    = viewModel::removeApod,
                    onRemoveMedia   = viewModel::removeMedia,
                    onRemoveLaunch  = viewModel::removeLaunch,
                    onRemoveArticle = viewModel::removeArticle,
                    s               = s
                )
                FavoriteTab.Apod -> if (apodFavorites.isEmpty()) {
                    EmptyFavoritesContent(s.favoritesEmptyTitle, s.favoritesEmptySubtitle)
                } else {
                    ApodFavoritesList(apodFavorites, viewModel::removeApod)
                }
                FavoriteTab.Media -> if (mediaFavorites.isEmpty()) {
                    EmptyFavoritesContent(s.favoritesEmptyTitle, s.favoritesEmptySubtitle)
                } else {
                    MediaFavoritesGrid(mediaFavorites, viewModel::removeMedia)
                }
                FavoriteTab.Launches -> if (launchFavorites.isEmpty()) {
                    EmptyFavoritesContent(s.favoritesEmptyTitle, s.favoritesEmptySubtitle)
                } else {
                    LaunchFavoritesList(launchFavorites, viewModel::removeLaunch)
                }
                FavoriteTab.News -> if (articleFavorites.isEmpty()) {
                    EmptyFavoritesContent(s.favoritesEmptyTitle, s.favoritesEmptySubtitle)
                } else {
                    ArticleFavoritesList(articleFavorites, viewModel::removeArticle)
                }
            }
        }
    }
}

@Composable
private fun EmptyFavoritesContent(title: String, subtitle: String) {
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                imageVector        = Icons.Outlined.FavoriteBorder,
                contentDescription = null,
                tint               = SoraColors.TextTertiary,
                modifier           = Modifier.size(52.dp)
            )
            Spacer(Modifier.height(20.dp))
            Text(text = title, style = SoraType.Title)
            Spacer(Modifier.height(8.dp))
            Text(text = subtitle, style = SoraType.Body)
        }
    }
}

@Composable
private fun AllFavoritesContent(
    apodList: List<Apod>,
    mediaList: List<NasaMedia>,
    launchList: List<SpaceLaunch>,
    articleList: List<SpaceArticle>,
    onRemoveApod: (Apod) -> Unit,
    onRemoveMedia: (NasaMedia) -> Unit,
    onRemoveLaunch: (SpaceLaunch) -> Unit,
    onRemoveArticle: (SpaceArticle) -> Unit,
    s: com.yumedev.soraspace.ui.strings.Strings
) {
    LazyColumn(
        contentPadding        = PaddingValues(start = 0.dp, end = 0.dp, bottom = 24.dp),
        verticalArrangement   = Arrangement.spacedBy(8.dp),
        modifier              = Modifier.fillMaxSize()
    ) {
        if (apodList.isNotEmpty()) {
            item { SectionLabel("APOD") }
            items(apodList, key = { it.date }) { apod ->
                ApodFavoriteRow(apod, onRemoveApod)
            }
        }
        if (mediaList.isNotEmpty()) {
            item { SectionLabel("MEDIA") }
            items(mediaList, key = { it.id }) { media ->
                MediaFavoriteRow(media, onRemoveMedia)
            }
        }
        if (launchList.isNotEmpty()) {
            item { SectionLabel(s.favoritesTabLaunches.uppercase()) }
            items(launchList, key = { it.id }) { launch ->
                LaunchFavoriteRow(launch, onRemoveLaunch)
            }
        }
        if (articleList.isNotEmpty()) {
            item { SectionLabel(s.favoritesTabNews.uppercase()) }
            items(articleList, key = { it.id }) { article ->
                ArticleFavoriteRow(article, onRemoveArticle)
            }
        }
    }
}

@Composable
private fun ApodFavoritesList(items: List<Apod>, onRemove: (Apod) -> Unit) {
    LazyColumn(
        contentPadding      = PaddingValues(start = 0.dp, end = 0.dp, bottom = 24.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier            = Modifier.fillMaxSize()
    ) {
        items(items, key = { it.date }) { apod ->
            ApodFavoriteRow(apod, onRemove)
        }
    }
}

@Composable
private fun MediaFavoritesGrid(items: List<NasaMedia>, onRemove: (NasaMedia) -> Unit) {
    LazyVerticalGrid(
        columns               = GridCells.Fixed(2),
        contentPadding        = PaddingValues(start = 16.dp, end = 16.dp, bottom = 24.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement   = Arrangement.spacedBy(8.dp),
        modifier              = Modifier.fillMaxSize()
    ) {
        items(items, key = { it.id }) { media ->
            MediaFavoriteCard(media, onRemove)
        }
    }
}

@Composable
private fun LaunchFavoritesList(items: List<SpaceLaunch>, onRemove: (SpaceLaunch) -> Unit) {
    LazyColumn(
        contentPadding      = PaddingValues(start = 0.dp, end = 0.dp, bottom = 24.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier            = Modifier.fillMaxSize()
    ) {
        items(items, key = { it.id }) { launch ->
            LaunchFavoriteRow(launch, onRemove)
        }
    }
}

@Composable
private fun ArticleFavoritesList(items: List<SpaceArticle>, onRemove: (SpaceArticle) -> Unit) {
    LazyColumn(
        contentPadding      = PaddingValues(start = 0.dp, end = 0.dp, bottom = 24.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier            = Modifier.fillMaxSize()
    ) {
        items(items, key = { it.id }) { article ->
            ArticleFavoriteRow(article, onRemove)
        }
    }
}

// ─── Row items ───────────────────────────────────────────────────────────────

@Composable
private fun SectionLabel(text: String) {
    Text(
        text     = text,
        style    = SoraType.Label,
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
    )
}

@Composable
private fun ApodFavoriteRow(apod: Apod, onRemove: (Apod) -> Unit) {
    Row(
        modifier              = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(SoraColors.Surface)
            .padding(8.dp),
        verticalAlignment     = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        AsyncImage(
            model              = apod.imageUrl,
            contentDescription = null,
            contentScale       = ContentScale.Crop,
            modifier           = Modifier.size(64.dp).clip(RoundedCornerShape(8.dp))
        )
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text     = apod.date,
                style    = SoraType.Label.copy(fontSize = 10.sp),
                color    = SoraColors.TextSecondary
            )
            Spacer(Modifier.height(2.dp))
            Text(
                text     = apod.title,
                style    = SoraType.Caption.copy(fontWeight = FontWeight.SemiBold),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }
        IconButton(onClick = { onRemove(apod) }) {
            Icon(
                imageVector        = Icons.Filled.Favorite,
                contentDescription = "Remove",
                tint               = SoraColors.Accent,
                modifier           = Modifier.size(20.dp)
            )
        }
    }
}

@Composable
private fun MediaFavoriteRow(media: NasaMedia, onRemove: (NasaMedia) -> Unit) {
    Row(
        modifier              = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(SoraColors.Surface)
            .padding(8.dp),
        verticalAlignment     = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        AsyncImage(
            model              = media.thumbnailUrl,
            contentDescription = null,
            contentScale       = ContentScale.Crop,
            modifier           = Modifier.size(64.dp).clip(RoundedCornerShape(8.dp))
        )
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text     = if (media.isVideo) "VIDEO" else "IMAGE",
                style    = SoraType.Label.copy(fontSize = 9.sp),
                color    = SoraColors.Accent
            )
            Spacer(Modifier.height(2.dp))
            Text(
                text     = media.title,
                style    = SoraType.Caption.copy(fontWeight = FontWeight.SemiBold),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }
        IconButton(onClick = { onRemove(media) }) {
            Icon(
                imageVector        = Icons.Filled.Favorite,
                contentDescription = "Remove",
                tint               = SoraColors.Accent,
                modifier           = Modifier.size(20.dp)
            )
        }
    }
}

@Composable
private fun MediaFavoriteCard(media: NasaMedia, onRemove: (NasaMedia) -> Unit) {
    Box(
        modifier = Modifier
            .aspectRatio(1f)
            .clip(RoundedCornerShape(8.dp))
    ) {
        AsyncImage(
            model              = media.thumbnailUrl,
            contentDescription = media.title,
            contentScale       = ContentScale.Crop,
            modifier           = Modifier.fillMaxSize()
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomStart)
                .background(
                    Brush.verticalGradient(listOf(Color.Transparent, Color.Black.copy(alpha = 0.80f)))
                )
                .padding(8.dp)
        ) {
            Text(
                text     = media.title,
                style    = SoraType.Caption.copy(color = SoraColors.TextPrimary, fontWeight = FontWeight.SemiBold),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }
        IconButton(
            onClick  = { onRemove(media) },
            modifier = Modifier.align(Alignment.TopEnd).size(32.dp)
        ) {
            Icon(
                imageVector        = Icons.Filled.Favorite,
                contentDescription = "Remove",
                tint               = SoraColors.Accent,
                modifier           = Modifier.size(16.dp)
            )
        }
    }
}

@Composable
private fun LaunchFavoriteRow(launch: SpaceLaunch, onRemove: (SpaceLaunch) -> Unit) {
    Row(
        modifier              = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(SoraColors.Surface)
            .padding(8.dp),
        verticalAlignment     = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        if (launch.imageUrl != null) {
            AsyncImage(
                model              = launch.imageUrl,
                contentDescription = null,
                contentScale       = ContentScale.Crop,
                modifier           = Modifier.size(64.dp).clip(RoundedCornerShape(8.dp))
            )
        } else {
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(SoraColors.SurfaceHigh)
            )
        }
        Column(modifier = Modifier.weight(1f)) {
            if (launch.provider.isNotEmpty()) {
                Text(
                    text  = launch.provider.uppercase(),
                    style = SoraType.Label.copy(fontSize = 9.sp),
                    color = SoraColors.TextSecondary
                )
                Spacer(Modifier.height(2.dp))
            }
            Text(
                text     = launch.name,
                style    = SoraType.Caption.copy(fontWeight = FontWeight.SemiBold),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }
        IconButton(onClick = { onRemove(launch) }) {
            Icon(
                imageVector        = Icons.Filled.Favorite,
                contentDescription = "Remove",
                tint               = SoraColors.Accent,
                modifier           = Modifier.size(20.dp)
            )
        }
    }
}

@Composable
private fun ArticleFavoriteRow(article: SpaceArticle, onRemove: (SpaceArticle) -> Unit) {
    Row(
        modifier              = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(SoraColors.Surface)
            .padding(8.dp),
        verticalAlignment     = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        AsyncImage(
            model              = article.imageUrl,
            contentDescription = null,
            contentScale       = ContentScale.Crop,
            modifier           = Modifier.size(64.dp).clip(RoundedCornerShape(8.dp))
        )
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text  = article.newsSite.uppercase(),
                style = SoraType.Label.copy(fontSize = 9.sp),
                color = SoraColors.TextSecondary
            )
            Spacer(Modifier.height(2.dp))
            Text(
                text     = article.title,
                style    = SoraType.Caption.copy(fontWeight = FontWeight.SemiBold),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }
        IconButton(onClick = { onRemove(article) }) {
            Icon(
                imageVector        = Icons.Filled.Favorite,
                contentDescription = "Remove",
                tint               = SoraColors.Accent,
                modifier           = Modifier.size(20.dp)
            )
        }
    }
}