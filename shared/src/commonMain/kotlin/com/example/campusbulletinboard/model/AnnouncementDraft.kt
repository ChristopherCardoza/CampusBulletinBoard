package com.example.campusbulletinboard.model

data class AnnouncementDraft(
    val title: String,
    val description: String,
    val imageUrl: String,
    val posterName: String = "Student",
)