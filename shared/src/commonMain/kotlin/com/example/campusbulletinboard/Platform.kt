package com.example.campusbulletinboard

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform