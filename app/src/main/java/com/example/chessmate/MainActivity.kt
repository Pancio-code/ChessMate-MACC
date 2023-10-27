package com.example.chessmate
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.Scaffold
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.chessmate.ui.theme.ChessMateTheme
import com.google.android.gms.auth.api.identity.Identity
import com.example.chessmate.ui.pages.ProfileScreen
import com.example.chessmate.sign_in.GoogleAuthUiClient
import com.example.chessmate.sign_in.NavBarSignIn
import com.example.chessmate.sign_in.SignInScreen
import com.example.chessmate.sign_in.SignInViewModel
import com.example.chessmate.ui.components.ExitApplicationComponent
import com.example.chessmate.ui.components.Screen
import com.example.chessmate.ui.components.TopBarComponent
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

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
            ChessMateTheme {
                val windowSizeClass = calculateWindowSizeClass(this)
                val navController = rememberNavController()
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
                                    NavBarSignIn(navController)
                                }
                            ) { innerPadding ->
                                (NavHost(
                                        navController = navController,
                                        startDestination = Screen.SignIn.route
                                    ) {
                                        composable(route = Screen.SignIn.route) {
                                            val viewModel = viewModel<SignInViewModel>()
                                            val state by viewModel.state.collectAsStateWithLifecycle()

                                            LaunchedEffect(key1 = Unit) {
                                                if (googleAuthUiClient.getSignedInUser() != null) {
                                                    navController.navigate(Screen.Home.route)
                                                }
                                            }

                                            val launcher = rememberLauncherForActivityResult(
                                                contract = ActivityResultContracts.StartIntentSenderForResult(),
                                                onResult = { result ->
                                                    if (result.resultCode == RESULT_OK) {
                                                        lifecycleScope.launch {
                                                            val signInResult =
                                                                googleAuthUiClient.signInWithIntent(
                                                                    intent = result.data
                                                                        ?: return@launch
                                                                )
                                                            viewModel.onSignInResult(
                                                                signInResult
                                                            )
                                                        }
                                                    }
                                                }
                                            )

                                            LaunchedEffect(key1 = state.isSignInSuccessful) {
                                                if (state.isSignInSuccessful) {
                                                    Toast.makeText(
                                                        applicationContext,
                                                        "Sign in successful",
                                                        Toast.LENGTH_LONG
                                                    ).show()

                                                    navController.navigate(Screen.Home.route)
                                                    viewModel.resetState()
                                                }
                                            }

                                            SignInScreen(
                                                state = state,
                                                onSignInClick = {
                                                    lifecycleScope.launch {
                                                        val signInIntentSender =
                                                            googleAuthUiClient.signIn()
                                                        launcher.launch(
                                                            IntentSenderRequest.Builder(
                                                                signInIntentSender
                                                                    ?: return@launch
                                                            ).build()
                                                        )
                                                    }
                                                }
                                            )
                                        }
                                    composable(Screen.SignUp.route) {
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

                                                    navController.popBackStack()
                                                }
                                            }
                                        )
                                    }
                                        composable(Screen.Home.route) {
                                            val context = LocalContext.current // Get the current context

                                            // Create an Intent to start the game activity
                                            val gameIntent = Intent(context, GameActivity::class.java)

                                            // Start the game activity
                                            context.startActivity(gameIntent)
                                        }
                                    }
                                )
                            }
                        }
                    }
            }
        }
    }
}

