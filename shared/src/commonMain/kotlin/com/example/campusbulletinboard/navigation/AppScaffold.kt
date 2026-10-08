package com.example.campusbulletinboard.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.automirrored.outlined.List
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector

private data class NavigationBarDestination(
    val route: Route,
    val label: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
)

private val navigationBarDestinations = listOf(
    NavigationBarDestination(
        route = Route.Create,
        label = "Create",
        selectedIcon = Icons.Filled.Edit,
        unselectedIcon = Icons.Outlined.Edit,
    ),
    NavigationBarDestination(
        route = Route.Announcements,
        label = "Announcements",
        selectedIcon = Icons.AutoMirrored.Filled.List,
        unselectedIcon = Icons.AutoMirrored.Outlined.List,
    ),
    NavigationBarDestination(
        route = Route.About,
        label = "About",
        selectedIcon = Icons.Filled.Info,
        unselectedIcon = Icons.Outlined.Info,
    ),
)

private val Route.navigationBarRoute: Route
    get() = when (this) {
        Route.Create, is Route.Preview -> Route.Create
        Route.Announcements, is Route.Chat -> Route.Announcements
        Route.About -> Route.About
    }

private val Route.title: String
    get() = when (this) {
        Route.Create -> "Create announcement"
        is Route.Preview -> "Preview"
        Route.Announcements -> "Announcements"
        Route.About -> "About"
        is Route.Chat -> "Messages"
    }

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppScaffold(
    currentRoute: Route,
    onNavigate: (Route) -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable (PaddingValues) -> Unit,
) {
    val selectedRoute = currentRoute.navigationBarRoute

    Scaffold(
        modifier = modifier,
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(currentRoute.title) },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                ),
            )
        },
        bottomBar = {
            NavigationBar {
                navigationBarDestinations.forEach { destination ->
                    val selected = destination.route == selectedRoute
                    NavigationBarItem(
                        selected = selected,
                        onClick = { onNavigate(destination.route) },
                        icon = {
                            Icon(
                                imageVector = if (selected) {
                                    destination.selectedIcon
                                } else {
                                    destination.unselectedIcon
                                },
                                contentDescription = destination.label,
                            )
                        },
                        label = { Text(destination.label) },
                    )
                }
            }
        },
    ) { innerPadding ->
        content(innerPadding)
    }
}