package com.example.chessmate.game.ui.app

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Layers
import androidx.compose.material.icons.filled.Loop
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.chessmate.R
import com.example.chessmate.game.model.data_chessmate.LocalActiveDatasetVisualisation
import com.example.chessmate.game.model.game.Resolution
import com.example.chessmate.game.model.game.controller.GameController
import com.example.chessmate.game.model.game.preset.Preset
import com.example.chessmate.game.model.game.speechParser.SpeechParser
import com.example.chessmate.game.model.game.state.GamePlayState
import com.example.chessmate.game.model.game.state.GameState
import com.example.chessmate.game.model.piece.Set
import com.example.chessmate.game.ui.chess.Board
import com.example.chessmate.game.ui.chess.CapturedPieces
import com.example.chessmate.game.ui.chess.Moves
import com.example.chessmate.game.ui.chess.resolutionText
import com.example.chessmate.matches.Match
import com.example.chessmate.matches.MatchesUIClient
import com.example.chessmate.matches.MatchesViewModel
import com.example.chessmate.multiplayer.GameType
import com.example.chessmate.multiplayer.OnlineUIClient
import com.example.chessmate.multiplayer.OnlineViewModel
import com.example.chessmate.sign_in.UserData
import com.example.chessmate.ui.pages.profile.generateRandomString

@Composable
fun Game(
    importGameFEN: String? = null,
    state: GamePlayState = if (importGameFEN != null) GamePlayState(stringFEN = importGameFEN) else GamePlayState(),
    importGamePGN: String? = null,
    preset: Preset? = null,
    gameType : GameType = GameType.TWO_OFFLINE,
    startColor : Set? = Set.WHITE,
    depth : Int = 5,
    toggleFullView: () -> Unit = {},
    onlineViewModel: OnlineViewModel,
    matchesViewModel: MatchesViewModel?,
    onlineUIClient: OnlineUIClient? = null,
    userData: UserData? = null
) {
    var isFlipped by rememberSaveable { mutableStateOf(!(startColor != null && startColor == Set.WHITE)) }
    val gamePlayState = rememberSaveable { mutableStateOf(state) }
    val showChessMateDialog = remember { mutableStateOf(false) }
    val showGameDialog = remember { mutableStateOf(false) }
    val showImportPgnDialog = remember { mutableStateOf(false) }
    val showImportFenDialog = remember { mutableStateOf(false) }
    val pngToImport = remember { mutableStateOf(importGamePGN) }
    val fenToImport = remember { mutableStateOf(importGameFEN) }
    val lifecycleOwner = LocalLifecycleOwner.current
    val roomData = onlineViewModel.roomData.collectAsState()

    val gameController = remember {
        GameController(
            getGamePlayState = { gamePlayState.value },
            setGamePlayState = { gamePlayState.value = it },
            preset = preset,
            startColor = startColor,
            gameType = gameType,
            roomData= roomData.value,
            onlineUIClient = onlineUIClient
        )
    }

    if (gameType == GameType.ONLINE) {
        LaunchedEffect(roomData.value) {
            Log.d("FEN",fenToImport.value.toString())
            if (gamePlayState.value.gameState.toMove != startColor && roomData.value.lastMove != null && fenToImport.value == null) {
                roomData.value.lastMove?.let { gameController.onResponse(it) }
            }
        }
    }

    if (gameType == GameType.ONE_OFFLINE) {
        LaunchedEffect(gamePlayState.value.gameState.toMove) {
            if (startColor != gamePlayState.value.gameState.toMove) {
                gameController.onPcTurn(  lifecycleOwner = lifecycleOwner, depth = depth )
            }
        }
    }


    CompositionLocalProvider(LocalActiveDatasetVisualisation  provides gamePlayState.value.visualisation) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {
            GamePlayers(gameType = gameType,userData=userData,roomData = onlineViewModel.getRoomData(),startColor = startColor)
            Status(gamePlayState.value.gameState)
            Moves(
                moves = gamePlayState.value.gameState.moves(),
                selectedItemIndex = gamePlayState.value.gameState.currentIndex - 1
            ) {
                gameController.goToMove(it)
            }
            CapturedPieces(
                gameState = gamePlayState.value.gameState,
                capturedBy = if (isFlipped) Set.WHITE else Set.BLACK,
                arrangement = Arrangement.Start,
                scoreAlignment = Alignment.End,
            )
            Board(
                gamePlayState = gamePlayState.value,
                gameController = gameController,
                isFlipped = isFlipped
            )
            CapturedPieces(
                gameState = gamePlayState.value.gameState,
                capturedBy = if (isFlipped) Set.BLACK else Set.WHITE,
                arrangement = Arrangement.End,
                scoreAlignment = Alignment.Start
            )
            GameControls(
                gamePlayState = gamePlayState.value,
                onStepBack = { gameController.stepBackward() },
                onStepForward = { gameController.stepForward() },
                onChessMateClicked = { showChessMateDialog.value = true },
                onFlipBoard = { isFlipped = !isFlipped },
                onGameClicked = { showGameDialog.value = true },
                gameController = gameController,
                gameType = gameType
            )

            if (gamePlayState.value.gameState.gameMetaInfo.result != null && gamePlayState.value.gameState.gameMetaInfo.termination != null) {
                OnFinishedGameDialog(gamePlayState = gamePlayState, gameType = gameType, userData = userData, onlineViewModel = onlineViewModel, matchesViewModel = matchesViewModel,toggleFullView = toggleFullView)
            }
        }

        GameDialogs(
            gamePlayState = gamePlayState,
            gameController = gameController,
            showChessMateDialog = showChessMateDialog,
            showGameDialog = showGameDialog,
            showImportPgnDialog = showImportPgnDialog,
            showImportFenDialog = showImportFenDialog,
            pngToImport = pngToImport,
            fenToImport = fenToImport,
            onlineViewModel = onlineViewModel,
            toggleFullView = toggleFullView,
            gameType = gameType,
            startColor = startColor,
            onlineUIClient = onlineUIClient
        )

        ManagedImport(
            pngToImport = pngToImport,
            fenToImport = fenToImport,
            gamePlayState = gamePlayState,
        )
    }
}

