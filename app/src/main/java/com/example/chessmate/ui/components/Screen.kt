package com.example.chessmate.ui.components


sealed class Screen(val route: String) {
    object Home : Screen(route = "Home")
    object Profile : Screen(route = "Profile")
    object Camera : Screen(route = "Scan")
    object Settings : Screen(route = "settings")
}
