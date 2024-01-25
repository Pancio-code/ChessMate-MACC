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
import com.example.chessmate.game.ui.dialogs.ImportDialog
import com.example.chessmate.game.ui.dialogs.PickActiveVisualisationDialog
import com.example.chessmate.game.ui.dialogs.PromotionDialog
import com.example.chessmate.multiplayer.OnlineViewModel

@Composable
fun GameDialogs(
    gamePlayState: MutableState<GamePlayState>,
    gameController: GameController,
    showChessMateDialog: MutableState<Boolean>,
    showGameDialog: MutableState<Boolean>,
    showImportDialog: MutableState<Boolean>,
    pngToImport: MutableState<String?>,
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
        showImportDialog = showImportDialog,
        gameState = gamePlayState.value.gameState,
        gameController = gameController,
        onlineViewModel = onlineViewModel,
        togglefullView = togglefullView
    )

    ManagedImportDialog(
        showImportDialog = showImportDialog,
        pngToImport = pngToImport
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
    showImportDialog: MutableState<Boolean>,
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
            onImportGame = {
                showGameDialog.value = false
                showImportDialog.value = true
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
fun ManagedImportDialog(
    showImportDialog: MutableState<Boolean>,
    pngToImport: MutableState<String?>,
) {
    if (showImportDialog.value) {
        ImportDialog(
            onDismiss = {
                showImportDialog.value = false
            },
            onImport = { png ->
                showImportDialog.value = false
                pngToImport.value = png
            }
        )
    }
}

