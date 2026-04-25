package com.yumedev.soraspace.presentation.media_explorer

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.ImageSearch
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
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
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.yumedev.soraspace.domain.model.NasaMedia
import com.yumedev.soraspace.ui.theme.SoraColors
import com.yumedev.soraspace.ui.theme.SoraType

@Composable
fun MarsScreen(
    viewModel: MediaExplorerViewModel,
    onBack: () -> Unit,
    onNavigateToDetail: (NasaMedia) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val query by viewModel.query.collectAsState()
    val keyboard = LocalSoftwareKeyboardController.current

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
                    imageVector        = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    modifier           = Modifier.size(18.dp)
                )
            }
            Text(text = "MEDIA EXPLORER", style = SoraType.Label)
        }

        // Search bar
        TextField(
            value         = query,
            onValueChange = viewModel::onQueryChange,
            placeholder   = { Text("Search images & videos...", style = SoraType.Body) },
            singleLine    = true,
            leadingIcon   = {
                Icon(Icons.Filled.Search, contentDescription = null, tint = SoraColors.Accent)
            },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
            keyboardActions = KeyboardActions(onSearch = {
                keyboard?.hide()
                viewModel.search()
            }),
            colors = TextFieldDefaults.colors(
                focusedContainerColor   = SoraColors.Surface,
                unfocusedContainerColor = SoraColors.Surface,
                focusedTextColor        = SoraColors.TextPrimary,
                unfocusedTextColor      = SoraColors.TextPrimary,
                cursorColor             = SoraColors.Accent,
                focusedIndicatorColor   = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                focusedLeadingIconColor = SoraColors.Accent
            ),
            shape    = RoundedCornerShape(12.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        )

        Spacer(Modifier.height(16.dp))

        when (val state = uiState) {
            is MediaExplorerUiState.Idle    -> IdleContent()
            is MediaExplorerUiState.Loading -> LoadingContent()
            is MediaExplorerUiState.Error   -> ErrorContent(state.message) { viewModel.search() }
            is MediaExplorerUiState.Success -> MediaGrid(state.items, onNavigateToDetail)
        }
    }
}

// ─── Componentes ─────────────────────────────────────────────────────────────

@Composable
private fun MediaGrid(items: List<NasaMedia>, onItemClick: (NasaMedia) -> Unit) {
    LazyVerticalGrid(
        columns               = GridCells.Fixed(2),
        contentPadding        = PaddingValues(horizontal = 16.dp, vertical = 4.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement   = Arrangement.spacedBy(8.dp),
        modifier              = Modifier.fillMaxSize()
    ) {
        items(items, key = { it.id }) { media ->
            MediaCard(media, onClick = { onItemClick(media) })
        }
    }
}

@Composable
private fun MediaCard(media: NasaMedia, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .aspectRatio(1f)
            .clip(RoundedCornerShape(8.dp))
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication        = null,
                onClick           = onClick
            )
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
                style    = SoraType.Caption.copy(
                    color      = SoraColors.TextPrimary,
                    fontWeight = FontWeight.SemiBold
                ),
                maxLines = 2
            )
        }

        if (media.isVideo) {
            Box(
                modifier         = Modifier
                    .align(Alignment.TopEnd)
                    .padding(6.dp)
                    .background(Color.Black.copy(alpha = 0.55f), RoundedCornerShape(4.dp))
                    .padding(4.dp)
            ) {
                Icon(
                    imageVector        = Icons.Filled.PlayCircle,
                    contentDescription = "Video",
                    tint               = SoraColors.Accent,
                    modifier           = Modifier.size(14.dp)
                )
            }
        }
    }
}

@Composable
private fun IdleContent() {
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                imageVector        = Icons.Outlined.ImageSearch,
                contentDescription = null,
                tint               = SoraColors.TextTertiary,
                modifier           = Modifier.size(52.dp)
            )
            Spacer(Modifier.height(16.dp))
            Text(text = "Search the NASA library", style = SoraType.Body)
        }
    }
}

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
            imageVector        = Icons.Outlined.ImageSearch,
            contentDescription = null,
            tint               = SoraColors.TextTertiary,
            modifier           = Modifier.size(52.dp)
        )
        Spacer(Modifier.height(16.dp))
        Text(text = message, style = SoraType.Body)
        Spacer(Modifier.height(16.dp))
        TextButton(onClick = onRetry) {
            Text("Retry", color = SoraColors.Accent)
        }
    }
}