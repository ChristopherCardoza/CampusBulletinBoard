package com.example.campusbulletinboard

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.campusbulletinboard.navigation.AppNavHost
import com.example.campusbulletinboard.state.ProvideBulletinBoardState

@Composable
@Preview
fun App() {
    MaterialTheme {
        ProvideBulletinBoardState {
            AppNavHost()
        }
    }
}