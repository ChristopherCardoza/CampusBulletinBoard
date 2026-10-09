package com.example.campusbulletinboard.model

/**
 * Who wrote a chat message.
 *
 * [Poster] is the person who published the announcement.
 * [Visitor] is anyone else replying on that announcement's thread.
 */
enum class Sender {
    Poster,
    Visitor,
}