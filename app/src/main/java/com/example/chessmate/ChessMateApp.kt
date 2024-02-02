package com.example.chessmate

import android.content.Context
import android.util.Log
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.result.ActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.PermanentNavigationDrawer
import androidx.compose.material3.Surface
import androidx.compose.material3.rememberDrawerState
import androidx.compose.material3.windowsizeclass.WindowHeightSizeClass
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.window.layout.DisplayFeature
import androidx.window.layout.FoldingFeature
import com.example.chessmate.game.ui.app.Game
import com.example.chessmate.matches.MatchesViewModel
import com.example.chessmate.multiplayer.GameType
import com.example.chessmate.multiplayer.OnlineUIClient
import com.example.chessmate.multiplayer.OnlineViewModel
import com.example.chessmate.sign_in.AuthUIClient
import com.example.chessmate.sign_in.SignInState
import com.example.chessmate.sign_in.SignInViewModel
import com.example.chessmate.sign_in.UserData
import com.example.chessmate.ui.navigation.ChessMateBottomNavigationBar
import com.example.chessmate.ui.navigation.ChessMateNavigationActions
import com.example.chessmate.ui.navigation.ChessMateNavigationRail
import com.example.chessmate.ui.navigation.ChessMateRoute
import com.example.chessmate.ui.navigation.ChessMateTopLevelDestination
import com.example.chessmate.ui.navigation.ModalNavigationDrawerContent
import com.example.chessmate.ui.navigation.PermanentNavigationDrawerContent
import com.example.chessmate.ui.pages.ChessboardParser
import com.example.chessmate.ui.pages.ContactUsScreen
import com.example.chessmate.ui.pages.HomePage
import com.example.chessmate.ui.pages.HomePageGuest
import com.example.chessmate.ui.pages.SignInScreen
import com.example.chessmate.ui.pages.SignUpScreen
import com.example.chessmate.ui.pages.multiplayer.FindGameScreen
import com.example.chessmate.ui.pages.multiplayer.SelectOptionsScreen
import com.example.chessmate.ui.pages.profile.ProfileScreen
import com.example.chessmate.ui.pages.profile.ProfileScreenGuest
import com.example.chessmate.ui.utils.ChessMateNavigationContentPosition
import com.example.chessmate.ui.utils.ChessMateNavigationType
import com.example.chessmate.ui.utils.DevicePosture
import com.example.chessmate.ui.utils.isBookPosture
import com.example.chessmate.ui.utils.isSeparating
import kotlinx.coroutines.launch

