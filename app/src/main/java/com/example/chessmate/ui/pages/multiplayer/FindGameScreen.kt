package com.example.chessmate.ui.pages.multiplayer

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.unit.dp
import com.example.chessmate.multiplayer.OnlineUIClient
import com.example.chessmate.multiplayer.OnlineViewModel
import com.example.chessmate.ui.navigation.ChessMateRoute
import com.example.chessmate.ui.utils.ChessMateNavigationType
import kotlinx.coroutines.launch

@Composable
fun FindGameScreen(
    modifier: Modifier = Modifier,
    navigationType: ChessMateNavigationType,
    onlineUIClient: OnlineUIClient,
    onlineViewModel: OnlineViewModel,
    togglefullView: () -> Unit = {}
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

    Column(modifier = modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
        Spacer(modifier = Modifier.height(20.dp))
        if (roomData.playerTwoId.isNullOrEmpty()) {
            if (!isFindingGame) {
                Button(onClick = { isFindingGame = true }) {
                    Text("Find game")
                }
            } else {
                CircularProgressIndicator()
                Text("Waiting for opponent...", style = MaterialTheme.typography.bodyLarge)
            }
        } else {
            // An opponent has been found, navigate to the game screen
            LaunchedEffect(roomData.playerTwoId) {
               onlineViewModel.setFullViewPage(ChessMateRoute.GAME)
            }
        }
    }
}