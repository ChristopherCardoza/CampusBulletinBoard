package com.example.campusbulletinboard.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

/**
 * Navigation destinations for the app.
 *
 * [Create], [Announcements], and [About] are top-level tabs.
 * [Preview] carries a draft into the confirm screen.
 * [Chat] opens the message thread for one announcement id.
 *
 * Every subtype is [kotlinx.serialization.Serializable] so the back stack can be restored.
 */
@Serializable
sealed class Route : NavKey {
    @Serializable
    data object Create : Route()

    @Serializable
    data class Preview(
        val title: String,
        val description: String,
        val imageUrl: String,
        val posterName: String,
    ) : Route()

    @Serializable
    data object Announcements : Route()

    @Serializable
    data object About : Route()

    @Serializable
    data class Chat(
        val announcementId: String,
    ) : Route()
}