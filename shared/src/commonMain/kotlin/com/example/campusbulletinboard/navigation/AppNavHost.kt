package com.example.campusbulletinboard.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import androidx.savedstate.serialization.SavedStateConfiguration
import com.example.campusbulletinboard.screen.CreateAnnouncementScreen
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import kotlinx.serialization.modules.subclass


private val routeSavedStateConfiguration = SavedStateConfiguration {
    serializersModule = SerializersModule {
        polymorphic(NavKey::class) {
            subclass(Route.Create::class)
            subclass(Route.Preview::class)
            subclass(Route.Announcements::class)
            subclass(Route.About::class)
            subclass(Route.Chat::class)
        }
    }
}

@Composable
fun AppNavHost(modifier: Modifier = Modifier) {
    val backStack = rememberNavBackStack(routeSavedStateConfiguration, Route.Create)
    val currentRoute = backStack.lastOrNull() as? Route ?: Route.Create

    fun showTopLevel(route: Route) {
        if (backStack.size == 1 && backStack.lastOrNull() == route) return
        while (backStack.size > 1) {
            backStack.removeAt(backStack.lastIndex)
        }
        if (backStack.lastOrNull() != route) {
            backStack[0] = route
        }
    }

    fun navigate(route: Route) {
        when (route) {
            Route.Create, Route.Announcements, Route.About -> showTopLevel(route)
            is Route.Preview, is Route.Chat -> {
                if (backStack.lastOrNull() != route) {
                    backStack.add(route)
                }
            }
        }
    }

    AppScaffold(
        currentRoute = currentRoute,
        onNavigate = ::navigate,
        modifier = modifier,
    ) { innerPadding ->
        NavDisplay(
            backStack = backStack,
            modifier = Modifier.padding(innerPadding).fillMaxSize(),
            onBack = {
                if (backStack.size > 1) {
                    backStack.removeAt(backStack.lastIndex)
                }
            },
            entryProvider = entryProvider {
                entry<Route.Create> {
                    CreateAnnouncementScreen()
                }
                entry<Route.Preview> { preview ->
                    DestinationPlaceholder("${preview.title}\nPosted by ${preview.posterName}")
                }
                entry<Route.Announcements> {
                    DestinationPlaceholder("Announcements")
                }
                entry<Route.About> {
                    DestinationPlaceholder("About")
                }
                entry<Route.Chat> { chat ->
                    DestinationPlaceholder("Chat ${chat.announcementId}")
                }
            },
        )
    }
}

@Composable
private fun DestinationPlaceholder(label: String) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.headlineSmall,
        )
    }
}