package com.example.campusbulletinboard.screen

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.example.campusbulletinboard.theme.SurfaceToBackground

/**
 * Static sample post shown on the About tab.
 *
 * [title], [description], and [imageUrl] are display-only. These posts are not added to the board.
 */
private data class FeaturedAnnouncement(
    val title: String,
    val description: String,
    val imageUrl: String,
)

/**
 * The two sample posts rendered under the About copy: library hours and the club fair.
 */
private val featuredAnnouncements = listOf(
    FeaturedAnnouncement(
        title = "Library hours this week",
        description = "The main library stays open until midnight Sunday through Thursday for midterms.",
        imageUrl = "https://picsum.photos/seed/campuslibrary/1200/800",
    ),
    FeaturedAnnouncement(
        title = "Club fair on the quad",
        description = "Student clubs set up tables Friday from noon to 3. Stop by and sign up.",
        imageUrl = "https://picsum.photos/seed/campusquad/1200/800",
    ),
)

/**
 * About tab: a short description of the app, two sample announcements, and a purpose card.
 *
 * The featured posts are static content. They are not stored on [com.example.campusbulletinboard.state.BulletinBoardState].
 */
@Composable
fun AboutScreen(modifier: Modifier = Modifier) {
    val primary = MaterialTheme.colorScheme.primary
    val secondary = MaterialTheme.colorScheme.secondary
    val tertiary = MaterialTheme.colorScheme.tertiary
    val drift by rememberInfiniteTransition(label = "aboutGradient").animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 9000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse,
        ),
        label = "aboutGradientDrift",
    )

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(SurfaceToBackground),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.primaryContainer)
                    .padding(horizontal = 20.dp, vertical = 28.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Text(
                    text = "CampusBulletinBoard",
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                )
                Text(
                    text = "Post campus announcements and message each other about them.",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                )
            }

            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                InfoCard(
                    icon = Icons.Outlined.Info,
                    title = "About the app",
                    body = "CampusBulletinBoard is a student bulletin for campus news. " +
                            "You write an announcement with a title, a description, and an image, " +
                            "preview how it will look, and add it to the board. Every post keeps " +
                            "its own message thread, so a conversation stays tied to that announcement.",
                )
                featuredAnnouncements.forEach { announcement ->
                    FeaturedAnnouncementCard(announcement)
                }
                InfoCard(
                    icon = Icons.Outlined.Star,
                    title = "Purpose",
                    body = "The purpose of the app is to give students one local place to share " +
                            "campus news and talk about a specific announcement.",
                )
            }
        }
    }
}

/**
 * Elevated card for one [FeaturedAnnouncement]: wide image, title, then description.
 */
@Composable
private fun FeaturedAnnouncementCard(
    announcement: FeaturedAnnouncement,
    modifier: Modifier = Modifier,
) {
    ElevatedCard(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.elevatedCardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerLow,
        ),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 4.dp),
    ) {
        AnnouncementHeroImage(
            imageUrl = announcement.imageUrl,
            contentDescription = announcement.title,
            modifier = Modifier.fillMaxWidth(),
        )
        Text(
            text = announcement.title,
            modifier = Modifier.padding(start = 20.dp, top = 20.dp, end = 20.dp),
            style = MaterialTheme.typography.headlineSmall,
        )
        Text(
            text = announcement.description,
            modifier = Modifier.padding(start = 20.dp, top = 8.dp, end = 20.dp, bottom = 20.dp),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

/**
 * Elevated card with a leading icon, a title, and a body paragraph.
 *
 * Used for the "About the app" and "Purpose" sections.
 */
@Composable
private fun InfoCard(
    icon: ImageVector,
    title: String,
    body: String,
    modifier: Modifier = Modifier,
) {
    ElevatedCard(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.elevatedCardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerLow,
        ),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 4.dp),
    ) {
        Row(
            modifier = Modifier.padding(20.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(24.dp),
                tint = MaterialTheme.colorScheme.primary,
            )
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                )
                Text(
                    text = body,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
    }
}