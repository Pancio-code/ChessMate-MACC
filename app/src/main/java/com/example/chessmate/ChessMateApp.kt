package com.example.chessmate

import android.app.Activity
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
import androidx.compose.material3.rememberDrawerState
import androidx.compose.material3.windowsizeclass.WindowHeightSizeClass
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.window.layout.DisplayFeature
import androidx.window.layout.FoldingFeature
import com.example.chessmate.sign_in.AuthUIClient
import com.example.chessmate.sign_in.SignInState
import com.example.chessmate.sign_in.SignInViewModel
import com.example.chessmate.ui.navigation.ModalNavigationDrawerContent
import com.example.chessmate.ui.navigation.PermanentNavigationDrawerContent
import com.example.chessmate.ui.navigation.ChessMateBottomNavigationBar
import com.example.chessmate.ui.navigation.ChessMateNavigationActions
import com.example.chessmate.ui.navigation.ChessMateNavigationRail
import com.example.chessmate.ui.navigation.ChessMateRoute
import com.example.chessmate.ui.navigation.ChessMateTopLevelDestination
import com.example.chessmate.ui.pages.HomePage
import com.example.chessmate.ui.pages.ScreenUnderConstruction
import com.example.chessmate.ui.pages.SignInScreen
import com.example.chessmate.ui.pages.SignUpScreen
import com.example.chessmate.ui.utils.DevicePosture
import com.example.chessmate.ui.utils.ChessMateContentType
import com.example.chessmate.ui.utils.ChessMateNavigationContentPosition
import com.example.chessmate.ui.utils.ChessMateNavigationType
import com.example.chessmate.ui.utils.isBookPosture
import com.example.chessmate.ui.utils.isSeparating
import kotlinx.coroutines.launch

@Composable
fun ChessMateApp(
    windowSize: WindowSizeClass,
    displayFeatures: List<DisplayFeature>,
    chessMateHomeUIState: ChessMateHomeUIState,
    isAuthenticated: Boolean,
    authHandler: AuthUIClient?,
    authViewModel:  SignInViewModel? = null,
    authState: SignInState? = null,
    googleIntentLaucher: ManagedActivityResultLauncher<IntentSenderRequest, ActivityResult>? = null
) {
    val navigationType: ChessMateNavigationType
    val contentType: ChessMateContentType

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
            contentType = ChessMateContentType.SINGLE_PANE
        }
        WindowWidthSizeClass.Medium -> {
            navigationType = ChessMateNavigationType.NAVIGATION_RAIL
            contentType = if (foldingDevicePosture != DevicePosture.NormalPosture) {
                ChessMateContentType.DUAL_PANE
            } else {
                ChessMateContentType.SINGLE_PANE
            }
        }
        WindowWidthSizeClass.Expanded -> {
            navigationType = if (foldingDevicePosture is DevicePosture.BookPosture) {
                ChessMateNavigationType.NAVIGATION_RAIL
            } else {
                ChessMateNavigationType.PERMANENT_NAVIGATION_DRAWER
            }
            contentType = ChessMateContentType.DUAL_PANE
        }
        else -> {
            navigationType = ChessMateNavigationType.BOTTOM_NAVIGATION
            contentType = ChessMateContentType.SINGLE_PANE
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

    ChessMateNavigationWrapper(
        navigationType = navigationType,
        contentType = contentType,
        displayFeatures = displayFeatures,
        navigationContentPosition = navigationContentPosition,
        chessMateHomeUIState = chessMateHomeUIState,
        isAuthenticated = isAuthenticated,
        authHandler = authHandler,
        authState = authState,
        authViewModel = authViewModel,
        googleIntentLaucher = googleIntentLaucher
    )
}

