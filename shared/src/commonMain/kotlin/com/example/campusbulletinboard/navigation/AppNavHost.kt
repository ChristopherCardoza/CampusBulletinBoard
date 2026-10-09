package com.example.campusbulletinboard.navigation

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
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
import androidx.navigation3.runtime.metadata
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import androidx.savedstate.serialization.SavedStateConfiguration
import com.example.campusbulletinboard.screen.AboutScreen
import com.example.campusbulletinboard.screen.AnnouncementListScreen
import com.example.campusbulletinboard.screen.AnnouncementPreviewScreen
import com.example.campusbulletinboard.screen.ChatScreen
import com.example.campusbulletinboard.screen.CreateAnnouncementScreen
import com.example.campusbulletinboard.state.currentBulletinBoardState
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

/**
 * Owns the back stack and shows the screen for the current [Route].
 *
 * Tapping a bottom-bar destination replaces the single top-level entry.
 * Preview and Chat are pushed on top of that entry and pop on back.
 * Confirming a preview saves the announcement and opens the Announcements tab.
 * Preview slides in vertically; Chat slides in from the side.
 */
@Composable
fun AppNavHost(modifier: Modifier = Modifier) {
    val backStack = rememberNavBackStack(routeSavedStateConfiguration, Route.Create)
    val board = currentBulletinBoardState()
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
                    CreateAnnouncementScreen(
                        onSubmit = { draft ->
                            navigate(
                                Route.Preview(
                                    title = draft.title,
                                    description = draft.description,
                                    imageUrl = draft.imageUrl,
                                    posterName = draft.posterName,
                                ),
                            )
                        },
                    )
                }
                entry<Route.Preview>(
                    metadata = metadata {
                        put(NavDisplay.TransitionKey) {
                            (
                                    slideInVertically(
                                        initialOffsetY = { it / 4 },
                                        animationSpec = tween(420),
                                    ) + fadeIn(tween(420))
                                    ) togetherWith fadeOut(tween(220))
                        }
                        put(NavDisplay.PopTransitionKey) {
                            fadeIn(tween(220)) togetherWith (
                                    slideOutVertically(
                                        targetOffsetY = { it / 4 },
                                        animationSpec = tween(420),
                                    ) + fadeOut(tween(420))
                                    )
                        }
                        put(NavDisplay.PredictivePopTransitionKey) {
                            fadeIn(tween(220)) togetherWith (
                                    slideOutVertically(
                                        targetOffsetY = { it / 4 },
                                        animationSpec = tween(420),
                                    ) + fadeOut(tween(420))
                                    )
                        }
                    },
                ) { preview ->
                    AnnouncementPreviewScreen(
                        title = preview.title,
                        description = preview.description,
                        imageUrl = preview.imageUrl,
                        onConfirm = {
                            board.addAnnouncement(
                                title = preview.title,
                                description = preview.description,
                                imageUrl = preview.imageUrl,
                                posterName = preview.posterName,
                            )
                            navigate(Route.Announcements)
                        },
                        onEdit = {
                            if (backStack.size > 1) {
                                backStack.removeAt(backStack.lastIndex)
                            }
                        },
                    )
                }
                entry<Route.Announcements> {
                    AnnouncementListScreen(
                        onOpenChat = { announcementId ->
                            navigate(Route.Chat(announcementId))
                        },
                    )
                }
                entry<Route.About> {
                    AboutScreen()
                }
                entry<Route.Chat>(
                    metadata = metadata {
                        put(NavDisplay.TransitionKey) {
                            (
                                    slideInHorizontally(
                                        initialOffsetX = { it },
                                        animationSpec = tween(420),
                                    ) + fadeIn(tween(420))
                                    ) togetherWith fadeOut(tween(220))
                        }
                        put(NavDisplay.PopTransitionKey) {
                            fadeIn(tween(220)) togetherWith (
                                    slideOutHorizontally(
                                        targetOffsetX = { it },
                                        animationSpec = tween(420),
                                    ) + fadeOut(tween(420))
                                    )
                        }
                        put(NavDisplay.PredictivePopTransitionKey) {
                            fadeIn(tween(220)) togetherWith (
                                    slideOutHorizontally(
                                        targetOffsetX = { it },
                                        animationSpec = tween(420),
                                    ) + fadeOut(tween(420))
                                    )
                        }
                    },
                ) { chat ->
                    ChatScreen(announcementId = chat.announcementId)
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