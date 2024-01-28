package com.example.chessmate.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Login
import androidx.compose.material.icons.automirrored.outlined.Login
import androidx.compose.material.icons.filled.AppRegistration
import androidx.compose.material.icons.filled.Camera
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.AppRegistration
import androidx.compose.material.icons.outlined.Camera
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import com.example.chessmate.R


object ChessMateRoute {
    const val HOME = "Home"
    const val PROFILE = "Profile"
    const val SCAN = "Scan"
    const val SETTINGS = "Settings"
    const val SIGN_IN = "Sign In"
    const val SIGN_UP = "Sign Up"
    const val CONTACT = "Contact Us"
    const val FIND_GAME = "Multiplayer Game"
    const val ONLINE_GAME = "Online Game"
    const val OFFLINE_GAME = "Offline Game"
    const val AI_GAME = "AI Game"
    const val SELECT_COLOR = "Select Color"
}

data class ChessMateTopLevelDestination(
    val route: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val iconTextId: Int
)

class ChessMateNavigationActions(private val navController: NavHostController) {
    fun navigateTo(destination: ChessMateTopLevelDestination) {
        navController.navigate(destination.route) {
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
        }
    }
}

val TOP_LEVEL_DESTINATIONS = listOf(
    ChessMateTopLevelDestination(
        route = ChessMateRoute.HOME,
        selectedIcon = Icons.Default.Home,
        unselectedIcon = Icons.Outlined.Home,
        iconTextId = R.string.tab_home
    ),
    ChessMateTopLevelDestination(
        route = ChessMateRoute.SCAN,
        selectedIcon = Icons.Default.Camera,
        unselectedIcon = Icons.Outlined.Camera,
        iconTextId = R.string.tab_scan
    ),
    ChessMateTopLevelDestination(
        route = ChessMateRoute.PROFILE,
        selectedIcon = Icons.Default.Person,
        unselectedIcon = Icons.Outlined.Person,
        iconTextId = R.string.profile
    )
)

val LOGIN_LEVEL_DESTINATIONS = listOf(
    ChessMateTopLevelDestination(
        route = ChessMateRoute.SIGN_IN,
        selectedIcon = Icons.AutoMirrored.Filled.Login,
        unselectedIcon = Icons.AutoMirrored.Outlined.Login,
        iconTextId = R.string.tab_sign_in
    ),
    ChessMateTopLevelDestination(
        route = ChessMateRoute.SIGN_UP,
        selectedIcon = Icons.Default.AppRegistration,
        unselectedIcon = Icons.Outlined.AppRegistration,
        iconTextId = R.string.tab_sign_up
    ),
    ChessMateTopLevelDestination(
        route = ChessMateRoute.CONTACT,
        selectedIcon = Icons.Default.Email,
        unselectedIcon = Icons.Outlined.Email,
        iconTextId = R.string.tab_contacs
)
)

