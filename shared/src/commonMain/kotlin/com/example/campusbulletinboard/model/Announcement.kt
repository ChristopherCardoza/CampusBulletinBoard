package com.example.campusbulletinboard.model

/**
 * A published campus announcement shown on the board.
 *
 * Created from an [AnnouncementDraft] when the user confirms a preview.
 * [id] is assigned by [com.example.campusbulletinboard.state.BulletinBoardState].
 */
data class Announcement(
    val id: String,
    val title: String,
    val description: String,
    val imageUrl: String,
    val posterName: String,
)