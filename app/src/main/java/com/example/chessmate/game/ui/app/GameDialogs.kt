package com.example.chessmate.game.ui.app

import android.content.Intent
import android.os.Bundle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import com.example.chessmate.game.model.game.Resolution
import com.example.chessmate.game.model.game.controller.GameController
import com.example.chessmate.game.model.game.converter.FenConverter
import com.example.chessmate.game.model.game.converter.PgnConverter
import com.example.chessmate.game.model.game.state.GamePlayState
import com.example.chessmate.game.model.game.state.GameState
import com.example.chessmate.game.model.piece.Bishop
import com.example.chessmate.game.model.piece.Knight
import com.example.chessmate.game.model.piece.Piece
import com.example.chessmate.game.model.piece.Queen
import com.example.chessmate.game.model.piece.Rook
import com.example.chessmate.game.model.piece.Set
import com.example.chessmate.game.ui.dialogs.GameDialog
import com.example.chessmate.game.ui.dialogs.ImportFenDialog
import com.example.chessmate.game.ui.dialogs.ImportPgnDialog
import com.example.chessmate.game.ui.dialogs.PickActiveVisualisationDialog
import com.example.chessmate.game.ui.dialogs.PromotionDialog
import com.example.chessmate.multiplayer.GameType
import com.example.chessmate.multiplayer.OnlineUIClient
import com.example.chessmate.multiplayer.OnlineViewModel
import com.example.chessmate.multiplayer.RoomData
import com.example.chessmate.multiplayer.RoomStatus
import java.util.Locale

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
    toggleFullView: () -> Unit = {},
    onlineViewModel: OnlineViewModel,
    gameType: GameType,
    startColor : Set? = null,
    roomData : RoomData? = null,
    onlineUIClient: OnlineUIClient? = null
) {
    ManagedPromotionDialog(
        showPromotionDialog = gamePlayState.value.uiState.showPromotionDialog,
        gameController = gameController,
        gameType = gameType,
        gameState = gamePlayState.value.gameState,
        startColor = startColor,
        roomData = roomData,
        onlineUIClient = onlineUIClient
    )
    ManagedChessMateDialog(
        showChessMateDialog = showChessMateDialog,
        gameController = gameController
    )

    ManagedGameDialog(
        showGameDialog = showGameDialog,
        showImportPngDialog = showImportPgnDialog,
        showImportFenDialog = showImportFenDialog,
        gamePlayState = gamePlayState.value,
        gameState = gamePlayState.value.gameState,
        gameController = gameController,
        onlineViewModel = onlineViewModel,
        toggleFullView = toggleFullView,
        gameType = gameType,
        onlineUIClient = onlineUIClient
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
    startColor: Set? = null,
    gameType : GameType? = null,
    gameState : GameState,
    roomData : RoomData? = null,
    onlineUIClient: OnlineUIClient? = null
) {
    if (showPromotionDialog) {
        val set = gameController.toMove
        if (gameType == GameType.TWO_OFFLINE || startColor == set) {
            PromotionDialog(set) { piece ->
                gameController.onPromotionPieceSelected(piece)
                if (gameType == GameType.ONLINE) {
                   roomData?.let {
                        onlineUIClient!!.updateRoomData(
                            model = it.copy(
                                gameState = if (gameState.resolution != Resolution.IN_PROGRESS) RoomStatus.FINISHED else RoomStatus.INPROGRESS,
                                currentTurn =  gameState.toMove.name,
                                lastMove = piece.textSymbol.lowercase(Locale.ROOT)
                            )
                        )
                    }
                }
            }
        } else if (gameType == GameType.ONE_OFFLINE) {
            val piece: Piece = convertToPiece(set, gameController)
            gameController.onPromotionPieceSelected(piece)
        }
    }
}

fun convertToPiece(set : Set,gameController : GameController) : Piece{
    return when(gameController.getPcPromotionPiece()) {
        "q" -> Queen(set)
        "r" -> Rook(set)
        "b" -> Bishop(set)
        "n" -> Knight(set)
        else -> throw IllegalStateException()
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
    gamePlayState : GamePlayState,
    gameController: GameController,
    toggleFullView: () -> Unit = {},
    onlineViewModel: OnlineViewModel,
    gameType: GameType,
    onlineUIClient: OnlineUIClient? = null
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
            onExportGamePgn = {
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
            onExportGameFen = {
                showGameDialog.value = false
                val fen = FenConverter.getFenFromSnapshot(gameState.currentSnapshotState,gamePlayState)
                val sendIntent: Intent = Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(Intent.EXTRA_TEXT, fen)
                    type = "text/plain"
                }

                val shareIntent = Intent.createChooser(sendIntent, null)
                ContextCompat.startActivity(context, shareIntent, Bundle())
            },
            onExitGame = {
                if( gameType == GameType.ONLINE) {
                    onlineUIClient?.deleteRoomData(onlineViewModel.getRoomData())
                    onlineViewModel.setFullViewPage("")
                    toggleFullView()
                } else {
                    onlineViewModel.setFullViewPage("")
                    onlineViewModel.setImportedFen("")
                    toggleFullView()
                }
            },
            gameType = gameType
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