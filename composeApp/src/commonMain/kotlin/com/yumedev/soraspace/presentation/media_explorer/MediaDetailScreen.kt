package com.yumedev.soraspace.presentation.media_explorer

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.yumedev.soraspace.domain.model.NasaMedia
import com.yumedev.soraspace.ui.theme.SoraColors
import com.yumedev.soraspace.ui.theme.SoraType

@Composable
fun MediaDetailScreen(
    media: NasaMedia,
    viewModel: MediaDetailViewModel,
    onBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val uriHandler = LocalUriHandler.current

    //val imageUrl = when (val s = uiState) {
    //    is MediaDetailUiState.Success -> s.assetUrl
    //    else                         -> media.thumbnailUrl
    //}

    val imageUrl = media.thumbnailUrl

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(SoraColors.Background)
            .verticalScroll(rememberScrollState())
    ) {
        // Imagen + overlays en una Box fija
        Box(modifier = Modifier.fillMaxWidth().height(420.dp)) {
            AsyncImage(
                model              = imageUrl,
                contentDescription = media.title,
                contentScale       = ContentScale.Crop,
                modifier           = Modifier.fillMaxSize()
            )

            // Gradiente inferior para transición suave al fondo negro
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .align(Alignment.BottomStart)
                    .background(
                        Brush.verticalGradient(
                            listOf(Color.Transparent, SoraColors.Background)
                        )
                    )
            )

            // Botón back
            FilledIconButton(
                onClick  = onBack,
                modifier = Modifier.statusBarsPadding().padding(16.dp).size(36.dp),
                colors   = IconButtonDefaults.filledIconButtonColors(
                    containerColor = Color.Black.copy(alpha = 0.55f),
                    contentColor   = SoraColors.TextPrimary
                )
            ) {
                Icon(
                    Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    modifier           = Modifier.size(18.dp)
                )
            }
        }

        // Contenido
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(SoraColors.Background)
                .padding(horizontal = 20.dp, vertical = 8.dp)
        ) {
            // Chips de metadata
            if (!media.center.isNullOrBlank() || !media.dateCreated.isNullOrBlank() || media.isVideo) {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    if (!media.center.isNullOrBlank()) MetaChip(media.center)
                    if (!media.dateCreated.isNullOrBlank()) MetaChip(media.dateCreated)
                    if (media.isVideo) MetaChip("VIDEO")
                }
                Spacer(Modifier.height(12.dp))
            }

            Text(
                text  = media.title,
                style = SoraType.Title.copy(fontSize = 22.sp, lineHeight = 30.sp)
            )

            if (!media.description.isNullOrBlank()) {
                Spacer(Modifier.height(12.dp))
                Text(text = media.description, style = SoraType.Body)
            }

            if (media.isVideo && uiState is MediaDetailUiState.Success) {
                Spacer(Modifier.height(24.dp))
                Button(
                    onClick  = { uriHandler.openUri((uiState as MediaDetailUiState.Success).assetUrl) },
                    colors   = ButtonDefaults.buttonColors(containerColor = SoraColors.Accent),
                    shape    = RoundedCornerShape(10.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(Icons.Filled.PlayCircle, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(Modifier.size(8.dp))
                    Text("Play Video", color = Color.White, fontWeight = FontWeight.SemiBold)
                }
            }

            if (uiState is MediaDetailUiState.Error) {
                Spacer(Modifier.height(16.dp))
                TextButton(onClick = viewModel::retry) {
                    Text("Retry loading asset", color = SoraColors.Accent)
                }
            }

            Spacer(Modifier.height(32.dp))
        }
    }
}

@Composable
private fun MetaChip(label: String) {
    Text(
        text     = label,
        style    = SoraType.Caption.copy(color = SoraColors.Accent, fontWeight = FontWeight.SemiBold),
        modifier = Modifier
            .background(SoraColors.AccentSubtle, RoundedCornerShape(4.dp))
            .padding(horizontal = 8.dp, vertical = 4.dp)
    )
}