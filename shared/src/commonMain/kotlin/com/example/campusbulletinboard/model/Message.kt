package com.example.campusbulletinboard.model

/**
 * One chat message attached to a single announcement.
 *
 * [announcementId] ties the message to its thread. [sender] decides which side
 * of the chat the bubble is drawn on, and [senderName] is the label shown above it.
 */
data class Message(
    val id: String,
    val announcementId: String,
    val sender: Sender,
    val senderName: String,
    val text: String,
)