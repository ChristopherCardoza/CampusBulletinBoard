package com.example.campusbulletinboard.model

/**
 * Unsaved announcement fields collected on the create screen.
 *
 * [posterName] defaults to "Student" because the create form does not ask for it.
 * Confirming the preview turns this draft into an [Announcement].
 */
data class AnnouncementDraft(
    val title: String,
    val description: String,
    val imageUrl: String,
    val posterName: String = "Student",
)