@Composable
private fun Status(gameState: GameState) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(36.dp)
            .background(MaterialTheme.colorScheme.primary),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = stringResource(gameState.resolutionText()),
            modifier = Modifier.padding(start = 16.dp),
            color = MaterialTheme.colorScheme.onPrimary
        )
    }
}

@Composable
private fun GameControls(
    gamePlayState: GamePlayState,
    onStepBack: () -> Unit,
    onStepForward: () -> Unit,
    onChessMateClicked: () -> Unit,
    onFlipBoard: () -> Unit,
    onGameClicked: () -> Unit,
    gameController: GameController,
    gameType: GameType
) {
    var textSpoken by remember { mutableStateOf("") }
    var canMove by remember { mutableStateOf(false)}
    var alreadySelected by remember { mutableStateOf(false) }
    var showErrorDialog by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ) {

        if (gameType == GameType.TWO_OFFLINE) {
            Button(
                onClick = onStepBack,
                enabled = gamePlayState.gameState.hasPrevIndex && gamePlayState.gameState.gameMetaInfo.result == null
            ) {
                Icon(
                    imageVector = Icons.Default.ChevronLeft,
                    tint = MaterialTheme.colorScheme.onPrimary,
                    contentDescription = stringResource(R.string.action_previous_move)
                )
            }
            Spacer(Modifier.size(4.dp))
            Button(
                onClick = onStepForward,
                enabled = gamePlayState.gameState.hasNextIndex && gamePlayState.gameState.gameMetaInfo.result == null
            ) {
                Icon(
                    imageVector = Icons.Default.ChevronRight,
                    tint = MaterialTheme.colorScheme.onPrimary,
                    contentDescription = stringResource(R.string.action_next_move)
                )
            }
            Spacer(Modifier.size(4.dp))
        }
        Button(
            onClick = onChessMateClicked,
            enabled = gamePlayState.gameState.gameMetaInfo.result == null
        ) {
            Icon(
                imageVector = Icons.Default.Layers,
                tint = MaterialTheme.colorScheme.onPrimary,
                contentDescription = stringResource(R.string.action_pick_active_visualisation)
            )
        }
        Spacer(Modifier.size(4.dp))
        Button(
            onClick = onFlipBoard,
        ) {
            Icon(
                imageVector = Icons.Default.Loop,
                tint = MaterialTheme.colorScheme.onPrimary,
                contentDescription = stringResource(R.string.action_flip)
            )
        }
        Spacer(Modifier.size(4.dp))
        Button(
            onClick = onGameClicked,
        ) {
            Icon(
                imageVector = Icons.Default.Menu,
                tint = MaterialTheme.colorScheme.onPrimary,
                contentDescription = stringResource(R.string.action_game_menu)
            )
        }
    }
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        ButtonSpeechToText(setSpokenText = {textSpoken = it}, setCanMove = {canMove = it})
        if (textSpoken != "") {

            val position = SpeechParser.parseSpeechToMove(textSpoken)
            Text(
                modifier = Modifier.padding(top = 8.dp),
                text = position?.toString() ?: "Retry",
                style = MaterialTheme.typography.bodySmall,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.secondary
            )
            if (canMove && position != null) {
                canMove = false
                if (!alreadySelected) {
                    gameController.selectBySpeech(position = position, onFinish = { alreadySelected = it }, onError = {showErrorDialog = true})
                } else {
                    gameController.moveBySpeech(position = position, onFinish = { alreadySelected = it }, onError = {showErrorDialog = true})
                }
            }
        }
    }
    if(showErrorDialog){
        OnErrorDialog { showErrorDialog = false }
    }
}


