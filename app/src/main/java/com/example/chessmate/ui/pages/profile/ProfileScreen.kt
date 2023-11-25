package com.example.chessmate.ui.pages.profile
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.chessmate.sign_in.AuthUIClient
import com.example.chessmate.sign_in.SignInViewModel
import com.example.chessmate.sign_in.UserData
import com.example.chessmate.ui.components.Match
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
    val userDataStatic = UserData(
        userId = "1",
        profilePictureUrl = null,
        username = "Nome Cognome",
        email = "andrea.pancio00@gmail.com",
        emailVerified = false,
        provider = null
    )
    val recentMatches = arrayOf(
        Match(0,"avatar","Awenega",1),
        Match(1,"avatar","Username",0),
        Match(1,"avatar","Francesco Sudoso",0),
        Match(0,"avatar","Jhon Doe",1),
        Match(0,"avatar","Andrew Smith",0),
        Match(0,"avatar","PlaceHolder",0),
        Match(0,"avatar","Nome Cognome",0),
        Match(0,"avatar","Andrew Smith",0),
        Match(0,"avatar","Andrew Smith",0),
        Match(0,"avatar","Andrew Smith",0),
        Match(0,"avatar","Andrew Smith",0),
        Match(0,"avatar","Andrew Smith",0),
        Match(0,"avatar","Andrew Smith",0),
        Match(0,"avatar","Andrew Smith",0),
    )
    if (!isEditMode) {
        ProfileReadMode(
            userData = userDataStatic,
            navigationType = navigationType,
            modifier = modifier,
            authHandler = authHandler,
            authViewModel = authViewModel,
            toggler = { isEditMode = !isEditMode },
            recentMatches = recentMatches
        )
    } else {
        ProfileEditMode(
            userData = userData,
            navigationType = navigationType,
            modifier = modifier,
            authHandler = authHandler,
            toggler = { isEditMode = !isEditMode },
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
            username = "Nome Cognome",
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