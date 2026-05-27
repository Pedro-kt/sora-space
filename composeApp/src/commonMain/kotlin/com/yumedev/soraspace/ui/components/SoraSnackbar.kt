package com.yumedev.soraspace.ui.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarData
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.yumedev.soraspace.ui.theme.SoraColors

/**
 * Reusable Snackbar Host with custom styling for Sora Space theme.
 *
 * Automatically detects message type based on keywords:
 * - "success", "added", "saved" -> Success icon (checkmark)
 * - "error", "failed", "couldn't" -> Error icon (X)
 * - Default -> Info icon (i)
 *
 * Usage:
 * ```
 * val snackbarHostState = remember { SnackbarHostState() }
 *
 * Scaffold(
 *     snackbarHost = { SoraSnackbarHost(snackbarHostState) }
 * ) { ... }
 *
 * LaunchedEffect(favoriteAdded) {
 *     if (favoriteAdded) {
 *         snackbarHostState.showSnackbar("Added to favorites")
 *     }
 * }
 * ```
 */
@Composable
fun SoraSnackbarHost(
    hostState: SnackbarHostState,
    modifier: Modifier = Modifier
) {
    SnackbarHost(
        hostState = hostState,
        modifier = modifier,
        snackbar = { snackbarData ->
            SoraSnackbar(snackbarData)
        }
    )
}

@Composable
private fun SoraSnackbar(snackbarData: SnackbarData) {
    val message = snackbarData.visuals.message
    val icon = when {
        message.contains("success", ignoreCase = true) ||
        message.contains("added", ignoreCase = true) ||
        message.contains("saved", ignoreCase = true) -> Icons.Default.CheckCircle

        message.contains("error", ignoreCase = true) ||
        message.contains("failed", ignoreCase = true) ||
        message.contains("couldn't", ignoreCase = true) -> Icons.Default.Error

        message.contains("removed", ignoreCase = true) ||
        message.contains("deleted", ignoreCase = true) -> Icons.Default.Info

        else -> Icons.Default.Info
    }

    val iconTint = when {
        message.contains("success", ignoreCase = true) ||
        message.contains("added", ignoreCase = true) ||
        message.contains("saved", ignoreCase = true) -> SoraColors.Status.Success

        message.contains("error", ignoreCase = true) ||
        message.contains("failed", ignoreCase = true) ||
        message.contains("couldn't", ignoreCase = true) -> SoraColors.Status.Error

        else -> SoraColors.Accent
    }

    Snackbar(
        modifier = Modifier.padding(16.dp),
        containerColor = SoraColors.Surface,
        contentColor = SoraColors.TextPrimary,
        actionContentColor = SoraColors.Accent,
        shape = RoundedCornerShape(12.dp),
        action = snackbarData.visuals.actionLabel?.let {
            {
                TextButton(
                    onClick = { snackbarData.performAction() }
                ) {
                    Text(
                        text = it,
                        color = SoraColors.Accent
                    )
                }
            }
        }
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = iconTint,
                modifier = Modifier.size(20.dp)
            )

            Spacer(Modifier.width(12.dp))

            Text(
                text = message,
                color = SoraColors.TextPrimary
            )
        }
    }
}

/**
 * Helper function to show snackbar with common messages
 */
suspend fun SnackbarHostState.showSuccess(message: String) {
    showSnackbar(message)
}

suspend fun SnackbarHostState.showError(message: String) {
    showSnackbar(message)
}
