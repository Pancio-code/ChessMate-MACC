package com.example.chessmate.ui.components

import androidx.activity.OnBackPressedCallback
import androidx.compose.foundation.Image
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.chessmate.R
import com.example.chessmate.ui.theme.ChessMateTheme

@Composable
fun NavBarComponent(navController: NavController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
        var selectedItem by remember { mutableIntStateOf(0) }
        val items = listOf("Home", "Scan", "Profile")

        val icons = listOf(
            R.drawable.ic_home,
            R.drawable.ic_photo_camera,
            R.drawable.ic_person
        )

        NavigationBar {
            items.forEachIndexed { index, item ->
                NavigationBarItem(
                    icon = {
                        Image(
                            painter = painterResource(id = icons[index]),
                            contentDescription = item,
                            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary)
                        )
                    },
                    label = { Text(item) },
                    selected = currentDestination?.hierarchy?.any { it.route == item } == true,
                    onClick = {
                        navController.navigate(item) {
                            // Pop up to the start destination of the graph to
                            // avoid building up a large stack of destinations
                            // on the back stack as users select items
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            // Avoid multiple copies of the same destination when
                            // reselecting the same item
                            launchSingleTop = true
                            // Restore state when reselecting a previously selected item
                            restoreState = true
                        }
                    }
                )
            }
        }
}
