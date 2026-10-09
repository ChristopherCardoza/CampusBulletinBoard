# Campus Bulletin Board

Campus Bulletin Board is a shared place for students to post campus news and talk about a specific post. You write an announcement, preview it, and add it to the board. Each post keeps its own message thread.

Announcements and messages stay in memory for the current session. Closing the app clears the board.

## Using the app

The bottom bar has three tabs.

**Create.** Enter a title, a description, and an image. Paste an `http` or `https` link, or pick a photo from the row loaded from Picsum. Submit opens a preview. Confirm adds the post to the board and switches to Announcements. Edit returns to the form. The poster name is "Student".

**Announcements.** Browse posts on the board. Search by title, and sort A–Z or Z–A. Tap a row to expand it. Open Chat starts that post's thread. Delete removes the post and its messages.

**About.** A short description of the app, plus two sample posts that are not on the board.

In a chat, choose Poster or Visitor, type a message, and send. Poster messages use the announcement's poster name. Visitor messages are labeled "Visitor".

## Running it

This is a Kotlin Multiplatform project with an Android app and a web app. Shared UI lives in `shared`.

- Android: `./gradlew :androidApp:assembleDebug`
- Web (Wasm): `./gradlew :webApp:wasmJsBrowserDevelopmentRun`
- Web (JavaScript): `./gradlew :webApp:jsBrowserDevelopmentRun`

On Windows, use `gradlew` instead of `./gradlew`. You can also launch the run configurations from the IDE toolbar.