package com.example.chessmate.game.ui.app

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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.chessmate.R
import com.example.chessmate.game.model.board.Position.b1
import com.example.chessmate.game.model.board.Position.b5
import com.example.chessmate.game.model.board.Position.b8
import com.example.chessmate.game.model.board.Position.c3
import com.example.chessmate.game.model.board.Position.c6
import com.example.chessmate.game.model.board.Position.d5
import com.example.chessmate.game.model.board.Position.d7
import com.example.chessmate.game.model.board.Position.d8
import com.example.chessmate.game.model.board.Position.e2
import com.example.chessmate.game.model.board.Position.e4
import com.example.chessmate.game.model.board.Position.e5
import com.example.chessmate.game.model.board.Position.e7
import com.example.chessmate.game.model.board.Position.f1
import com.example.chessmate.game.model.board.Position.g8
import com.example.chessmate.game.model.data_chessmate.LocalActiveDatasetVisualisation
import com.example.chessmate.game.model.game.controller.GameController
import com.example.chessmate.game.model.game.preset.Preset
import com.example.chessmate.game.model.game.state.GamePlayState
import com.example.chessmate.game.model.game.state.GameState
import com.example.chessmate.game.model.piece.Set
import com.example.chessmate.game.ui.chess.Board
import com.example.chessmate.game.ui.chess.CapturedPieces
import com.example.chessmate.game.ui.chess.Moves
import com.example.chessmate.game.ui.chess.resolutionText
import com.example.chessmate.ui.theme.ChessMateTheme

@Composable
fun Game(
    state: GamePlayState = GamePlayState(),
    importGameText: String? = null,
    preset: Preset? = null,
) {
    var isFlipped by rememberSaveable { mutableStateOf(false) }
    val gamePlayState = rememberSaveable { mutableStateOf(state) }
    val showChessMateDialog = remember { mutableStateOf(false) }
    val showGameDialog = remember { mutableStateOf(false) }
    val showImportDialog = remember { mutableStateOf(false) }
    val pgnToImport = remember { mutableStateOf(importGameText) }

    val gameController = remember {
        GameController(
            getGamePlayState = { gamePlayState.value },
            setGamePlayState = { gamePlayState.value = it },
            preset = preset
        )
    }

    CompositionLocalProvider(LocalActiveDatasetVisualisation  provides gamePlayState.value.visualisation) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {
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
        }

        GameDialogs(
            gamePlayState = gamePlayState,
            gameController = gameController,
            showChessMateDialog = showChessMateDialog,
            showGameDialog = showGameDialog,
            showImportDialog = showImportDialog,
            pgnToImport = pgnToImport,
        )

        ManagedImport(
            pgnToImport = pgnToImport,
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
            enabled = gamePlayState.gameState.hasPrevIndex
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
            enabled = gamePlayState.gameState.hasNextIndex
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

@Preview(showBackground = true)
@Composable
fun GamePreview() {
    ChessMateTheme {
        var gamePlayState = GamePlayState()
        GameController({ gamePlayState }, { gamePlayState = it }).apply {
            applyMove(e2, e4)
            applyMove(e7, e5)
            applyMove(b1, c3)
            applyMove(b8, c6)
            applyMove(f1, b5)
            applyMove(d7, d5)
            applyMove(e4, d5)
            applyMove(d8, d5)
            applyMove(c3, d5)
            onClick(g8)
        }
        Game(
            state = gamePlayState,
        )
    }
}