@Composable
fun ChessMateApp(
    windowSize: WindowSizeClass,
    displayFeatures: List<DisplayFeature>,
    isAuthenticated: Boolean,
    authState: SignInState? = null,
    authHandler: AuthUIClient?,
    authViewModel:  SignInViewModel? = null,
    onlineViewModel: OnlineViewModel? = null,
    matchesViewModel: MatchesViewModel? = null,
    googleIntentLaucher: ManagedActivityResultLauncher<IntentSenderRequest, ActivityResult>? = null,
    toggleFullView: () -> Unit = {},
    onlineUIClient: OnlineUIClient? = null,
    immersivePage: String? = null,
    userData: UserData? = null,
    context: Context? = null
) {
    val navigationType: ChessMateNavigationType

    val foldingFeature = displayFeatures.filterIsInstance<FoldingFeature>().firstOrNull()

    val foldingDevicePosture = when {
        isBookPosture(foldingFeature) ->
            DevicePosture.BookPosture(foldingFeature.bounds)

        isSeparating(foldingFeature) ->
            DevicePosture.Separating(foldingFeature.bounds, foldingFeature.orientation)

        else -> DevicePosture.NormalPosture
    }

    when (windowSize.widthSizeClass) {
        WindowWidthSizeClass.Compact -> {
            navigationType = ChessMateNavigationType.BOTTOM_NAVIGATION
        }
        WindowWidthSizeClass.Medium -> {
            navigationType = ChessMateNavigationType.NAVIGATION_RAIL
        }
        WindowWidthSizeClass.Expanded -> {
            navigationType = if (foldingDevicePosture is DevicePosture.BookPosture) {
                ChessMateNavigationType.NAVIGATION_RAIL
            } else {
                ChessMateNavigationType.PERMANENT_NAVIGATION_DRAWER
            }
        }
        else -> {
            navigationType = ChessMateNavigationType.BOTTOM_NAVIGATION
        }
    }
    val navigationContentPosition = when (windowSize.heightSizeClass) {
        WindowHeightSizeClass.Compact -> {
            ChessMateNavigationContentPosition.TOP
        }
        WindowHeightSizeClass.Medium,
        WindowHeightSizeClass.Expanded -> {
            ChessMateNavigationContentPosition.CENTER
        }
        else -> {
            ChessMateNavigationContentPosition.TOP
        }
    }

    when (immersivePage) {
        ChessMateRoute.FIND_GAME -> FindGameScreen(
            modifier = Modifier,
            onlineUIClient = onlineUIClient!!,
            onlineViewModel = onlineViewModel!!,
            toggleFullView = toggleFullView,
            userData = userData
        )
        ChessMateRoute.OFFLINE_GAME -> Surface(color = MaterialTheme.colorScheme.background) {
            val importedFen = onlineViewModel!!.getImportedFen()
            if (importedFen == "") {
                Game(
                    gameType = GameType.TWO_OFFLINE,
                    onlineViewModel = onlineViewModel,
                    matchesViewModel = matchesViewModel,
                    signInViewModel = authViewModel,
                    toggleFullView = toggleFullView,
                    userData = userData
                )
            } else {
                Game(
                    gameType = GameType.TWO_OFFLINE,
                    onlineViewModel = onlineViewModel,
                    matchesViewModel = matchesViewModel,
                    signInViewModel = authViewModel,
                    toggleFullView = toggleFullView,
                    importGameFEN = onlineViewModel.getImportedFen(),
                    userData = userData
                )
            }
        }
        ChessMateRoute.SELECT_COLOR -> Surface(color = MaterialTheme.colorScheme.background) {
            SelectOptionsScreen(
                onlineViewModel = onlineViewModel!!
            )
        }
        ChessMateRoute.ONLINE_GAME -> Surface(color = MaterialTheme.colorScheme.background) {
            val roomData = onlineViewModel!!.getRoomData()
            Log.d("ROOM",roomData.toString())
            Game(
                gameType = GameType.ONLINE,
                onlineViewModel = onlineViewModel,
                matchesViewModel = matchesViewModel,
                signInViewModel = authViewModel,
                onlineUIClient = onlineUIClient,
                authUIClient = authHandler,
                importGamePGN = roomData.boardState,
                toggleFullView = toggleFullView,
                userData = userData,
                importGameFEN = if (roomData.fen != "") roomData.fen else null,
                startColor = if (roomData.playerOneId == userData!!.id) com.example.chessmate.game.model.piece.Set.WHITE else com.example.chessmate.game.model.piece.Set.BLACK
            )
        }
        ChessMateRoute.AI_GAME -> Surface(color = MaterialTheme.colorScheme.background) {
            val importedFen = onlineViewModel!!.getImportedFen()
            if (importedFen == "") {
                Game(
                    gameType = GameType.ONE_OFFLINE,
                    onlineViewModel = onlineViewModel!!,
                    matchesViewModel = matchesViewModel,
                    signInViewModel = authViewModel,
                    toggleFullView = toggleFullView,
                    startColor = onlineViewModel.getStartColor(),
                    depth = onlineViewModel.getDepth(),
                    userData = userData
                )
            } else {
                Game(
                    gameType = GameType.ONE_OFFLINE,
                    onlineViewModel = onlineViewModel!!,
                    matchesViewModel = matchesViewModel,
                    signInViewModel = authViewModel,
                    toggleFullView = toggleFullView,
                    startColor = onlineViewModel.getStartColor(),
                    depth = onlineViewModel.getDepth(),
                    userData = userData,
                    importGameFEN = onlineViewModel.getImportedFen()
                )
            }
        }
        else -> ChessMateNavigationWrapper(
            navigationType = navigationType,
            navigationContentPosition = navigationContentPosition,
            isAuthenticated = isAuthenticated,
            authState = authState,
            authHandler = authHandler,
            authViewModel = authViewModel,
            onlineViewModel= onlineViewModel,
            matchesViewModel = matchesViewModel,
            onlineUIClient = onlineUIClient,
            googleIntentLaucher = googleIntentLaucher,
            toggleFullView = toggleFullView,
            context = context
        )
    }
}

