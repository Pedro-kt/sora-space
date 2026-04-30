package com.yumedev.soraspace.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.WifiOff
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.yumedev.soraspace.ui.strings.LocalStrings
import com.yumedev.soraspace.ui.theme.SoraColors
import com.yumedev.soraspace.ui.theme.SoraType

@Composable
fun SoraErrorCard(
    title: String,
    message: String? = null,
    onRetry: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    val s = LocalStrings.current

    Column(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(SoraColors.SurfaceHigh)
            .padding(horizontal = 24.dp, vertical = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Box(
            modifier = Modifier
                .size(52.dp)
                .clip(CircleShape)
                .background(SoraColors.Accent.copy(alpha = 0.10f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector        = Icons.Rounded.WifiOff,
                contentDescription = null,
                tint               = SoraColors.Accent,
                modifier           = Modifier.size(22.dp)
            )
        }

        Text(
            text  = title,
            style = SoraType.Title.copy(textAlign = TextAlign.Center)
        )

        if (message != null) {
            Text(
                text  = message,
                style = SoraType.Body.copy(textAlign = TextAlign.Center)
            )
        }

        if (onRetry != null) {
            Spacer(Modifier.height(6.dp))
            OutlinedButton(
                onClick        = onRetry,
                shape          = RoundedCornerShape(8.dp),
                border         = BorderStroke(1.dp, SoraColors.Accent.copy(alpha = 0.45f)),
                colors         = ButtonDefaults.outlinedButtonColors(contentColor = SoraColors.Accent),
                contentPadding = PaddingValues(horizontal = 36.dp, vertical = 10.dp)
            ) {
                Text(s.retry.uppercase(), style = SoraType.Label)
            }
        }
    }
}

// ─── Previews ─────────────────────────────────────────────────────────────────

@Preview(showBackground = true)
@Composable
private fun SoraErrorCardWithRetryPreview() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(SoraColors.Background)
            .padding(16.dp)
    ) {
        SoraErrorCard(
            title    = "Signal lost",
            message  = "Couldn't connect to the network. Check your connection and try again.",
            onRetry  = {},
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Preview
@Composable
private fun SoraErrorCardNoRetryPreview() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(SoraColors.Background)
            .padding(16.dp)
    ) {
        SoraErrorCard(
            title    = "Signal lost",
            message  = "Couldn't load the latest news.",
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Preview
@Composable
private fun SoraErrorCardFullScreenPreview() {
    Box(
        modifier           = Modifier
            .fillMaxSize()
            .background(SoraColors.Background)
            .padding(24.dp),
        contentAlignment   = Alignment.Center
    ) {
        SoraErrorCard(
            title    = "Signal lost",
            message  = "Couldn't load content. Check your connection and try again.",
            onRetry  = {},
            modifier = Modifier.fillMaxWidth()
        )
    }
}