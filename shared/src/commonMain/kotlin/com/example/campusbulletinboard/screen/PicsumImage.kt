package com.example.campusbulletinboard.screen

import com.example.campusbulletinboard.network.AppHttpClient
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

@Serializable
data class PicsumImage(
    val id: String,
    val author: String,
) {
    val thumbnailUrl: String get() = "https://picsum.photos/seed/campus$id/200/200"
    val imageUrl: String get() = "https://picsum.photos/seed/campus$id/1200/800"
}

private val picsumJson = Json { ignoreUnknownKeys = true }

suspend fun loadPicsumImages(): List<PicsumImage> {
    val body = AppHttpClient.client
        .get("https://picsum.photos/v2/list?page=3&limit=12")
        .bodyAsText()
    return picsumJson.decodeFromString(body)
}