@Composable
private fun ChessMateNavigationWrapper(
    navigationType: ChessMateNavigationType,
    navigationContentPosition: ChessMateNavigationContentPosition,
    isAuthenticated: Boolean,
    authState: SignInState? = null,
    authHandler: AuthUIClient? = null,
    authViewModel: SignInViewModel? = null,
    onlineViewModel: OnlineViewModel? = null,
    matchesViewModel: MatchesViewModel? = null,
    onlineUIClient: OnlineUIClient? = null,
    googleIntentLaucher: ManagedActivityResultLauncher<IntentSenderRequest, ActivityResult>? = null,
    toggleFullView: () -> Unit = {},
    context: Context? = null
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    val navController = rememberNavController()
    val navigationActions = remember(navController) {
        ChessMateNavigationActions(navController)
    }
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val selectedDestination =
        navBackStackEntry?.destination?.route ?: ChessMateRoute.HOME

    if (navigationType == ChessMateNavigationType.PERMANENT_NAVIGATION_DRAWER) {
        PermanentNavigationDrawer(drawerContent = {
            PermanentNavigationDrawerContent(
                selectedDestination = selectedDestination,
                navigationContentPosition = navigationContentPosition,
                navigateToTopLevelDestination = navigationActions::navigateTo,
                isAuthenticate = isAuthenticated
            )
        }) {
            ChessMateAppContent(
                navigationType = navigationType,
                navigationContentPosition = navigationContentPosition,
                navController = navController,
                selectedDestination = selectedDestination,
                authState = authState,
                navigateToTopLevelDestination = navigationActions::navigateTo,
                isAuthenticated = isAuthenticated,
                authViewModel = authViewModel,
                onlineViewModel= onlineViewModel,
                matchesViewModel= matchesViewModel,
                onlineUIClient = onlineUIClient,
                authHandler = authHandler,
                googleIntentLaucher = googleIntentLaucher,
                toggleFullView = toggleFullView,
                context = context
            )
        }
    } else {
        ModalNavigationDrawer(
            drawerContent = {
                ModalNavigationDrawerContent(
                    selectedDestination = selectedDestination,
                    navigationContentPosition = navigationContentPosition,
                    navigateToTopLevelDestination = navigationActions::navigateTo,
                    onDrawerClicked = {
                        scope.launch {
                            drawerState.close()
                        }
                    },
                    isAuthenticate = isAuthenticated
                )
            },
            drawerState = drawerState
        ) {
            ChessMateAppContent(
                navigationType = navigationType,
                navigationContentPosition = navigationContentPosition,
                navController = navController,
                selectedDestination = selectedDestination,
                authState= authState,
                navigateToTopLevelDestination = navigationActions::navigateTo,
                onDrawerClicked = {
                    scope.launch {
                        drawerState.open()
                    }
                },
                isAuthenticated = isAuthenticated,
                authViewModel = authViewModel,
                onlineViewModel = onlineViewModel,
                matchesViewModel = matchesViewModel,
                onlineUIClient = onlineUIClient,
                authHandler = authHandler,
                googleIntentLaucher = googleIntentLaucher,
                toggleFullView = toggleFullView,
                context = context
            )
        }
    }
}

@Composable
fun ChessMateAppContent(
    modifier: Modifier = Modifier,
    navigationType: ChessMateNavigationType,
    navigationContentPosition: ChessMateNavigationContentPosition,
    navController: NavHostController,
    selectedDestination: String,
    authState: SignInState? = null,
    navigateToTopLevelDestination: (ChessMateTopLevelDestination) -> Unit,
    onDrawerClicked: () -> Unit = {},
    isAuthenticated: Boolean,
    authViewModel: SignInViewModel? = null,
    onlineViewModel: OnlineViewModel? = null,
    matchesViewModel: MatchesViewModel? = null,
    onlineUIClient: OnlineUIClient? = null,
    authHandler: AuthUIClient? = null,
    googleIntentLaucher: ManagedActivityResultLauncher<IntentSenderRequest, ActivityResult>? = null,
    toggleFullView: () -> Unit = {},
    context: Context? = null
) {
    Row(modifier = modifier.fillMaxSize()) {
        AnimatedVisibility(visible = navigationType == ChessMateNavigationType.NAVIGATION_RAIL) {
            ChessMateNavigationRail(
                selectedDestination = selectedDestination,
                navigationContentPosition = navigationContentPosition,
                navigateToTopLevelDestination = navigateToTopLevelDestination,
                onDrawerClicked = onDrawerClicked,
                isAuthenticate = isAuthenticated
            )
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.inverseOnSurface)
        ) {
            ChessMateNavHost(
                modifier = Modifier.weight(1f),
                navController = navController,
                navigationType = navigationType,
                authState = authState,
                isAuthenticated = isAuthenticated,
                authHandler = authHandler,
                authViewModel = authViewModel,
                onlineViewModel = onlineViewModel,
                matchesViewModel = matchesViewModel,
                onlineUIClient = onlineUIClient,
                googleIntentLaucher = googleIntentLaucher,
                toggleFullView = toggleFullView,
                context = context
            )
            AnimatedVisibility(visible = navigationType == ChessMateNavigationType.BOTTOM_NAVIGATION) {
                ChessMateBottomNavigationBar(
                    selectedDestination = selectedDestination,
                    navigateToTopLevelDestination = navigateToTopLevelDestination,
                    isAuthenticated = isAuthenticated
                )
            }
        }
    }
}

