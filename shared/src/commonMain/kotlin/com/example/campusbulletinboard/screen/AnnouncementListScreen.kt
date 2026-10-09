package com.example.campusbulletinboard.screen

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil3.compose.SubcomposeAsyncImage
import com.example.campusbulletinboard.model.Announcement
import com.example.campusbulletinboard.state.currentBulletinBoardState

/**
 * Announcements tab: search, sort, expand, delete, and open chat.
 *
 * Titles can be filtered and sorted A–Z or Z–A. A collapsed row shows a thumbnail,
 * title, description, and poster. Expanding it shows [AnnouncementHeroImage] and an
 * Open Chat action. An empty board and a search with no matches each have their own message.
 */
@Composable
fun AnnouncementListScreen(
    onOpenChat: (announcementId: String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val board = currentBulletinBoardState()
    val announcements = board.announcements
    var titleQuery by rememberSaveable { mutableStateOf("") }
    var sortAscending by rememberSaveable { mutableStateOf(true) }
    var expandedId by rememberSaveable { mutableStateOf<String?>(null) }
    val trimmedQuery = titleQuery.trim()
    val filteredAnnouncements = if (trimmedQuery.isEmpty()) {
        announcements
    } else {
        announcements.filter { announcement ->
            announcement.title.contains(trimmedQuery, ignoreCase = true)
        }
    }
    val sortedAnnouncements = if (sortAscending) {
        filteredAnnouncements.sortedBy { it.title.lowercase() }
    } else {
        filteredAnnouncements.sortedByDescending { it.title.lowercase() }
    }


    if (announcements.isEmpty()) {
        EmptyAnnouncementList(modifier)
    } else {
        Column(modifier = modifier.fillMaxSize()) {
            AnnouncementTitleFilter(
                query = titleQuery,
                onQueryChange = { titleQuery = it },
                modifier = Modifier.padding(start = 16.dp, top = 8.dp, end = 16.dp),
            )
            AnnouncementSortToggle(
                ascending = sortAscending,
                onAscendingChange = { sortAscending = it },
                modifier = Modifier.padding(start = 16.dp, top = 8.dp, end = 16.dp),
            )
            if (sortedAnnouncements.isEmpty()) {
                NoMatchingAnnouncements(Modifier.fillMaxSize())
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    items(
                        items = sortedAnnouncements,
                        key = { it.id },
                    ) { announcement ->
                        AnnouncementRow(
                            announcement = announcement,
                            expanded = expandedId == announcement.id,
                            onToggle = {
                                expandedId = if (expandedId == announcement.id) {
                                    null
                                } else {
                                    announcement.id
                                }
                            },
                            onOpenChat = { onOpenChat(announcement.id) },
                            onDelete = { board.deleteAnnouncement(announcement.id) },
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun AnnouncementTitleFilter(
    query: String,
    onQueryChange: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    OutlinedTextField(
        value = query,
        onValueChange = onQueryChange,
        modifier = modifier.fillMaxWidth(),
        label = { Text("Search titles") },
        placeholder = { Text("Filter by title") },
        singleLine = true,
        leadingIcon = {
            Icon(
                imageVector = Icons.Outlined.Search,
                contentDescription = null,
            )
        },
        trailingIcon = if (query.isNotEmpty()) {
            {
                IconButton(onClick = { onQueryChange("") }) {
                    Icon(
                        imageVector = Icons.Outlined.Close,
                        contentDescription = "Clear search",
                    )
                }
            }
        } else {
            null
        },
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
    )
}

@Composable
private fun NoMatchingAnnouncements(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = "No announcements match",
            style = MaterialTheme.typography.titleMedium,
            textAlign = TextAlign.Center,
        )
        Text(
            text = "Try a different title.",
            modifier = Modifier.padding(top = 8.dp),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
        )
    }
}

@Composable
private fun AnnouncementSortToggle(
    ascending: Boolean,
    onAscendingChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    SingleChoiceSegmentedButtonRow(modifier = modifier.fillMaxWidth()) {
        SegmentedButton(
            selected = ascending,
            onClick = { onAscendingChange(true) },
            shape = SegmentedButtonDefaults.itemShape(index = 0, count = 2),
            modifier = Modifier.weight(1f),
            label = { Text("A–Z") },
        )
        SegmentedButton(
            selected = !ascending,
            onClick = { onAscendingChange(false) },
            shape = SegmentedButtonDefaults.itemShape(index = 1, count = 2),
            modifier = Modifier.weight(1f),
            label = { Text("Z–A") },
        )
    }
}

@Composable
private fun AnnouncementRow(
    announcement: Announcement,
    expanded: Boolean,
    onToggle: () -> Unit,
    onOpenChat: () -> Unit,
    onDelete: () -> Unit,
) {
    Column(
        modifier = Modifier
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.surfaceContainerLow)
            .animateContentSize(),
    ) {
        if (expanded) {
            ExpandedAnnouncement(
                announcement = announcement,
                onClick = onToggle,
            )
        } else {
            ListItem(
                modifier = Modifier.clickable(onClick = onToggle),
                colors = ListItemDefaults.colors(
                    containerColor = MaterialTheme.colorScheme.surfaceContainerLow,
                ),
                leadingContent = {
                    AnnouncementThumbnail(
                        imageUrl = announcement.imageUrl,
                        contentDescription = announcement.title,
                    )
                },
                headlineContent = {
                    Text(
                        text = announcement.title,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        style = MaterialTheme.typography.titleMedium,
                    )
                },
                supportingContent = {
                    Column {
                        Text(
                            text = announcement.description,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                        Text(
                            text = announcement.posterName,
                            modifier = Modifier.padding(top = 2.dp),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            style = MaterialTheme.typography.labelSmall,
                        )
                    }
                },
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(end = 12.dp, bottom = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            IconButton(
                onClick = onDelete,
                modifier = Modifier.padding(start = 4.dp),
            ) {
                Icon(
                    imageVector = Icons.Outlined.Delete,
                    contentDescription = "Delete ${announcement.title}",
                    tint = MaterialTheme.colorScheme.error,
                )
            }
            if (expanded) {
                Button(onClick = onOpenChat) {
                    Text("Open Chat")
                }
            }
        }
    }
}
@Composable
private fun ExpandedAnnouncement(
    announcement: Announcement,
    onClick: () -> Unit,
) {
    Column(modifier = Modifier.clickable(onClick = onClick)) {
        AnnouncementHeroImage(
            imageUrl = announcement.imageUrl,
            contentDescription = announcement.title,
            modifier = Modifier.fillMaxWidth(),
        )
        Text(
            text = announcement.title,
            modifier = Modifier.padding(start = 16.dp, top = 16.dp, end = 16.dp),
            style = MaterialTheme.typography.headlineSmall,
        )
        Text(
            text = announcement.description,
            modifier = Modifier.padding(start = 16.dp, top = 8.dp, end = 16.dp, bottom = 8.dp),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

@Composable
private fun AnnouncementThumbnail(
    imageUrl: String,
    contentDescription: String,
) {
    SubcomposeAsyncImage(
        model = imageUrl,
        contentDescription = contentDescription,
        modifier = Modifier
            .size(72.dp)
            .clip(RoundedCornerShape(12.dp)),
        contentScale = ContentScale.Crop,
        loading = {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.surfaceVariant),
                contentAlignment = Alignment.Center,
            ) {
                CircularProgressIndicator(modifier = Modifier.size(24.dp))
            }
        },
        error = {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.surfaceVariant),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    imageVector = Icons.Outlined.Info,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        },
    )
}

@Composable
private fun EmptyAnnouncementList(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = "No announcements yet",
            style = MaterialTheme.typography.titleMedium,
            textAlign = TextAlign.Center,
        )
        Text(
            text = "Create an announcement and it will show up here.",
            modifier = Modifier.padding(top = 8.dp),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
        )
    }
}