package com.example.chessmate.ui.pages.multiplayer

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.StopCircle
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.chessmate.multiplayer.OnlineUIClient
import com.example.chessmate.multiplayer.OnlineViewModel
import com.example.chessmate.multiplayer.RoomData
import com.example.chessmate.multiplayer.RoomStatus
import com.example.chessmate.sign_in.UserData
import com.example.chessmate.sign_in.UserDataHelper
import com.example.chessmate.ui.components.CardProfileSearch
import com.example.chessmate.ui.navigation.ChessMateRoute
import kotlinx.coroutines.launch

@Composable
fun FindGameScreen(
    modifier: Modifier = Modifier,
    onlineUIClient: OnlineUIClient,
    onlineViewModel: OnlineViewModel,
    toggleFullView: () -> Unit = {},
    userData: UserData?
) {

    val roomData by onlineViewModel.roomData.collectAsState()
    val lifeScope = rememberCoroutineScope()
    var isFindingGame by remember { mutableStateOf(false) }
    val painter = rememberAsyncImagePainter("${UserDataHelper.AVATAR_URL}/${userData!!.id}/${userData.profilePictureUrl}")

    LaunchedEffect(isFindingGame) {
        if (isFindingGame) {
            lifeScope.launch {
                onlineUIClient.startGame().run {
                    isFindingGame = !isFindingGame
                }
            }
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.inverseOnSurface),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            CardProfileSearch(userData = userData,modifier=modifier, painter = painter)
        }
        Spacer(modifier = Modifier.height(20.dp))
        when(roomData.gameState) {
            RoomStatus.WAITING -> {
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
                        onlineViewModel.setRoomData(RoomData())
                        toggleFullView()
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
            }
            RoomStatus.CREATED -> {
                CircularProgressIndicator(color = MaterialTheme.colorScheme.onSurface)
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    "Waiting for opponent...",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = {
                        onlineUIClient.deleteRoomData(roomData)
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
            RoomStatus.JOINED -> {
                val painterTwo = rememberAsyncImagePainter("${UserDataHelper.AVATAR_URL}/${if(userData.id == roomData.playerOneId) roomData.playerTwoId else roomData.playerOneId}/${if(userData.id == roomData.playerOneId) roomData.pictureUrlTwo else roomData.pictureUrlOne}")
                Text(
                    "VS",
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(20.dp))
                CardProfileSearch(userData = UserData(username = if(userData.id == roomData.playerOneId) roomData.playerTwoUsername else roomData.playerOneUsername), modifier = modifier, painter = painterTwo)
                Spacer(modifier = Modifier.height(20.dp))
                Button(
                    onClick = {
                        roomData.let {
                            onlineUIClient.updateRoomData(model = it.copy(gameState = RoomStatus.INPROGRESS))
                        }
                        onlineViewModel.setFullViewPage(ChessMateRoute.ONLINE_GAME)
                        toggleFullView()
                    },
                    colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.primary)) {
                    Text(text = "Start Game", color = MaterialTheme.colorScheme.onPrimary)
                    Spacer(modifier = Modifier.width(8.dp))
                    Icon(
                        imageVector = Icons.Default.ChevronRight,
                        modifier = Modifier.size(16.dp),
                        contentDescription = "Enter icon",
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                }
                Spacer(modifier = Modifier.height(20.dp))
                Button(
                    onClick = {
                        onlineUIClient.deleteRoomData(roomData)
                        isFindingGame = false
                    },
                    colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.errorContainer)) {
                    Icon(
                        imageVector = Icons.Default.ChevronLeft,
                        modifier = Modifier.size(16.dp),
                        contentDescription = "Exit icon",
                        tint = MaterialTheme.colorScheme.onErrorContainer
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = "Exit Game", color = MaterialTheme.colorScheme.onErrorContainer)
                }
            }
            RoomStatus.INPROGRESS -> {
                onlineViewModel.setFullViewPage(ChessMateRoute.ONLINE_GAME)
                toggleFullView()
            }
            RoomStatus.FINISHED -> {
                onlineUIClient.deleteRoomData(roomData)
                isFindingGame = false
            }
        }
    }
}