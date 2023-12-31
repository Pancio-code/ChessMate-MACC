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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.chessmate.sign_in.AuthUIClient
import com.example.chessmate.sign_in.SignInViewModel
import com.example.chessmate.sign_in.UserData
import com.example.chessmate.ui.utils.ChessMateNavigationType
import kotlinx.coroutines.launch
import com.example.chessmate.ui.components.CardProfile
import com.example.chessmate.ui.components.Match
import com.example.chessmate.ui.components.Score
import com.example.chessmate.ui.components.RecentMatches
import com.example.chessmate.ui.theme.light_primary


@Composable
fun ProfileReadMode(
    modifier: Modifier = Modifier,
    userData: UserData?,
    recentMatches: Array<Match>,
    authHandler: AuthUIClient? = null,
    navigationType: ChessMateNavigationType,
    authViewModel: SignInViewModel? = null,
    toggler: () -> Unit
) {
    val lyfescope = rememberCoroutineScope()
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(top = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Row{
            if(userData?.username != null) {
                CardProfile(userData = userData, toggler)
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
                colors = ButtonDefaults.buttonColors(light_primary),
                onClick = {
                lyfescope.launch {
                    val signInResult = authHandler?.signOut()
                    authViewModel?.onSignInResult(
                        signInResult!!
                    )
                }
            }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                    modifier = Modifier.size(16.dp),
                    contentDescription = "Exit icon",
                    tint = Color.LightGray
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = "Sign Out", color = Color.LightGray)
            }
        }

    }
}

@Preview
@Composable
fun ProfileReadModePreview() {
    ProfileReadMode(
        userData = UserData(
            id = "1",
            profilePictureUrl = null,
            username = "Nome Cognome",
            email = "andrea.pancio00@gmail.com",
            emailVerified = false,
            provider = null
        ),
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
            )
    )
}

@Preview
@Composable
fun ProfileReadModeTabletPreview() {
    ProfileReadMode(
        userData = UserData(
            id = "1",
            profilePictureUrl = null,
            username = "Nome Cognome",
            email = "andrea.pancio00@gmail.com",
            emailVerified = false,
            provider = null
        ),
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
            )
    )
}