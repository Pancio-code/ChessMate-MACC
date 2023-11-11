package com.example.chessmate
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.annotation.RequiresApi
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import com.google.android.gms.auth.api.identity.Identity

class LoginActivity : ComponentActivity() {


    @RequiresApi(Build.VERSION_CODES.S)
    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        /*setContent {
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
                        ExitApplicationComponent(this)
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
                                            if (googleAuthUiClient.getSignedInUser() != null || emailAuthUiClient.getSignedInUser() != null) {
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
                                            onSignInWithGoogleClick = {
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
                                            },
                                            onSignInWithEmailClick = { email, password ->
                                                lifecycleScope.launch {
                                                    val signInResult = emailAuthUiClient.firebaseSignInWithEmailAndPassword(
                                                        email,
                                                        password
                                                    )
                                                    viewModel.onSignInResult(
                                                        signInResult
                                                    )
                                                }
                                            },
                                            navController = navController,
                                            innerPadding = innerPadding
                                        )
                                    }
                                composable(Screen.SignUp.route) {
                                    val viewModel = viewModel<SignInViewModel>()
                                    val state by viewModel.state.collectAsStateWithLifecycle()


                                    SignUpScreen(
                                        state = state,
                                        onSignInWithEmailClick = { email, password ->
                                            lifecycleScope.launch {
                                                val signInResult = emailAuthUiClient.firebaseSignUpWithEmailAndPassword(
                                                    email,
                                                    password
                                                )/*
                                                viewModel.onSignInResult(
                                                    signInResult
                                                )*/
                                            }
                                        },
                                        innerPadding = innerPadding
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
   */ }
}

