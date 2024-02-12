package com.example.chessmate.game.ui.app

import android.widget.Toast
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
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
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
import com.example.chessmate.multiplayer.RoomData
import com.example.chessmate.multiplayer.RoomStatus
import com.example.chessmate.sign_in.AuthUIClient
import com.example.chessmate.sign_in.SignInViewModel
import com.example.chessmate.sign_in.UserData
import com.example.chessmate.ui.pages.profile.generateRandomString
import com.example.chessmate.ui.utils.RankingManager
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

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
    signInViewModel: SignInViewModel?,
    onlineUIClient: OnlineUIClient? = null,
    authUIClient: AuthUIClient? = null,
    userData: UserData? = null
) {
    var isFlipped by rememberSaveable { mutableStateOf(!(startColor != null && startColor == Set.WHITE)) }
    val gamePlayState = rememberSaveable { mutableStateOf(state) }
    val showChessMateDialog = remember { mutableStateOf(false) }
    val showGameDialog = remember { mutableStateOf(false) }
    val showOnlineExitDialog = remember { mutableStateOf(false) }
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
            if (roomData.value.gameState == RoomStatus.FINISHED && roomData.value.winner == "") {
                onlineUIClient?.deleteRoomData(roomData.value)
                toggleFullView()
                onlineViewModel.setFullViewPage("")
                Toast.makeText(
                    LocalContext.current,
                    "Adversary Exit Game!",
                    Toast.LENGTH_LONG
                ).show()
            } else if ( gamePlayState.value.gameState.gameMetaInfo.result != null && gamePlayState.value.gameState.gameMetaInfo.termination != null) {
                if (gameType == GameType.ONLINE) {
                    OnFinishedGameDialogOnline(
                        gamePlayState = gamePlayState,
                        gameType = gameType,
                        userData = userData!!,
                        onlineViewModel = onlineViewModel,
                        matchesViewModel = matchesViewModel,
                        toggleFullView = toggleFullView,
                        signInViewModel = signInViewModel,
                        onlineUIClient = onlineUIClient,
                        authUIClient = authUIClient!!,
                        roomData = roomData.value,
                        startColor = startColor!!
                    )
                } else {
                    OnFinishedGameDialog(
                        gamePlayState = gamePlayState,
                        gameType = gameType,
                        userData = userData,
                        onlineViewModel = onlineViewModel,
                        matchesViewModel = matchesViewModel,
                        toggleFullView = toggleFullView,
                        signInViewModel = signInViewModel
                    )
                }
            }
        }

        GameDialogs(
            gamePlayState = gamePlayState,
            gameController = gameController,
            showChessMateDialog = showChessMateDialog,
            showGameDialog = showGameDialog,
            showImportPgnDialog = showImportPgnDialog,
            showImportFenDialog = showImportFenDialog,
            showOnlineExitDialog = showOnlineExitDialog,
            pngToImport = pngToImport,
            fenToImport = fenToImport,
            onlineViewModel = onlineViewModel,
            toggleFullView = toggleFullView,
            gameType = gameType,
            startColor = startColor,
            onlineUIClient = onlineUIClient,
            authUIClient = authUIClient,
            matchesViewModel = matchesViewModel,
            signInViewModel = signInViewModel,
            roomData = roomData.value,
            userData = userData
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
    signInViewModel: SignInViewModel?,
    toggleFullView: () -> Unit
) {
    var isLoading by remember { mutableStateOf(true) }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(key1 = isLoading) {
        coroutineScope.launch {
            delay(2000)
            isLoading = false
        }
    }

    if (userData != null){
        LaunchedEffect(Unit) {
            val matchType = gameType.toString()
            val (userIdOne, userIdTwo) = getUserIds(onlineViewModel = onlineViewModel, matchType = matchType, userData = userData)
            val results = getResults(matchType = matchType, resolution = gamePlayState.value.gameState.resolution, result = gamePlayState.value.gameState.gameMetaInfo.result, startColor = onlineViewModel.startColor.value )
            val roomId = generateRandomString(10)
            val match = Match(roomId = roomId, matchType = matchType, userIdOne = userIdOne, userIdTwo = userIdTwo, results = results )
            val updatedUserData = getNewUserData(userData = userData, results = results)
            MatchesUIClient(userData = userData, matchesViewModel = matchesViewModel!!).insertMatch(match = match, signInViewModel = signInViewModel, userDataNew = updatedUserData )
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
                Spacer(modifier = Modifier.height(15.dp))
                if(isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .size(40.dp)
                            .padding(8.dp),
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Updating...", color = Color.White)
                } else {
                    Button(
                        onClick = {
                            onlineViewModel.setFullViewPage("")
                            onlineViewModel.setImportedFen("")
                            toggleFullView()
                            isLoading = true
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
}

@Composable
fun OnFinishedGameDialogOnline(
    gamePlayState: MutableState<GamePlayState>,
    gameType : GameType,
    userData: UserData,
    onlineViewModel: OnlineViewModel,
    matchesViewModel: MatchesViewModel?,
    signInViewModel: SignInViewModel?,
    toggleFullView: () -> Unit,
    onlineUIClient: OnlineUIClient?,
    authUIClient: AuthUIClient,
    roomData: RoomData,
    startColor : Set
) {

    var isLoading by remember { mutableStateOf(true) }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(key1 = isLoading) {
        coroutineScope.launch {
            delay(2000)
            isLoading = false
        }
    }

    val matchesNew = userData.matchesPlayed + 1
    val matchType = gameType.toString()
    val (userIdOne, userIdTwo) = getUserIds(onlineViewModel = onlineViewModel, matchType = matchType, userData = userData)
    val eloRatingTwo = if (userIdOne == userData.id) roomData.rankPlayerTwo!! else roomData.rankPlayerOne
    val result = gamePlayState.value.gameState.gameMetaInfo.result
    val resolution = gamePlayState.value.gameState.resolution

    val win =
        if(resolution != Resolution.CHECKMATE){
        2
        } else if(startColor == Set.WHITE){
            if (result == "1-0") 0 else 1
        } else{
            if (result == "0-1") 0 else 1
        }

    val results = getResults(matchType = matchType, resolution = resolution, result = result, startColor = startColor )

    val eloRankNew = RankingManager.eloRating(
        matchesNew,
        userData.eloRank,
        eloRatingTwo,
        win == 0
    )

    LaunchedEffect(Unit) {
        val matchesWon = userData.matchesWon
        authUIClient.update(
            userData = userData.copy(
                matchesPlayed = matchesNew,
                matchesWon = if (results == 1) matchesWon + 1 else matchesWon,
                eloRank =  eloRankNew
            )
        )

        val roomId = if(roomData.roomId == "-1") generateRandomString(10) else  roomData.roomId
        val match = Match(roomId = roomId, matchType = matchType, userIdOne = userIdOne, userIdTwo = userIdTwo, results = results )
        val updatedUserData = getNewUserData(userData = userData, results = results)
        MatchesUIClient(userData = userData, matchesViewModel = matchesViewModel!!).insertMatch(match = match, signInViewModel = signInViewModel, userDataNew = updatedUserData )
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
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "Updated elo rank in your profile",
                    color = MaterialTheme.colorScheme.onBackground,
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(15.dp))
                if(isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .size(40.dp)
                            .padding(8.dp),
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Updating...", color = Color.White)
                } else {
                    Button(
                        onClick = {
                            onlineViewModel.setFullViewPage("")
                            onlineViewModel.setImportedFen("")
                            toggleFullView()
                            isLoading = true
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
}

fun getUserIds(onlineViewModel: OnlineViewModel, matchType: String, userData: UserData?): Pair<String, String> {
    var userIdOne = ""
    var userIdTwo = ""

    when (matchType) {
        "ONLINE" -> {
            userIdOne = onlineViewModel.roomData.value.playerOneId
            userIdTwo = onlineViewModel.roomData.value.playerTwoId!!
        }
        "ONE_OFFLINE" -> {
            userIdOne = userData!!.id
            userIdTwo = "AI Player"
        }
        "TWO_OFFLINE" -> {
            userIdOne = userData!!.id
            userIdTwo = "Local player"
        }
    }

    return Pair(userIdOne, userIdTwo)
}

fun getResults(matchType: String, resolution: Resolution, result: String?, startColor: Set?): Int{
    if(resolution != Resolution.CHECKMATE){
        return 2
    }

    when (matchType) {
        "ONLINE" -> {
            return if(startColor == Set.WHITE){
                if (result == "1-0") 0 else 1
            } else{ //startColor == Set.BLACK
                if (result == "0-1") 1 else 0
            }
        }
        "ONE_OFFLINE" -> {
            return if(startColor == Set.WHITE){
                if (result == "1-0") 0 else 1
            } else{ //startColor == Set.BLACK
                if (result == "1-0") 1 else 0
            }
        }
        "TWO_OFFLINE" -> {
            return if (result == "1-0") 0 else 1
        }
    }
    return 0
}

fun getNewUserData(userData: UserData, results: Int): UserData {
    //need to check if when online, if i'm black and i won, matchesWon increase
    return if (results == 0) {
        userData.copy(matchesPlayed = userData.matchesPlayed+1, matchesWon = userData.matchesWon+1)
    } else {
        userData.copy(matchesPlayed = userData.matchesPlayed+1)
    }
}