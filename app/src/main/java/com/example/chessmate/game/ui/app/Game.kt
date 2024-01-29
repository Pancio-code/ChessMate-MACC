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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Layers
import androidx.compose.material.icons.filled.Loop
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.chessmate.R
import com.example.chessmate.game.model.data_chessmate.LocalActiveDatasetVisualisation
import com.example.chessmate.game.model.game.controller.GameController
import com.example.chessmate.game.model.game.converter.FenConverter
import com.example.chessmate.game.model.game.preset.Preset
import com.example.chessmate.game.model.game.state.GamePlayState
import com.example.chessmate.game.model.game.state.GameState
import com.example.chessmate.game.model.piece.Set
import com.example.chessmate.game.ui.chess.Board
import com.example.chessmate.game.ui.chess.CapturedPieces
import com.example.chessmate.game.ui.chess.Moves
import com.example.chessmate.game.ui.chess.resolutionText
import com.example.chessmate.multiplayer.GameType
import com.example.chessmate.multiplayer.OnlineViewModel
import com.example.chessmate.sign_in.UserData

@Composable
fun Game(
    importGameFEN: String? = null,
    state: GamePlayState = if (importGameFEN != null) GamePlayState(stringFEN = importGameFEN) else GamePlayState(),
    importGamePGN: String? = null,
    preset: Preset? = null,
    gameType : GameType = GameType.TWO_OFFLINE,
    startColor : Set? = Set.WHITE,
    toggleFullView: () -> Unit = {},
    onlineViewModel: OnlineViewModel,
    userData: UserData? = null
) {
    var isFlipped by rememberSaveable { mutableStateOf(false) }
    val gamePlayState = rememberSaveable { mutableStateOf(state) }
    val showChessMateDialog = remember { mutableStateOf(false) }
    val showGameDialog = remember { mutableStateOf(false) }
    val showImportPgnDialog = remember { mutableStateOf(false) }
    val showImportFenDialog = remember { mutableStateOf(false) }
    val pngToImport = remember { mutableStateOf(importGamePGN) }
    val fenToImport = remember { mutableStateOf(importGameFEN) }

    val gameController = remember {
        GameController(
            getGamePlayState = { gamePlayState.value },
            setGamePlayState = { gamePlayState.value = it },
            preset = preset,
            startColor = startColor,
            gameType = gameType
        )
    }


    CompositionLocalProvider(LocalActiveDatasetVisualisation  provides gamePlayState.value.visualisation) {
        Log.d("FENNNN", FenConverter.getFenFromSnapshot(gamePlayState.value.gameState.currentSnapshotState,gamePlayState.value))
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {
            GamePlayers(gameType = gameType,roomData = null,userData = userData,startColor = startColor)
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
                onGameClicked = { showGameDialog.value = true }
            )

            if (gamePlayState.value.gameState.gameMetaInfo.result != null && gamePlayState.value.gameState.gameMetaInfo.termination != null) {
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
            toggleFullView = toggleFullView
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
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ) {

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
}

