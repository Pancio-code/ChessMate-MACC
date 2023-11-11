package com.example.chessmate.ui.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MenuOpen
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.PermanentDrawerSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.Measurable
import androidx.compose.ui.layout.MeasurePolicy
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.offset
import com.example.chessmate.R
import com.example.chessmate.ui.utils.ChessMateNavigationContentPosition

@Composable
fun ChessMateNavigationRail(
    selectedDestination: String,
    navigationContentPosition: ChessMateNavigationContentPosition,
    navigateToTopLevelDestination: (ChessMateTopLevelDestination) -> Unit,
    onDrawerClicked: () -> Unit = {},
    isAuthenticate: Boolean
) {
    NavigationRail(
        modifier = Modifier.fillMaxHeight(),
        containerColor = MaterialTheme.colorScheme.inverseOnSurface
    ) {
        Layout(
            modifier = Modifier.widthIn(max = 80.dp),
            content = {
                Column(
                    modifier = Modifier.layoutId(LayoutType.HEADER),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    NavigationRailItem(
                        selected = false,
                        onClick = onDrawerClicked,
                        icon = {
                            Icon(
                                imageVector = Icons.Default.Menu,
                                contentDescription = stringResource(id = R.string.navigation_drawer)
                            )
                        }
                    )
                    FloatingActionButton(
                        onClick = { navigateToTopLevelDestination(
                            ChessMateTopLevelDestination(
                                route = ChessMateRoute.SETTINGS,
                                selectedIcon = Icons.Default.Settings,
                                unselectedIcon = Icons.Outlined.Settings,
                                iconTextId = R.string.tab_settings
                            )) },
                        modifier = Modifier.padding(top = 8.dp, bottom = 32.dp),
                        containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                        contentColor = MaterialTheme.colorScheme.onTertiaryContainer
                    ) {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = stringResource(id = R.string.edit),
                            modifier = Modifier.size(18.dp)
                        )
                    }
                    Spacer(Modifier.height(12.dp))
                }
                Column(
                    modifier = Modifier.layoutId(LayoutType.CONTENT),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    if (isAuthenticate) {
                        TOP_LEVEL_DESTINATIONS.forEach { ChessMateDestination ->
                            val selected = selectedDestination == ChessMateDestination.route
                            NavigationRailItem(
                                selected = selected,
                                onClick = { navigateToTopLevelDestination(ChessMateDestination) },
                                icon = {
                                    Icon(
                                        imageVector = if(selected) ChessMateDestination.selectedIcon else ChessMateDestination.unselectedIcon,
                                        contentDescription = stringResource(
                                            id = ChessMateDestination.iconTextId
                                        )
                                    )
                                },
                                label = {
                                    Text(text = stringResource(id = ChessMateDestination.iconTextId))
                                }
                            )
                        }
                    } else {
                        LOGIN_LEVEL_DESTINATIONS.forEach { ChessMateDestination ->
                            val selected = selectedDestination == ChessMateDestination.route
                            NavigationRailItem(
                                selected = selectedDestination == ChessMateDestination.route,
                                onClick = { navigateToTopLevelDestination(ChessMateDestination) },
                                icon = {
                                    Icon(
                                        imageVector = if(selected) ChessMateDestination.selectedIcon else ChessMateDestination.unselectedIcon,
                                        contentDescription = stringResource(
                                            id = ChessMateDestination.iconTextId
                                        )
                                    )
                                },
                                label = {
                                    Text(text = stringResource(id = ChessMateDestination.iconTextId))
                                }
                            )
                        }
                    }
                }
            },
            measurePolicy = navigationMeasurePolicy(navigationContentPosition)
        )
    }
}

@Composable
fun ChessMateBottomNavigationBar(
    selectedDestination: String,
    navigateToTopLevelDestination: (ChessMateTopLevelDestination) -> Unit,
    isAuthenticated: Boolean
) {
    NavigationBar(modifier = Modifier.fillMaxWidth()) {
        if (isAuthenticated) {
            TOP_LEVEL_DESTINATIONS.forEach { ChessMateDestination ->
                val selected = selectedDestination == ChessMateDestination.route
                NavigationBarItem(
                    selected = selected,
                    onClick = { navigateToTopLevelDestination(ChessMateDestination) },
                    icon = {
                        Icon(
                            imageVector = if(selected) ChessMateDestination.selectedIcon else ChessMateDestination.unselectedIcon,
                            contentDescription = stringResource(id = ChessMateDestination.iconTextId)
                        )
                    },
                    label = {
                        Text(text = stringResource(id = ChessMateDestination.iconTextId))
                    }
                )
            }
        } else {
            LOGIN_LEVEL_DESTINATIONS.forEach { ChessMateDestination ->
                val selected = selectedDestination == ChessMateDestination.route
                NavigationBarItem(
                    selected = selected,
                    onClick = { navigateToTopLevelDestination(ChessMateDestination) },
                    icon = {
                        Icon(
                            imageVector = if(selected) ChessMateDestination.selectedIcon else ChessMateDestination.unselectedIcon,
                            contentDescription = stringResource(id = ChessMateDestination.iconTextId)
                        )
                    },
                    label = {
                        Text(text = stringResource(id = ChessMateDestination.iconTextId))
                    }
                )
            }
        }
    }
}

