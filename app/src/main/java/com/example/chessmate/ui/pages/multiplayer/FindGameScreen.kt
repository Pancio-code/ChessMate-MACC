package com.example.chessmate.ui.pages.multiplayer

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.StopCircle
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.chessmate.R
import com.example.chessmate.multiplayer.OnlineUIClient
import com.example.chessmate.multiplayer.OnlineViewModel
import com.example.chessmate.sign_in.UserData
import com.example.chessmate.ui.components.ProfileImage
import com.example.chessmate.ui.navigation.ChessMateRoute
import com.example.chessmate.ui.utils.ChessMateNavigationType
import kotlinx.coroutines.launch

@Composable
fun FindGameScreen(
    modifier: Modifier = Modifier,
    navigationType: ChessMateNavigationType,
    onlineUIClient: OnlineUIClient,
    onlineViewModel: OnlineViewModel,
    togglefullView: () -> Unit = {},
    userData: UserData?
) {
    val roomData by onlineViewModel.roomData.collectAsState()
    val lyfescope = rememberCoroutineScope()
    var isFindingGame by remember { mutableStateOf(false) }

    // Initiate the game search
    LaunchedEffect(isFindingGame) {
        if (isFindingGame) {
            lyfescope.launch {
                val roomId = onlineUIClient.startGame()
                Log.d("Find game", roomId.toString())
            }
        }
    }

    Column(
        modifier = modifier.fillMaxSize().background(MaterialTheme.colorScheme.inverseOnSurface),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            if (userData?.username != null) {
                ElevatedCard(
                    elevation = CardDefaults.cardElevation(
                        defaultElevation = 6.dp
                    ),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.secondary,
                    ),
                    modifier = modifier.fillMaxWidth(0.8f).padding(10.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(1f)
                            .padding(horizontal = 20.dp, vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Start
                    ) {
                        Column(
                            modifier = Modifier.padding(end=8.dp),
                            horizontalAlignment = Alignment.Start,
                            verticalArrangement = Arrangement.Center,
                        ){
                            ProfileImage(imageResourceId = R.drawable.profile_picture)
                        }
                        Column(
                            horizontalAlignment = Alignment.Start,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column{
                                    Text(
                                        text = userData.username,
                                        fontWeight = FontWeight.Medium,
                                        fontSize = 20.sp,
                                        color = MaterialTheme.colorScheme.onSecondary
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "Your elo rank: " + userData.eloRank.toString(),
                                fontWeight = FontWeight.Bold,
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onSecondary
                            )
                        }
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(20.dp))
        if (roomData.playerTwoId.isNullOrEmpty()) {
            if (!isFindingGame) {
                Button(
                    onClick = { isFindingGame = true }) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        modifier = Modifier.size(16.dp),
                        contentDescription = "Search icon"
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = "Find game")
                }
                Spacer(modifier = Modifier.height(10.dp))
                Button(
                    onClick = {
                        onlineViewModel.setFullViewPage("")
                        togglefullView()
                    },
                    colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.secondary)) {
                    Icon(
                        imageVector = Icons.Default.ChevronLeft,
                        modifier = Modifier.size(16.dp),
                        contentDescription = "Back icon",
                        tint = MaterialTheme.colorScheme.onSecondary
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = "Back home", color = MaterialTheme.colorScheme.onSecondary)
                }
            } else {
                CircularProgressIndicator()
                Text("Waiting for opponent...", style = MaterialTheme.typography.bodyLarge)

                Button(
                    onClick = {
                        isFindingGame = false
                    }) {
                    Icon(
                        imageVector = Icons.Default.StopCircle,
                        modifier = Modifier.size(16.dp),
                        contentDescription = "Stop icon"
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = "Stop search")
                }
            }
        } else {
            // An opponent has been found, navigate to the game screen
            LaunchedEffect(roomData.playerTwoId) {
               onlineViewModel.setFullViewPage(ChessMateRoute.GAME)
            }
        }
    }
}