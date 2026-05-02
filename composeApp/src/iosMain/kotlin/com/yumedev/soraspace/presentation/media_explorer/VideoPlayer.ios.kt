package com.yumedev.soraspace.presentation.media_explorer

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.interop.UIKitView
import kotlinx.cinterop.ExperimentalForeignApi
import platform.AVFoundation.AVPlayer
import platform.AVFoundation.AVPlayerLayer
import platform.AVFoundation.play
import platform.Foundation.NSURL
import platform.QuartzCore.CATransaction
import platform.QuartzCore.kCATransactionDisableActions
import platform.UIKit.UIView

@OptIn(ExperimentalForeignApi::class)
@Composable
actual fun VideoPlayer(url: String, modifier: Modifier) {
    val player = remember(url) {
        NSURL.URLWithString(url)?.let { AVPlayer(uRL = it) }
    }

    DisposableEffect(player) {
        player?.play()
        onDispose { player?.pause() }
    }

    if (player == null) return

    UIKitView(
        modifier = modifier,
        factory = {
            val view = UIView()
            val playerLayer = AVPlayerLayer.playerLayerWithPlayer(player)
            view.layer.addSublayer(playerLayer)
            view
        },
        onResize = { view, rect ->
            val playerLayer = view.layer.sublayers?.firstOrNull() as? AVPlayerLayer
            CATransaction.begin()
            CATransaction.setValue(true, kCATransactionDisableActions)
            playerLayer?.frame = rect
            CATransaction.commit()
        }
    )
}