@Composable
fun PermanentNavigationDrawerContent(
    selectedDestination: String,
    navigationContentPosition: ChessMateNavigationContentPosition,
    navigateToTopLevelDestination: (ChessMateTopLevelDestination) -> Unit,
    isAuthenticate: Boolean
) {
    PermanentDrawerSheet(modifier = Modifier.sizeIn(minWidth = 200.dp, maxWidth = 300.dp)) {
        Layout(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.inverseOnSurface)
                .padding(16.dp),
            content = {
                Column(
                    modifier = Modifier.layoutId(LayoutType.HEADER),
                    horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        modifier = Modifier
                            .padding(16.dp),
                        text = stringResource(id = R.string.app_name).uppercase(),
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                    ExtendedFloatingActionButton(
                        onClick = { navigateToTopLevelDestination(
                            ChessMateTopLevelDestination(
                                route = ChessMateRoute.SETTINGS,
                                selectedIcon = Icons.Default.Settings,
                                unselectedIcon = Icons.Outlined.Settings,
                                iconTextId = R.string.tab_settings
                            )) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp, bottom = 40.dp),
                        containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                        contentColor = MaterialTheme.colorScheme.onTertiaryContainer
                    ) {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = stringResource(id = R.string.edit),
                            modifier = Modifier.size(18.dp)
                        )
                        Text(
                            text = stringResource(id = R.string.tab_settings),
                            modifier = Modifier.weight(1f),
                            textAlign = TextAlign.Center
                        )
                    }
                }
                Column(
                    modifier = Modifier
                        .layoutId(LayoutType.CONTENT)
                        .verticalScroll(rememberScrollState()),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    if (isAuthenticate) {
                        TOP_LEVEL_DESTINATIONS.forEach { ChessMateDestination ->
                            val selected = selectedDestination == ChessMateDestination.route
                            NavigationDrawerItem(
                                selected = selected,
                                label = {
                                    Text(
                                        text = stringResource(id = ChessMateDestination.iconTextId),
                                        modifier = Modifier.padding(horizontal = 16.dp)
                                    )
                                },
                                icon = {
                                    Icon(
                                        imageVector = if(selected) ChessMateDestination.selectedIcon else ChessMateDestination.unselectedIcon,
                                        contentDescription = stringResource(
                                            id = ChessMateDestination.iconTextId
                                        )
                                    )
                                },
                                colors = NavigationDrawerItemDefaults.colors(
                                    unselectedContainerColor = Color.Transparent
                                ),
                                onClick = { navigateToTopLevelDestination(ChessMateDestination) }
                            )
                        }
                    } else {
                        LOGIN_LEVEL_DESTINATIONS.forEach { ChessMateDestination ->
                            val selected = selectedDestination == ChessMateDestination.route
                            NavigationDrawerItem(
                                selected = selected,
                                label = {
                                    Text(
                                        text = stringResource(id = ChessMateDestination.iconTextId),
                                        modifier = Modifier.padding(horizontal = 16.dp)
                                    )
                                },
                                icon = {
                                    Icon(
                                        imageVector = if(selected) ChessMateDestination.selectedIcon else ChessMateDestination.unselectedIcon,
                                        contentDescription = stringResource(
                                            id = ChessMateDestination.iconTextId
                                        )
                                    )
                                },
                                colors = NavigationDrawerItemDefaults.colors(
                                    unselectedContainerColor = Color.Transparent
                                ),
                                onClick = { navigateToTopLevelDestination(ChessMateDestination) }
                            )
                        }
                    }
                }
            },
            measurePolicy = navigationMeasurePolicy(navigationContentPosition)
        )
    }
}

