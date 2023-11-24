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
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.chessmate.sign_in.AuthUIClient
import com.example.chessmate.sign_in.SignInViewModel
import com.example.chessmate.sign_in.UserData
import com.example.chessmate.ui.utils.ChessMateNavigationType
import kotlinx.coroutines.launch
import com.example.chessmate.ui.components.CardProfile
import com.example.chessmate.ui.components.Score

@Composable
fun ProfileReadMode(
    modifier: Modifier = Modifier,
    userData: UserData?,
    authHandler: AuthUIClient? = null,
    navigationType: ChessMateNavigationType,
    authViewModel: SignInViewModel? = null,
    toggler: () -> Unit
) {
    val lyfescope = rememberCoroutineScope()
    Column(
        modifier = modifier
            .fillMaxSize().padding(top = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Row{
            if(userData?.username != null) {
                CardProfile(userData = userData, toggler)
            }
        }
        Row(
            modifier = Modifier.padding(start = 8.dp, top = 16.dp, end = 8.dp, bottom = 8.dp)
        ) {
            Score()
        }
        Row (
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Button(onClick = {
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
                    contentDescription = "Exit icon"
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = "Sign Out")
            }
        }

    }
}

@Preview
@Composable
fun ProfileReadModePreview() {
    ProfileReadMode(
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
        navigationType = ChessMateNavigationType.BOTTOM_NAVIGATION,
        toggler = {  }
    )
}

@Preview
@Composable
fun ProfileReadModeTabletPreview() {
    ProfileReadMode(
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
        navigationType = ChessMateNavigationType.NAVIGATION_RAIL,
        toggler = { }
    )
}