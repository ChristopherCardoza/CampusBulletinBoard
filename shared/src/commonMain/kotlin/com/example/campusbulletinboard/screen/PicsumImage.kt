package com.example.campusbulletinboard.screen

import com.example.campusbulletinboard.network.AppHttpClient
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

/**
 * A photo from the Picsum list API, plus the request that loads a page of them.
 *
 * [PicsumImage.thumbnailUrl] is the small picker image.
 * [PicsumImage.imageUrl] is the larger URL stored on an announcement.
 * [loadPicsumImages] fetches page 3 (12 photos) through [com.example.campusbulletinboard.network.AppHttpClient].
 */
@Serializable
data class PicsumImage(
    val id: String,
    val author: String,
) {
    val thumbnailUrl: String get() = "https://picsum.photos/seed/campus$id/200/200"
    val imageUrl: String get() = "https://picsum.photos/seed/campus$id/1200/800"
}

/**
 * JSON parser for the Picsum list response.
 *
 * Unknown fields are ignored so decoding only needs [PicsumImage.id] and [PicsumImage.author].
 */
private val picsumJson = Json { ignoreUnknownKeys = true }

suspend fun loadPicsumImages(): List<PicsumImage> {
    val body = AppHttpClient.client
        .get("https://picsum.photos/v2/list?page=3&limit=12")
        .bodyAsText()
    return picsumJson.decodeFromString(body)
}