@Composable
private fun ChessMateNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    navigationType: ChessMateNavigationType,
    authState: SignInState? = null,
    isAuthenticated: Boolean,
    authHandler: AuthUIClient? = null,
    authViewModel: SignInViewModel? = null,
    onlineViewModel: OnlineViewModel? = null,
    matchesViewModel: MatchesViewModel? = null,
    onlineUIClient: OnlineUIClient? = null,
    googleIntentLaucher: ManagedActivityResultLauncher<IntentSenderRequest, ActivityResult>? = null,
    toggleFullView: () -> Unit = {},
    context: Context? = null
) {
    if (isAuthenticated && authViewModel?.isGuest() != true) {
        NavHost(
            modifier = modifier,
            navController = navController,
            startDestination = ChessMateRoute.HOME,
        ) {
            composable(ChessMateRoute.HOME) {
                HomePage(
                    modifier = modifier,
                    onlineViewModel = onlineViewModel!!,
                    toggleFullView = toggleFullView,
                    onlineUIClient = onlineUIClient!!
                )
            }
            composable(ChessMateRoute.SCAN) {
                ChessboardParser(
                    onlineViewModel = onlineViewModel!!,
                    toggleFullView = toggleFullView
                )
            }
            composable(ChessMateRoute.PROFILE) {
                ProfileScreen(
                    modifier= modifier,
                    authViewModel = authViewModel,
                    matchesViewModel = matchesViewModel,
                    authHandler = authHandler,
                    navigationType = navigationType)
            }
        }
    } else if (authViewModel?.isGuest() != true) {
        NavHost(
            modifier = modifier,
            navController = navController,
            startDestination = ChessMateRoute.SIGN_IN,
        ) {
            composable(ChessMateRoute.SIGN_IN) {
               SignInScreen(
                   state = authState,
                   modifier = modifier,
                   authHandler = authHandler,
                   authViewModel = authViewModel,
                   googleIntentLaucher = googleIntentLaucher,
                   navController = navController,
                   navigationType = navigationType,
                   context = context
               )
            }
            composable(ChessMateRoute.SIGN_UP) {
                SignUpScreen(
                    state = authState,
                    modifier = modifier,
                    authHandler = authHandler,
                    authViewModel = authViewModel,
                    navigationType = navigationType,
                    context = context
                )
            }
            composable(ChessMateRoute.CONTACT) {
                ContactUsScreen(
                    state = authState,
                    modifier = modifier,
                    authHandler = authHandler,
                    authViewModel = authViewModel,
                    navigationType = navigationType
                )
            }
        }
    } else {
        NavHost(
            modifier = modifier,
            navController = navController,
            startDestination = ChessMateRoute.HOME,
        ) {
            composable(ChessMateRoute.HOME) {
                HomePageGuest(
                    modifier = modifier,
                    toggleFullView = toggleFullView,
                    onlineViewModel = onlineViewModel!!
                )
            }
            composable(ChessMateRoute.SCAN) {
                ChessboardParser(
                    onlineViewModel = onlineViewModel!!,
                    toggleFullView = toggleFullView
                )
            }
            composable(ChessMateRoute.PROFILE) {
                ProfileScreenGuest(
                    modifier= modifier,
                    authViewModel = authViewModel
                )
            }
        }
    }
}
