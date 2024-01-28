package com.example.chessmate.ui.pages

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.RestartAlt
import androidx.compose.material.icons.filled.SmartToy
import androidx.compose.material.icons.filled.WifiOff
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.chessmate.R
import com.example.chessmate.multiplayer.OnlineUIClient
import com.example.chessmate.multiplayer.OnlineViewModel
import com.example.chessmate.sign_in.UserData
import com.example.chessmate.ui.navigation.ChessMateRoute
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch

@Composable

fun HomePage(
    modifier: Modifier = Modifier,
    onlineViewModel: OnlineViewModel,
    togglefullView: () -> Unit = {},
    onlineUIClient: OnlineUIClient,
) {
    val scroll = rememberScrollState(0)
    val localContext = LocalContext.current
    val resumeGameError =  stringResource(id = R.string.resume_game)
    val lyfescope = rememberCoroutineScope()
    Column(
        modifier = modifier.fillMaxSize().verticalScroll(scroll),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_chessmate_foreground),
            contentDescription = stringResource(id = R.string.logo),
            modifier = Modifier.size(200.dp),
            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            modifier = Modifier.padding(12.dp),
            text = stringResource(id = R.string.app_name),
            style = MaterialTheme.typography.titleLarge,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
                onlineViewModel.setFullViewPage(ChessMateRoute.FIND_GAME)
                togglefullView()
            },
            modifier = Modifier
                .fillMaxWidth(fraction = 0.6f)
                .height(65.dp)
        ) {
            Icon(
                imageVector = Icons.Default.People,
                modifier = Modifier.size(16.dp),
                contentDescription = "Players icon"
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = "Play 1vs1 online")
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
                onlineViewModel.setFullViewPage(ChessMateRoute.OFFLINE_GAME)
                togglefullView()
              },
            modifier = Modifier
                .fillMaxWidth(fraction = 0.6f)
                .height(65.dp)
        ) {
            Icon(
                imageVector = Icons.Default.WifiOff,
                modifier = Modifier.size(16.dp),
                contentDescription = "Offline icon"
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = "Play 1vs1 offline")
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
                onlineViewModel.setFullViewPage(ChessMateRoute.SELECT_COLOR)
                togglefullView()
            },
            modifier = Modifier
                .fillMaxWidth(fraction = 0.6f)
                .height(65.dp)
        ) {
            Icon(
                imageVector = Icons.Default.SmartToy,
                modifier = Modifier.size(16.dp),
                contentDescription = "Robot icon"
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = "Play against AI")
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
                lyfescope.launch {
                    if (onlineUIClient.getRoom() != null) {
                        onlineViewModel.setFullViewPage(ChessMateRoute.ONLINE_GAME)
                        togglefullView()
                    } else {
                        Toast.makeText(
                            localContext,
                            resumeGameError,
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth(fraction = 0.6f)
                .height(65.dp)
        ) {
            Icon(
                imageVector = Icons.Default.RestartAlt,
                modifier = Modifier.size(16.dp),
                contentDescription = "Restart icon"
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = "Resume online game")
        }
    }
}

@Preview
@Composable
fun HomePagePreview() {
    HomePage(onlineViewModel = OnlineViewModel(), onlineUIClient = OnlineUIClient(context = LocalContext.current,
        FirebaseFirestore.getInstance(), onlineViewModel = OnlineViewModel(), UserData()
    ))
}

