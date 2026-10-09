package com.example.campusbulletinboard

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.campusbulletinboard.navigation.AppNavHost
import com.example.campusbulletinboard.state.ProvideBulletinBoardState
import coil3.ImageLoader
import coil3.compose.setSingletonImageLoaderFactory
import coil3.network.ktor3.KtorNetworkFetcherFactory
import coil3.request.crossfade
import com.example.campusbulletinboard.network.AppHttpClient
import com.example.campusbulletinboard.theme.CampusBulletinBoardTheme


@Composable
@Preview
fun App() {
    setSingletonImageLoaderFactory { context ->
        ImageLoader.Builder(context)
            .components {
                add(KtorNetworkFetcherFactory(AppHttpClient.client))
            }
            .crossfade(true)
            .build()
    }
    CampusBulletinBoardTheme {
        ProvideBulletinBoardState {
            AppNavHost()
        }
    }
}