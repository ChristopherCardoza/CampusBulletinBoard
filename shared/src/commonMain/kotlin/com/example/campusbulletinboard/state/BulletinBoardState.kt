package com.example.campusbulletinboard.state

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf
import com.example.campusbulletinboard.model.Announcement
import com.example.campusbulletinboard.model.Message
import com.example.campusbulletinboard.model.Sender

class BulletinBoardState {
    private val messagesState = mutableStateListOf<Message>()
    private var nextId = 1L

    val announcements: List<Announcement>
        field = mutableStateListOf<Announcement>()

    fun announcement(id: String): Announcement? {
        return announcements.find { it.id == id }
    }

    fun addAnnouncement(
        title: String,
        description: String,
        imageUrl: String,
        posterName: String,
    ): Announcement {
        val announcement = Announcement(
            id = newId(),
            title = title,
            description = description,
            imageUrl = imageUrl,
            posterName = posterName,
        )
        announcements.add(announcement)
        return announcement
    }

    fun deleteAnnouncement(id: String) {
        announcements.removeAll { it.id == id }
        messagesState.removeAll { it.announcementId == id }
    }

    fun moveAnnouncement(fromIndex: Int, toIndex: Int) {
        if (fromIndex == toIndex) return
        val announcement = announcements.removeAt(fromIndex)
        val targetIndex = if (toIndex > fromIndex) toIndex - 1 else toIndex
        announcements.add(targetIndex, announcement)
    }

    fun messagesFor(announcementId: String): List<Message> {
        return messagesState.filter { it.announcementId == announcementId }
    }

    fun addMessage(
        announcementId: String,
        sender: Sender,
        senderName: String,
        text: String,
    ): Message? {
        if (announcement(announcementId) == null) return null
        val message = Message(
            id = newId(),
            announcementId = announcementId,
            sender = sender,
            senderName = senderName,
            text = text,
        )
        messagesState.add(message)
        return message
    }

    private fun newId(): String = (nextId++).toString()
}

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