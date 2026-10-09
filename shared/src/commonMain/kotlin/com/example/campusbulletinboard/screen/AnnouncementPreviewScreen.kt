package com.example.campusbulletinboard.screen

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil3.compose.SubcomposeAsyncImage

/**
 * Read-only preview of an announcement before it is posted.
 *
 * The card, title, and description fade and rise in sequence. Edit returns to the
 * form; Confirm is handled by the caller, which saves the announcement.
 * [AnnouncementHeroImage] is the shared wide image used by preview, the list, and About.
 */
@Composable
fun AnnouncementPreviewScreen(
    title: String,
    description: String,
    imageUrl: String,
    onConfirm: () -> Unit,
    onEdit: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val cardReveal = rememberReveal(delayMillis = 40)
    val titleReveal = rememberReveal(delayMillis = 160)
    val descriptionReveal = rememberReveal(delayMillis = 260)
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
    ) {
        ElevatedCard(
            modifier = Modifier
                .fillMaxWidth()
                .graphicsLayer {
                    alpha = cardReveal
                    translationY = (1f - cardReveal) * 28.dp.toPx()
                    val scale = 0.97f + (0.03f * cardReveal)
                    scaleX = scale
                    scaleY = scale
                },
            shape = RoundedCornerShape(28.dp),
            colors = CardDefaults.elevatedCardColors(
                containerColor = MaterialTheme.colorScheme.surfaceContainerLow,
            ),
            elevation = CardDefaults.elevatedCardElevation(defaultElevation = 4.dp),
        ) {
            AnnouncementHeroImage(
                imageUrl = imageUrl,
                contentDescription = title,
                modifier = Modifier.fillMaxWidth(),
            )
            Text(
                text = title,
                modifier = Modifier
                    .padding(start = 20.dp, top = 20.dp, end = 20.dp)
                    .graphicsLayer {
                        alpha = titleReveal
                        translationY = (1f - titleReveal) * 16.dp.toPx()
                    },
                style = MaterialTheme.typography.headlineMedium,
            )
            Text(
                text = description,
                modifier = Modifier
                    .padding(start = 20.dp, top = 8.dp, end = 20.dp)
                    .graphicsLayer {
                        alpha = descriptionReveal
                        translationY = (1f - descriptionReveal) * 16.dp.toPx()
                    },
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 20.dp, top = 20.dp, end = 20.dp, bottom = 20.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                OutlinedButton(
                    onClick = onEdit,
                    modifier = Modifier.weight(1f),
                ) {
                    Text("Edit")
                }
                Button(
                    onClick = onConfirm,
                    modifier = Modifier.weight(1f),
                ) {
                    Text("Confirm")
                }
            }
        }
    }
}

/**
 * Wide cropped image shared by the preview, an expanded list row, and About's sample cards.
 *
 * The image is a 3:2 crop with rounded bottom corners. A spinner fills the frame while it loads.
 * If the load fails, the frame shows an info icon and "Image unavailable".
 */
@Composable
fun AnnouncementHeroImage(
    imageUrl: String,
    contentDescription: String,
    modifier: Modifier = Modifier,
) {
    SubcomposeAsyncImage(
        model = imageUrl,
        contentDescription = contentDescription,
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(3f / 2f)
            .clip(RoundedCornerShape(bottomStart = 28.dp, bottomEnd = 28.dp)),
        contentScale = ContentScale.Crop,
        loading = {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.surfaceVariant),
                contentAlignment = Alignment.Center,
            ) {
                CircularProgressIndicator()
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
                Text(
                    text = "Image unavailable",
                    modifier = Modifier.align(Alignment.BottomCenter),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        },
    )
}

/**
 * Fades a value from 0 to 1 once, after this composable enters composition.
 *
 * The animation runs for 420ms with [delayMillis] before it starts. Callers use the result for alpha, slide, and scale.
 */
@Composable
private fun rememberReveal(delayMillis: Int): Float {
    var started by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) { started = true }
    val reveal by animateFloatAsState(
        targetValue = if (started) 1f else 0f,
        animationSpec = tween(
            durationMillis = 420,
            delayMillis = delayMillis,
            easing = FastOutSlowInEasing,
        ),
        label = "previewReveal",
    )
    return reveal
}