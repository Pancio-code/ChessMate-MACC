package com.example.chessmate
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.chessmate.sign_in.AuthUIClient
import com.example.chessmate.sign_in.SignInViewModel
import com.example.chessmate.sign_in.UserAuthStateType
import com.example.chessmate.ui.navigation.ExitApplicationComponent
import com.example.chessmate.ui.theme.ChessMateTheme
import com.example.chessmate.ui.utils.PreferencesManagerHelper
import com.facebook.FacebookSdk
import com.google.accompanist.adaptive.calculateDisplayFeatures
import com.google.android.gms.auth.api.identity.Identity
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.ksp.generated.defaultModule

class MainActivity : ComponentActivity() {

    private val signInViewModel: SignInViewModel by viewModels()
    private var userAuthState =  mutableStateOf(UserAuthStateType.UNAUTHENTICATED)
    private val db = Firebase.firestore
    private val preferencesManager by lazy {
        PreferencesManagerHelper(applicationContext)
    }


    private val authUIClient by lazy {
        AuthUIClient(
            context = applicationContext,
            oneTapClient = Identity.getSignInClient(applicationContext),
            db = db
        )
    }

    private fun initKoin() {
        startKoin {
            androidLogger()
            androidContext(this@MainActivity)
            modules(defaultModule)
        }
    }


    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initKoin()
        FacebookSdk.sdkInitialize(applicationContext);

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                signInViewModel.getAuthenticationState(handler = authUIClient).run {
                    signInViewModel.isAuthenticated.collect {  userAuthState.value = it.state }
                }
            }
        }

        setContent {
            ChessMateTheme {
                val windowSize = calculateWindowSizeClass(this)
                val displayFeatures = calculateDisplayFeatures(this)
                val authState by signInViewModel.signInState.collectAsStateWithLifecycle()

                ExitApplicationComponent(this)
                when (userAuthState.value) {
                    UserAuthStateType.UNAUTHENTICATED -> {

                        val launcher = rememberLauncherForActivityResult(
                            contract = ActivityResultContracts.StartIntentSenderForResult(),
                            onResult = { result ->
                                if (result.resultCode == RESULT_OK) {
                                    lifecycleScope.launch {
                                        val signInResult = authUIClient.signInWithIntent(
                                            intent = result.data ?: return@launch
                                        )
                                        signInViewModel.onSignInResult(
                                            signInResult
                                        )
                                    }
                                }
                            }
                        )

                        LaunchedEffect(key1 = authState.isSignInSuccessful) {
                            if (authState.isSignInSuccessful) {
                                Toast.makeText(
                                    applicationContext,
                                    "Sign in successful",
                                    Toast.LENGTH_LONG
                                ).show()
                                signInViewModel.getAuthenticationState(handler = authUIClient).run {
                                    signInViewModel.isAuthenticated.collect {  userAuthState.value = it.state }
                                }
                            }
                        }

                        ChessMateApp(
                            chessMateHomeUIState = null,
                            windowSize = windowSize,
                            displayFeatures = displayFeatures,
                            isAuthenticated = false,
                            authViewModel = signInViewModel,
                            authHandler = authUIClient,
                            authState= authState,
                            googleIntentLaucher = launcher
                        )
                    }

                    UserAuthStateType.AUTHENTICATED -> {
                        LaunchedEffect(key1 = authState.isSignInSuccessful) {
                            if (!authState.isSignInSuccessful) {
                                Toast.makeText(
                                    applicationContext,
                                    "Sign out successful",
                                    Toast.LENGTH_LONG
                                ).show()
                                signInViewModel.getAuthenticationState(handler = authUIClient).run {
                                    signInViewModel.isAuthenticated.collect {  userAuthState.value = it.state }
                                }
                            }
                        }

                        ChessMateApp(
                            chessMateHomeUIState = null,
                            windowSize = windowSize,
                            authState= authState,
                            displayFeatures = displayFeatures,
                            authViewModel = signInViewModel,
                            isAuthenticated = true,
                            authHandler = authUIClient
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Preview(showBackground = true)
@Composable
fun ChessMateAppPreview() {
    ChessMateTheme {
        ChessMateApp(
            chessMateHomeUIState = ChessMateHomeUIState(),
            windowSize = WindowSizeClass.calculateFromSize(DpSize(400.dp, 900.dp)),
            displayFeatures = emptyList(),
            isAuthenticated = true,
            authHandler = null
        )
    }
}

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Preview(showBackground = true, widthDp = 700, heightDp = 500)
@Composable
fun ChessMateAppPreviewTablet() {
    ChessMateTheme {
        ChessMateApp(
            chessMateHomeUIState = ChessMateHomeUIState(),
            windowSize = WindowSizeClass.calculateFromSize(DpSize(700.dp, 500.dp)),
            displayFeatures = emptyList(),
            isAuthenticated = true,
            authHandler = null
        )
    }
}

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Preview(showBackground = true, widthDp = 500, heightDp = 700)
@Composable
fun ChessMateAppPreviewTabletPortrait() {
    ChessMateTheme {
        ChessMateApp(
            chessMateHomeUIState = ChessMateHomeUIState(),
            windowSize = WindowSizeClass.calculateFromSize(DpSize(500.dp, 700.dp)),
            displayFeatures = emptyList(),
            isAuthenticated = true,
            authHandler = null
        )
    }
}

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Preview(showBackground = true, widthDp = 1100, heightDp = 600)
@Composable
fun ChessMateAppPreviewDesktop() {
    ChessMateTheme {
        ChessMateApp(
            chessMateHomeUIState = ChessMateHomeUIState(),
            windowSize = WindowSizeClass.calculateFromSize(DpSize(1100.dp, 600.dp)),
            displayFeatures = emptyList(),
            isAuthenticated = true,
            authHandler = null
        )
    }
}

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Preview(showBackground = true, widthDp = 600, heightDp = 1100)
@Composable
fun ChessMateAppPreviewDesktopPortrait() {
    ChessMateTheme {
        ChessMateApp(
            chessMateHomeUIState = ChessMateHomeUIState(),
            windowSize = WindowSizeClass.calculateFromSize(DpSize(600.dp, 1100.dp)),
            displayFeatures = emptyList(),
            isAuthenticated = true,
            authHandler = null
        )
    }
}

