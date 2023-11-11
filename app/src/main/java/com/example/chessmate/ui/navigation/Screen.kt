package com.example.chessmate.ui.navigation


sealed class Screen(val route: String) {
    object Home : Screen(route = "Home")
    object Profile : Screen(route = "Profile")
    object Camera : Screen(route = "Scan")
    object Settings : Screen(route = "Settings")
    object SignIn : Screen(route = "Sign In")
    object SignUp : Screen(route = "Sign Up")
}