@Composable
fun ModalNavigationDrawerContent(
    selectedDestination: String,
    navigationContentPosition: ChessMateNavigationContentPosition,
    navigateToTopLevelDestination: (ChessMateTopLevelDestination) -> Unit,
    onDrawerClicked: () -> Unit = {},
    isAuthenticate: Boolean
) {
    ModalDrawerSheet {
        Layout(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.inverseOnSurface)
                .padding(16.dp),
            content = {
                Column(
                    modifier = Modifier.layoutId(LayoutType.HEADER),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = stringResource(id = R.string.app_name).uppercase(),
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.primary
                        )
                        IconButton(onClick = onDrawerClicked) {
                            Icon(
                                imageVector = Icons.Default.MenuOpen,
                                contentDescription = stringResource(id = R.string.navigation_drawer)
                            )
                        }
                    }

                    ExtendedFloatingActionButton(
                        onClick = { navigateToTopLevelDestination(
                            ChessMateTopLevelDestination(
                                route = ChessMateRoute.SETTINGS,
                                selectedIcon = Icons.Default.Settings,
                                unselectedIcon = Icons.Outlined.Settings,
                                iconTextId = R.string.tab_settings
                            )) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp, bottom = 40.dp),
                        containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                        contentColor = MaterialTheme.colorScheme.onTertiaryContainer
                    ) {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = stringResource(id = R.string.tab_settings),
                            modifier = Modifier.size(18.dp)
                        )
                        Text(
                            text = stringResource(id = R.string.tab_settings),
                            modifier = Modifier.weight(1f),
                            textAlign = TextAlign.Center
                        )
                    }
                }

                Column(
                    modifier = Modifier
                        .layoutId(LayoutType.CONTENT)
                        .verticalScroll(rememberScrollState()),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    if (isAuthenticate) {
                        TOP_LEVEL_DESTINATIONS.forEach { ChessMateDestination ->
                            val selected = selectedDestination == ChessMateDestination.route
                            NavigationDrawerItem(
                                selected = selected,
                                label = {
                                    Text(
                                        text = stringResource(id = ChessMateDestination.iconTextId),
                                        modifier = Modifier.padding(horizontal = 16.dp)
                                    )
                                },
                                icon = {
                                    Icon(
                                        imageVector = if(selected) ChessMateDestination.selectedIcon else ChessMateDestination.unselectedIcon,
                                        contentDescription = stringResource(
                                            id = ChessMateDestination.iconTextId
                                        )
                                    )
                                },
                                colors = NavigationDrawerItemDefaults.colors(
                                    unselectedContainerColor = Color.Transparent
                                ),
                                onClick = { navigateToTopLevelDestination(ChessMateDestination) }
                            )
                        }
                    } else {
                        LOGIN_LEVEL_DESTINATIONS.forEach { ChessMateDestination ->
                            val selected = selectedDestination == ChessMateDestination.route
                            NavigationDrawerItem(
                                selected = selected,
                                label = {
                                    Text(
                                        text = stringResource(id = ChessMateDestination.iconTextId),
                                        modifier = Modifier.padding(horizontal = 16.dp)
                                    )
                                },
                                icon = {
                                    Icon(
                                        imageVector = if(selected) ChessMateDestination.selectedIcon else ChessMateDestination.unselectedIcon,
                                        contentDescription = stringResource(
                                            id = ChessMateDestination.iconTextId
                                        )
                                    )
                                },
                                colors = NavigationDrawerItemDefaults.colors(
                                    unselectedContainerColor = Color.Transparent
                                ),
                                onClick = { navigateToTopLevelDestination(ChessMateDestination) }
                            )
                        }
                    }
                }
            },
            measurePolicy = navigationMeasurePolicy(navigationContentPosition)
        )
    }
}

fun navigationMeasurePolicy(
    navigationContentPosition: ChessMateNavigationContentPosition,
): MeasurePolicy {
    return MeasurePolicy { measurables, constraints ->
        lateinit var headerMeasurable: Measurable
        lateinit var contentMeasurable: Measurable
        measurables.forEach {
            when (it.layoutId) {
                LayoutType.HEADER -> headerMeasurable = it
                LayoutType.CONTENT -> contentMeasurable = it
                else -> error("Unknown layoutId encountered!")
            }
        }

        val headerPlaceable = headerMeasurable.measure(constraints)
        val contentPlaceable = contentMeasurable.measure(
            constraints.offset(vertical = -headerPlaceable.height)
        )
        layout(constraints.maxWidth, constraints.maxHeight) {
            headerPlaceable.placeRelative(0, 0)

            val nonContentVerticalSpace = constraints.maxHeight - contentPlaceable.height

            val contentPlaceableY = when (navigationContentPosition) {

                ChessMateNavigationContentPosition.TOP -> 0
                ChessMateNavigationContentPosition.CENTER -> nonContentVerticalSpace / 2
            }
                .coerceAtLeast(headerPlaceable.height)

            contentPlaceable.placeRelative(0, contentPlaceableY)
        }
    }
}

enum class LayoutType {
    HEADER, CONTENT
}
