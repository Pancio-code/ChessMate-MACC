package com.example.chessmate.ui.pages.profile
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.example.chessmate.sign_in.AuthUIClient
import com.example.chessmate.sign_in.SignInViewModel
import com.example.chessmate.sign_in.UserData
import com.example.chessmate.ui.utils.ChessMateNavigationType

@Composable
fun ProfileScreen(
    userData: UserData?,
    authHandler: AuthUIClient? = null,
    modifier: Modifier = Modifier,
    navigationType: ChessMateNavigationType,
    authViewModel: SignInViewModel? = null,
) {
    var isEditMode by remember { mutableStateOf(false) }

    if (!isEditMode) {
        ProfileReadMode(
            userData = userData,
            navigationType = navigationType,
            modifier = modifier,
            authHandler = authHandler,
            authViewModel = authViewModel,
            toggler = { isEditMode = !isEditMode }
        )
    } else {
        ProfileEditMode(
            userData = userData,
            navigationType = navigationType,
            modifier = modifier,
            authHandler = authHandler,
            toggler = { isEditMode = !isEditMode }
        )
    }
}

@Preview
@Composable
fun ProfilePagePreview() {
    ProfileScreen(
        userData = UserData(
            userId = "1",
            profilePictureUrl = null,
            username = "Andrea",
            email = "andrea.pancio00@gmail.com",
            emailVerified = false,
            provider = null
        ),
        modifier = Modifier,
        authHandler = null,
        navigationType = ChessMateNavigationType.BOTTOM_NAVIGATION
    )
}

@Preview
@Composable
fun ProfilePageTabletPreview() {
    ProfileScreen(
        userData = UserData(
            userId = "1",
            profilePictureUrl = null,
            username = "Andrea",
            email = "andrea.pancio00@gmail.com",
            emailVerified = false,
            provider = null
        ),
        modifier = Modifier,
        authHandler = null,
        navigationType = ChessMateNavigationType.NAVIGATION_RAIL
    )
}