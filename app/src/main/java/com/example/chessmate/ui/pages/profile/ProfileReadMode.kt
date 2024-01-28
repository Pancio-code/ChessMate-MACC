package com.example.chessmate.ui.pages.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import com.example.chessmate.BuildConfig
import com.example.chessmate.sign_in.AuthUIClient
import com.example.chessmate.sign_in.SignInViewModel
import com.example.chessmate.sign_in.UserData
import com.example.chessmate.ui.components.CardProfile
import com.example.chessmate.ui.components.Match
import com.example.chessmate.ui.components.RecentMatches
import com.example.chessmate.ui.components.Score
import com.example.chessmate.ui.utils.ChessMateNavigationType
import kotlinx.coroutines.launch


@Composable
fun ProfileReadMode(
    modifier: Modifier = Modifier,
    userData: UserData?,
    recentMatches: Array<Match>,
    authHandler: AuthUIClient? = null,
    navigationType: ChessMateNavigationType,
    authViewModel: SignInViewModel? = null,
    toggler: () -> Unit,
    painter: AsyncImagePainter
) {
    val lyfescope = rememberCoroutineScope()
    val context = LocalContext.current
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(top = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Row{
            if(userData?.username != null) {
                CardProfile(userData = userData, toggler, painter)
            }
        }
        Row(
            modifier = Modifier.padding(start = 8.dp, top = 16.dp, end = 8.dp, bottom = 16.dp)
        ) {
            Score()
        }
        Row{
            RecentMatches(matchList = recentMatches)
        }
        Row (
            modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Button(
                colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.primary),
                onClick = {
                lyfescope.launch {
                    val signInResult = authHandler?.signOut()
                    authViewModel?.onSignInResult(
                        signInResult!!,
                        context = context
                    )
                }
            }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                    modifier = Modifier.size(16.dp),
                    contentDescription = "Exit icon",
                    tint = MaterialTheme.colorScheme.onPrimary
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = "Sign Out", color = MaterialTheme.colorScheme.onPrimary)
            }
        }

    }
}

@Preview
@Composable
fun ProfileReadModePreview() {
    val userData = UserData(
        id = "1",
        profilePictureUrl = null,
        username = "Nome Cognome",
        email = "andrea.pancio00@gmail.com",
        emailVerified = false,
        provider = null
    )
    ProfileReadMode(
        userData = userData,
        modifier = Modifier,
        authHandler = null,
        navigationType = ChessMateNavigationType.BOTTOM_NAVIGATION,
        toggler = {  },
        recentMatches = arrayOf(
            Match(0,"avatar","Awenega",1),
            Match(1,"avatar","Username",0),
            Match(1,"avatar","Francesco Sudoso",0),
            Match(0,"avatar","Jhon Doe",1),
            Match(0,"avatar","Andrew Smith",0),
            Match(1,"avatar","Nome Cognome",1),
            Match(1,"avatar","Nome Cognome",1),
            Match(1,"avatar","Nome Cognome",1),
            Match(1,"avatar","Nome Cognome",1),
            Match(1,"avatar","Nome Cognome",1),
            Match(1,"avatar","Nome Cognome",1),
            ),
        painter = rememberAsyncImagePainter("${BuildConfig.API_URL}/api/v1/user/avatar/${userData.id}/${userData.profilePictureUrl}")
    )
}

@Preview
@Composable
fun ProfileReadModeTabletPreview() {
    val userData = UserData(
        id = "1",
        profilePictureUrl = null,
        username = "Nome Cognome",
        email = "andrea.pancio00@gmail.com",
        emailVerified = false,
        provider = null
    )
    ProfileReadMode(
        userData = userData,
        modifier = Modifier,
        authHandler = null,
        navigationType = ChessMateNavigationType.NAVIGATION_RAIL,
        toggler = { },
        recentMatches = arrayOf(
            Match(0,"avatar","Awenega",1),
            Match(1,"avatar","Username",0),
            Match(1,"avatar","Francesco Sudoso",0),
            Match(0,"avatar","Jhon Doe",1),
            Match(0,"avatar","Andrew Smith",0),
            Match(1,"avatar","Nome Cognome",1),
            Match(1,"avatar","Nome Cognome",1),
            Match(1,"avatar","Nome Cognome",1),
            Match(1,"avatar","Nome Cognome",1),
            Match(1,"avatar","Nome Cognome",1),
            ),
        painter = rememberAsyncImagePainter("${BuildConfig.API_URL}/api/v1/user/avatar/${userData.id}/${userData.profilePictureUrl}")
    )
}