package com.example.chessmate.ui.pages.profile
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.rememberAsyncImagePainter
import com.example.chessmate.sign_in.AuthUIClient
import com.example.chessmate.sign_in.SignInViewModel
import com.example.chessmate.sign_in.UserDataHelper
import com.example.chessmate.ui.components.Match
import com.example.chessmate.ui.utils.ChessMateNavigationType

@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier,
    authHandler: AuthUIClient? = null,
    navigationType: ChessMateNavigationType,
    authViewModel: SignInViewModel? = null,
) {
    val userData by authViewModel!!.userData.collectAsStateWithLifecycle()

    val painter = rememberAsyncImagePainter("${UserDataHelper.AVATAR_URL}/${userData.data!!.id}/${userData.data!!.profilePictureUrl}")
    var isEditMode by remember { mutableStateOf(false) }
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
            userData = userData.data,
            modifier = modifier,
            authHandler = authHandler,
            authViewModel = authViewModel,
            toggler = { isEditMode = !isEditMode },
            recentMatches = recentMatches,
            painter = painter
        )
    } else {
        ProfileEditMode(
            userData = userData.data,
            modifier = modifier,
            authHandler = authHandler,
            authViewModel = authViewModel,
            toggler = { isEditMode = !isEditMode },
            painter = painter
        )
    }
}

@Preview
@Composable
fun ProfilePagePreview() {
    ProfileScreen(
        modifier = Modifier,
        authHandler = null,
        navigationType = ChessMateNavigationType.BOTTOM_NAVIGATION
    )
}

@Preview
@Composable
fun ProfilePageTabletPreview() {
    ProfileScreen(
        modifier = Modifier,
        authHandler = null,
        navigationType = ChessMateNavigationType.NAVIGATION_RAIL
    )
}