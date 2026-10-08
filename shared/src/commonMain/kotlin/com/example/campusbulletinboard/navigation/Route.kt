package com.example.campusbulletinboard.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

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