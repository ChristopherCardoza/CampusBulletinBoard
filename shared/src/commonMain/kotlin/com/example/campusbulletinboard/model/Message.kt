package com.example.campusbulletinboard.model

data class Message(
    val id: String,
    val announcementId: String,
    val senderName: String,
    val text: String,
)