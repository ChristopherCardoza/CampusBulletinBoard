package com.example.campusbulletinboard.network

import io.ktor.client.HttpClient

expect fun createHttpClient(): HttpClient

object AppHttpClient {
    val client: HttpClient by lazy { createHttpClient() }
}