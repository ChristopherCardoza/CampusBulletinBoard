package com.example.campusbulletinboard.network

import io.ktor.client.HttpClient

expect fun createHttpClient(): HttpClient

/**
 * Shared Ktor [io.ktor.client.HttpClient] for common code.
 *
 * [createHttpClient] is implemented per platform. [AppHttpClient.client] builds
 * that client once and is used to load Picsum photos and remote images.
 */
object AppHttpClient {
    val client: HttpClient by lazy { createHttpClient() }
}