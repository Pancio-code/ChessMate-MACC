package com.example.chessmate.game.ui.app

import android.content.Intent
import android.os.Bundle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import com.example.chessmate.game.model.game.controller.GameController
import com.example.chessmate.game.model.game.converter.PgnConverter
import com.example.chessmate.game.model.game.state.GamePlayState
import com.example.chessmate.game.model.game.state.GameState
import com.example.chessmate.game.ui.dialogs.GameDialog
import com.example.chessmate.game.ui.dialogs.ImportFenDialog
import com.example.chessmate.game.ui.dialogs.ImportPgnDialog
import com.example.chessmate.game.ui.dialogs.PickActiveVisualisationDialog
import com.example.chessmate.game.ui.dialogs.PromotionDialog
import com.example.chessmate.multiplayer.OnlineViewModel

@Composable
fun GameDialogs(
    gamePlayState: MutableState<GamePlayState>,
    gameController: GameController,
    showChessMateDialog: MutableState<Boolean>,
    showGameDialog: MutableState<Boolean>,
    showImportPgnDialog: MutableState<Boolean>,
    showImportFenDialog: MutableState<Boolean>,
    pngToImport: MutableState<String?>,
    fenToImport: MutableState<String?>,
    togglefullView: () -> Unit = {},
    onlineViewModel: OnlineViewModel
) {
    ManagedPromotionDialog(
        showPromotionDialog = gamePlayState.value.uiState.showPromotionDialog,
        gameController = gameController
    )
    ManagedChessMateDialog(
        showChessMateDialog = showChessMateDialog,
        gameController = gameController
    )

    ManagedGameDialog(
        showGameDialog = showGameDialog,
        showImportPngDialog = showImportPgnDialog,
        showImportFenDialog = showImportFenDialog,
        gameState = gamePlayState.value.gameState,
        gameController = gameController,
        onlineViewModel = onlineViewModel,
        togglefullView = togglefullView
    )

    ManagedImportPgnDialog(
        showImportPgnDialog = showImportPgnDialog,
        pngToImport = pngToImport
    )

    ManagedImportFenDialog(
        showImportFenDialog = showImportFenDialog,
        fenToImport = fenToImport
    )
}

@Composable
fun ManagedPromotionDialog(
    showPromotionDialog: Boolean,
    gameController: GameController,
) {
    if (showPromotionDialog) {
        PromotionDialog(gameController.toMove) {
            gameController.onPromotionPieceSelected(it)
        }
    }
}

@Composable
fun ManagedChessMateDialog(
    showChessMateDialog: MutableState<Boolean>,
    gameController: GameController,
) {
    if (showChessMateDialog.value) {
        PickActiveVisualisationDialog(
            onDismiss = {
                showChessMateDialog.value = false
            },
            onItemSelected = {
                showChessMateDialog.value = false
                gameController.setVisualisation(it)
            }
        )
    }
}

@Composable
fun ManagedGameDialog(
    showGameDialog: MutableState<Boolean>,
    showImportPngDialog: MutableState<Boolean>,
    showImportFenDialog: MutableState<Boolean>,
    gameState: GameState,
    gameController: GameController,
    togglefullView: () -> Unit = {},
    onlineViewModel: OnlineViewModel
) {
    if (showGameDialog.value) {
        val context = LocalContext.current

        GameDialog(
            onDismiss = {
                showGameDialog.value = false
            },
            onNewGame = {
                showGameDialog.value = false
                gameController.reset()
            },
            onImportPgnGame = {
                showGameDialog.value = false
                showImportPngDialog.value = true
            },
            onImportFenGame = {
                showGameDialog.value = false
                showImportFenDialog.value = true
            },
            onExportGame = {
                showGameDialog.value = false
                val pgn = PgnConverter.export(gameState)
                val sendIntent: Intent = Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(Intent.EXTRA_TEXT, pgn)
                    type = "text/plain"
                }

                val shareIntent = Intent.createChooser(sendIntent, null)
                ContextCompat.startActivity(context, shareIntent, Bundle())
            },
            onExitGame = {
                onlineViewModel.setFullViewPage("")
                togglefullView()
            }
        )
    }
}

@Composable
fun ManagedImportPgnDialog(
    showImportPgnDialog: MutableState<Boolean>,
    pngToImport: MutableState<String?>,
) {
    if (showImportPgnDialog.value) {
        ImportPgnDialog(
            onDismiss = {
                showImportPgnDialog.value = false
            },
            onImportPgn = { pgn ->
                showImportPgnDialog.value = false
                pngToImport.value = pgn
            }
        )
    }
}


@Composable
fun ManagedImportFenDialog(
    showImportFenDialog: MutableState<Boolean>,
    fenToImport: MutableState<String?>,
) {
    if (showImportFenDialog.value) {
        ImportFenDialog(
            onDismiss = {
                showImportFenDialog.value = false
            },
            onImportFen = { fen ->
                showImportFenDialog.value = false
                fenToImport.value = fen
            }
        )
    }
}