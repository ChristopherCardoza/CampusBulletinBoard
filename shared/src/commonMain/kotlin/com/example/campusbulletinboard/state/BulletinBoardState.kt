package com.example.campusbulletinboard.state

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.runtime.remember

class BulletinBoardState

private val LocalBulletinBoardState = staticCompositionLocalOf<BulletinBoardState> {
    error("BulletinBoardState is not provided")
}

@Composable
fun ProvideBulletinBoardState(
    content: @Composable () -> Unit,
) {
    val state = remember { BulletinBoardState() }
    CompositionLocalProvider(LocalBulletinBoardState provides state) {
        content()
    }
}

@Composable
fun currentBulletinBoardState(): BulletinBoardState = LocalBulletinBoardState.current