@Composable
private fun ChessMateNavigationWrapper(
    navigationType: ChessMateNavigationType,
    contentType: ChessMateContentType,
    displayFeatures: List<DisplayFeature>,
    navigationContentPosition: ChessMateNavigationContentPosition,
    chessMateHomeUIState: ChessMateHomeUIState,
    isAuthenticated: Boolean,
    authHandler: AuthUIClient? = null,
    authState: SignInState? = null,
    authViewModel: SignInViewModel? = null,
    googleIntentLaucher: ManagedActivityResultLauncher<IntentSenderRequest, ActivityResult>? = null
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
                contentType = contentType,
                displayFeatures = displayFeatures,
                navigationContentPosition = navigationContentPosition,
                chessMateHomeUIState = chessMateHomeUIState,
                navController = navController,
                selectedDestination = selectedDestination,
                navigateToTopLevelDestination = navigationActions::navigateTo,
                isAuthenticated = isAuthenticated,
                authHandler = authHandler,
                authState = authState,
                authViewModel = authViewModel,
                googleIntentLaucher = googleIntentLaucher
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
                contentType = contentType,
                displayFeatures = displayFeatures,
                navigationContentPosition = navigationContentPosition,
                chessMateHomeUIState = chessMateHomeUIState,
                navController = navController,
                selectedDestination = selectedDestination,
                navigateToTopLevelDestination = navigationActions::navigateTo,
                isAuthenticated = isAuthenticated,
                onDrawerClicked = {
                    scope.launch {
                        drawerState.open()
                    }
                },
                authHandler = authHandler,
                authState = authState,
                authViewModel = authViewModel,
                googleIntentLaucher = googleIntentLaucher
            )
        }
    }
}

@Composable
fun ChessMateAppContent(
    modifier: Modifier = Modifier,
    navigationType: ChessMateNavigationType,
    contentType: ChessMateContentType,
    displayFeatures: List<DisplayFeature>,
    navigationContentPosition: ChessMateNavigationContentPosition,
    chessMateHomeUIState: ChessMateHomeUIState,
    navController: NavHostController,
    selectedDestination: String,
    navigateToTopLevelDestination: (ChessMateTopLevelDestination) -> Unit,
    onDrawerClicked: () -> Unit = {},
    isAuthenticated: Boolean,
    authViewModel: SignInViewModel? = null,
    authHandler: AuthUIClient? = null,
    authState: SignInState? = null,
    googleIntentLaucher: ManagedActivityResultLauncher<IntentSenderRequest, ActivityResult>? = null
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
                navController = navController,
                contentType = contentType,
                displayFeatures = displayFeatures,
                chessMateHomeUIState = chessMateHomeUIState,
                navigationType = navigationType,
                modifier = Modifier.weight(1f),
                isAuthenticated = isAuthenticated,
                authHandler = authHandler,
                authState = authState,
                authViewModel = authViewModel,
                googleIntentLaucher = googleIntentLaucher
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
    navController: NavHostController,
    contentType: ChessMateContentType,
    displayFeatures: List<DisplayFeature>,
    chessMateHomeUIState: ChessMateHomeUIState,
    navigationType: ChessMateNavigationType,
    modifier: Modifier = Modifier,
    isAuthenticated: Boolean,
    authHandler: AuthUIClient? = null,
    authState: SignInState? = null,
    authViewModel: SignInViewModel? = null,
    googleIntentLaucher: ManagedActivityResultLauncher<IntentSenderRequest, ActivityResult>? = null
) {
    if (isAuthenticated) {
        NavHost(
            modifier = modifier,
            navController = navController,
            startDestination = ChessMateRoute.HOME,
        ) {
            composable(ChessMateRoute.HOME) {
                HomePage(modifier = modifier)
            }
            composable(ChessMateRoute.SCAN) {
                ScreenUnderConstruction()
            }
            composable(ChessMateRoute.PROFILE) {
                ScreenUnderConstruction()
            }
        }
    } else {
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
                   navigationType = navigationType
               )
            }
            composable(ChessMateRoute.SIGN_UP) {
                SignUpScreen(
                    state = authState,
                    modifier = modifier,
                    authHandler = authHandler,
                    authViewModel = authViewModel,
                    navigationType = navigationType
                )
            }
            composable(ChessMateRoute.CONTACT) {
                ScreenUnderConstruction()
            }
        }
    }
}
