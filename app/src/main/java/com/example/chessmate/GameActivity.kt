package com.example.chessmate
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Scaffold
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.chessmate.sign_in.GoogleAuthUiClient
import com.example.chessmate.ui.components.ExitApplicationComponent
import com.example.chessmate.ui.components.NavBarComponent
import com.example.chessmate.ui.components.Screen
import com.example.chessmate.ui.components.TopBarComponent
import com.example.chessmate.ui.pages.MainPage
import com.example.chessmate.ui.pages.ProfileScreen
import com.example.chessmate.ui.theme.ChessMateTheme
import com.google.android.gms.auth.api.identity.Identity
import kotlinx.coroutines.launch

class GameActivity : ComponentActivity() {
    private val googleAuthUiClient by lazy {
        GoogleAuthUiClient(
            context = applicationContext,
            oneTapClient = Identity.getSignInClient(applicationContext)
        )
    }

    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val navController = rememberNavController()
            val windowSizeClass = calculateWindowSizeClass(this)

            ChessMateTheme {
                when (windowSizeClass.widthSizeClass) {
                    WindowWidthSizeClass.Expanded -> {
                        // orientation is landscape in most devices including foldables (width 840dp+)
                    }

                    WindowWidthSizeClass.Medium -> {
                        // Most tablets are in landscape, larger unfolded inner displays in portrait (width 600dp+)
                    }

                    WindowWidthSizeClass.Compact -> {
                        ExitApplicationComponent()
                        Scaffold(
                            topBar = {
                                TopBarComponent()
                            },
                            bottomBar = {
                                NavBarComponent(navController)
                            }
                        ) { innerPadding ->
                            (
                                    NavHost(
                                        navController = navController,
                                        startDestination = Screen.Home.route
                                    ) {
                                        composable(route = Screen.Home.route) {
                                            MainPage(
                                                windowSizeClass.widthSizeClass,
                                                navController = navController,
                                                innerPadding
                                            )
                                        }
                                        composable(route = Screen.Profile.route) {
                                            val context = LocalContext.current
                                            ProfileScreen(
                                                userData = googleAuthUiClient.getSignedInUser(),
                                                onSignOut = {
                                                    lifecycleScope.launch {
                                                        googleAuthUiClient.signOut()
                                                        Toast.makeText(
                                                            applicationContext,
                                                            "Signed out",
                                                            Toast.LENGTH_LONG
                                                        ).show()


                                                        // Create an Intent to start the game activity
                                                        val gameIntent = Intent(context, MainActivity::class.java)

                                                        // Start the game activity
                                                        context.startActivity(gameIntent)
                                                    }
                                                }
                                            )
                                        }
                                        composable(route = Screen.Camera.route) { }
                                        composable(route = Screen.Settings.route) { }
                                    }
                                    )
                        }
                    }
                }
            }
        }
    }
}


