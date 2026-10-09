package com.example.campusbulletinboard.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.campusbulletinboard.model.Message
import com.example.campusbulletinboard.model.Sender
import com.example.campusbulletinboard.state.currentBulletinBoardState

@Composable
fun ChatScreen(
    announcementId: String,
    modifier: Modifier = Modifier,
) {
    val board = currentBulletinBoardState()
    val announcement = board.announcement(announcementId)
    val messages = board.messagesFor(announcementId)

    if (announcement == null) {
        MissingAnnouncement(modifier)
    } else {
        Column(modifier = modifier.fillMaxSize()) {
            Text(
                text = announcement.title,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                style = MaterialTheme.typography.titleMedium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            HorizontalDivider()
            if (messages.isEmpty()) {
                EmptyChat(Modifier.weight(1f))
            } else {
                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    items(
                        items = messages,
                        key = { it.id },
                    ) { message ->
                        MessageBubble(message)
                    }
                }
            }
            ChatInputBar(
                onSend = { text ->
                    board.addMessage(
                        announcementId = announcementId,
                        sender = Sender.Visitor,
                        senderName = "Visitor",
                        text = text,
                    )
                },
            )
                }
            }
        }

@Composable
private fun MessageBubble(message: Message) {
    val isPoster = message.sender == Sender.Poster
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = if (isPoster) Alignment.End else Alignment.Start,
    ) {
        Text(
            text = message.senderName,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Surface(
            modifier = Modifier
                .padding(top = 4.dp)
                .widthIn(max = 280.dp),
            shape = RoundedCornerShape(
                topStart = 16.dp,
                topEnd = 16.dp,
                bottomStart = if (isPoster) 16.dp else 4.dp,
                bottomEnd = if (isPoster) 4.dp else 16.dp,
            ),
            color = if (isPoster) {
                MaterialTheme.colorScheme.primaryContainer
            } else {
                MaterialTheme.colorScheme.surfaceVariant
            },
        ) {
            Text(
                text = message.text,
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                style = MaterialTheme.typography.bodyLarge,
                color = if (isPoster) {
                    MaterialTheme.colorScheme.onPrimaryContainer
                } else {
                    MaterialTheme.colorScheme.onSurfaceVariant
                },
            )
        }
    }
}

@Composable
private fun EmptyChat(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = "No messages yet",
            style = MaterialTheme.typography.titleMedium,
            textAlign = TextAlign.Center,
        )
    }
}

@Composable
private fun MissingAnnouncement(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = "This announcement is no longer available.",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
        )
    }
}

@Composable
private fun ChatInputBar(
    onSend: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    var text by rememberSaveable { mutableStateOf("") }

    fun send() {
        val trimmed = text.trim()
        if (trimmed.isEmpty()) return
        onSend(trimmed)
        text = ""
    }

    HorizontalDivider()
    Row(
        modifier = modifier
            .fillMaxWidth()
            .imePadding()
            .padding(horizontal = 12.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        OutlinedTextField(
            value = text,
            onValueChange = { text = it },
            modifier = Modifier.weight(1f),
            placeholder = { Text("Message") },
            maxLines = 4,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Send),
            keyboardActions = KeyboardActions(onSend = { send() }),
        )
        IconButton(
            onClick = ::send,
            enabled = text.isNotBlank(),
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.Send,
                contentDescription = "Send",
            )
        }
    }
}