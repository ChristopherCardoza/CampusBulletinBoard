package com.example.campusbulletinboard.screen

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import kotlin.coroutines.cancellation.CancellationException
import com.example.campusbulletinboard.model.AnnouncementDraft

@Composable
fun CreateAnnouncementScreen(
    onSubmit: (AnnouncementDraft) -> Unit,
    modifier: Modifier = Modifier,
) {
    var title by rememberSaveable { mutableStateOf("") }
    var description by rememberSaveable { mutableStateOf("") }
    var imageUrl by rememberSaveable { mutableStateOf("") }
    var images by remember { mutableStateOf<List<PicsumImage>>(emptyList()) }
    var imagesLoading by remember { mutableStateOf(true) }
    var imagesError by remember { mutableStateOf<String?>(null) }
    var reloadKey by remember { mutableIntStateOf(0) }
    var showRequiredErrors by remember { mutableStateOf(false) }
    var showUrlError by remember { mutableStateOf(false) }

    LaunchedEffect(reloadKey) {
        imagesLoading = true
        imagesError = null
        try {
            images = loadPicsumImages()
        } catch (cancelled: CancellationException) {
            throw cancelled
        } catch (_: Exception) {
            imagesError = "Could not load photos."
        } finally {
            imagesLoading = false
        }
    }

    Column(
        modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        Text(
            text = "What should students see first?",
            style = MaterialTheme.typography.titleMedium,
        )
        OutlinedTextField(
            value = title,
            onValueChange = { title = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            label = { Text("Title") },
            placeholder = { Text("Club meeting, lost item, campus event") },
            singleLine = true,
            isError = showRequiredErrors && title.isBlank(),
            supportingText = {
                Text(
                    if (showRequiredErrors && title.isBlank()) {
                        "Title is required"
                    } else {
                        "${title.length}/80"
                    },
                )
            },
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Sentences,
            ),

        )
        OutlinedTextField(
            value = description,
            onValueChange = { description = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            label = { Text("Description") },
            placeholder = { Text("When, where, and what students should know") },
            minLines = 4,
            isError = showRequiredErrors && description.isBlank(),
            supportingText = {
                Text(
                    if (showRequiredErrors && description.isBlank()) {
                        "Description is required"
                    } else {
                        "${description.length}/500"
                    },
                )
            },
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Sentences,
            ),
        )
        OutlinedTextField(
            value = imageUrl,
            onValueChange = { imageUrl = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            label = { Text("Image URL") },
            placeholder = { Text("https://example.com/poster.jpg") },
            singleLine = true,
            isError = (showRequiredErrors && imageUrl.isBlank()) ||
                    (showUrlError && imageUrl.isNotBlank()),
            supportingText = {
                Text(
                    when {
                        showRequiredErrors && imageUrl.isBlank() -> "Image is required"
                        showUrlError && imageUrl.isNotBlank() -> "Enter a link that starts with http:// or https://"
                        else -> "Paste a direct link to an image"
                    },
                )
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Uri,
            ),
        )
        Text(
            text = "Or choose a photo",
            modifier = Modifier.padding(top = 16.dp),
            style = MaterialTheme.typography.titleSmall,
        )
        when {
            imagesLoading -> {
                CircularProgressIndicator(modifier = Modifier.padding(top = 12.dp))
            }
            imagesError != null -> {
                Text(
                    text = imagesError!!,
                    modifier = Modifier.padding(top = 8.dp),
                    color = MaterialTheme.colorScheme.error,
                )
                TextButton(onClick = { reloadKey++ }) {
                    Text("Try again")
                }
            }
            else -> {
                LazyRow(
                    modifier = Modifier.padding(top = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    items(images, key = { it.id }) { image ->
                        val selected = imageUrl == image.imageUrl
                        AsyncImage(
                            model = image.thumbnailUrl,
                            contentDescription = "Photo by ${image.author}",
                            modifier = Modifier
                                .size(96.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .border(
                                    width = if (selected) 3.dp else 1.dp,
                                    color = if (selected) {
                                        MaterialTheme.colorScheme.primary
                                    } else {
                                        MaterialTheme.colorScheme.outline
                                    },
                                    shape = RoundedCornerShape(12.dp),
                                )
                                .clickable { imageUrl = image.imageUrl },
                            contentScale = ContentScale.Crop,
                        )
                    }
                }
                val selectedImage = images.find { it.imageUrl == imageUrl }
                if (selectedImage != null) {
                    Text(
                        text = "Selected photo by ${selectedImage.author}",
                        modifier = Modifier.padding(top = 8.dp),
                        style = MaterialTheme.typography.bodyMedium,
                    )
                }
            }
        }
        Button(
            onClick = {
                val trimmedTitle = title.trim()
                val trimmedDescription = description.trim()
                val trimmedImageUrl = imageUrl.trim()
                val missingRequiredField = trimmedTitle.isBlank() ||
                        trimmedDescription.isBlank() ||
                        trimmedImageUrl.isBlank()
                showRequiredErrors = missingRequiredField
                if (missingRequiredField) {
                    showUrlError = false
                    return@Button
                }
                val invalidImageUrl = !trimmedImageUrl.isWebImageLink()
                showUrlError = invalidImageUrl
                if (!invalidImageUrl) {
                    onSubmit(
                        AnnouncementDraft(
                            title = trimmedTitle,
                            description = trimmedDescription,
                            imageUrl = trimmedImageUrl,
                        ),
                    )
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 24.dp),
        ) {
            Text("Submit")
        }

    }
}

private fun String.isWebImageLink(): Boolean {
    val value = trim()
    val scheme = when {
        value.startsWith("https://", ignoreCase = true) -> "https://"
        value.startsWith("http://", ignoreCase = true) -> "http://"
        else -> return false
    }
    if (value.any { it.isWhitespace() }) return false
    val host = value.substring(scheme.length)
        .substringBefore('/')
        .substringBefore('?')
        .substringBefore('#')
    return host.isNotBlank() && (host == "localhost" || '.' in host)
}