@Composable
fun OnErrorDialog(
    onDismiss: () -> Unit,
) {
    Dialog(onDismissRequest = { onDismiss() }){
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp),
            shape = RoundedCornerShape(16.dp),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Delete",
                    tint = Color.LightGray,
                    modifier = Modifier.padding(bottom = 24.dp)
                )
                Text(
                    text = "Coordinates not valid. Retry.",
                    modifier = Modifier.padding(horizontal = 16.dp),
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
                    horizontalArrangement = Arrangement.Center,
                ) {
                    TextButton(
                        onClick = { onDismiss() },
                        modifier = Modifier.padding(horizontal = 8.dp),
                    ) {
                        Text("Close")
                    }
                }
            }
        }
    }
}

@Composable
fun OnFinishedGameDialog(
    gamePlayState: MutableState<GamePlayState>,
    gameType : GameType,
    userData: UserData?,
    onlineViewModel: OnlineViewModel,
    matchesViewModel: MatchesViewModel?,
    toggleFullView: () -> Unit
) {

    if (userData != null){

        LaunchedEffect(Unit) {
            val roomId = if(onlineViewModel.roomData.value.roomId == "-1") generateRandomString(10) else  onlineViewModel.roomData.value.roomId
            val matchType = gameType.toString()
            val userIdOne = userData.id
            val userIdTwo = if(onlineViewModel.roomData.value.playerTwoId == null) {
                if(matchType == "ONE_OFFLINE") "AI Player"
                else "Local Player"
            } else onlineViewModel.roomData.value.playerTwoId
            val results = if (gamePlayState.value.gameState.resolution == Resolution.CHECKMATE){
                if(gamePlayState.value.gameState.gameMetaInfo.result == "1-0") 0 else 1
            } else {
                2
            }
            val match = Match(roomId = roomId, matchType = matchType, userIdOne = userIdOne, userIdTwo = userIdTwo!!, results = results )
            MatchesUIClient(
                userData = userData, matchesViewModel = matchesViewModel!!)
                .insertMatch(match)
        }
    }


    Dialog(onDismissRequest = { null }) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp),
            shape = RoundedCornerShape(16.dp),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = gamePlayState.value.gameState.gameMetaInfo.result!!,
                    color = MaterialTheme.colorScheme.onBackground,
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = gamePlayState.value.gameState.gameMetaInfo.termination!!,
                    color = MaterialTheme.colorScheme.onBackground,
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = TextAlign.Center
                )
                if(gameType == GameType.ONLINE) {
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "Updated elo rank: ${userData?.eloRank} --> ",
                        color = MaterialTheme.colorScheme.onBackground,
                        style = MaterialTheme.typography.bodyLarge,
                        textAlign = TextAlign.Center
                    )
                }
                Spacer(modifier = Modifier.height(15.dp))
                Button(
                    onClick = {
                        onlineViewModel.setFullViewPage("")
                        toggleFullView()
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.ChevronLeft,
                        modifier = Modifier.size(8.dp),
                        contentDescription = "Left icon"
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = "Back to Home", style = MaterialTheme.typography.bodySmall)
                }
            }
        }
    }
}