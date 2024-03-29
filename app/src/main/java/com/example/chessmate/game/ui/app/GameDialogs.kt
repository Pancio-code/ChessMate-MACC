package com.example.chessmate.game.ui.app

import android.content.Intent
import android.os.Bundle
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewModelScope
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
import kotlinx.coroutines.launch

@Composable
fun GameDialogs(
    gamePlayState: MutableState<GamePlayState>,
    gameController: GameController,
    showChessMateDialog: MutableState<Boolean>,
    showGameDialog: MutableState<Boolean>,
    showOnlineExitDialog: MutableState<Boolean>,
    showImportPgnDialog: MutableState<Boolean>,
    showImportFenDialog: MutableState<Boolean>,
    pngToImport: MutableState<String?>,
    fenToImport: MutableState<String?>,
    toggleFullView: () -> Unit = {},
    onlineViewModel: OnlineViewModel,
    gameType: GameType,
    startColor : Set? = null,
    onlineUIClient: OnlineUIClient? = null,
    authUIClient: AuthUIClient? = null,
    matchesViewModel: MatchesViewModel? = null,
    signInViewModel: SignInViewModel? = null,
    roomData: RoomData? = null,
    userData: UserData? = null
) {
    ManagedPromotionDialog(
        showPromotionDialog = gamePlayState.value.uiState.showPromotionDialog,
        gameController = gameController,
        gameType = gameType,
        startColor = startColor,
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
        showOnlineExitDialog = showOnlineExitDialog
    )

    ManagedOnlineExitDialog(
        showOnlineExitDialog = showOnlineExitDialog,
        onlineUIClient = onlineUIClient,
        onlineViewModel = onlineViewModel,
        toggleFullView = toggleFullView,
        signInViewModel = signInViewModel,
        matchesViewModel = matchesViewModel,
        roomData = roomData,
        userData = userData,
        authUIClient = authUIClient,
        gameType = gameType,
        startColor = startColor
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
fun ManagedOnlineExitDialog(
    showOnlineExitDialog : MutableState<Boolean>,
    toggleFullView: () -> Unit = {},
    onlineViewModel: OnlineViewModel,
    onlineUIClient: OnlineUIClient? = null,
    gameType: GameType,
    userData : UserData?,
    roomData: RoomData?,
    authUIClient: AuthUIClient?,
    matchesViewModel: MatchesViewModel?,
    signInViewModel: SignInViewModel?,
    startColor: Set?
) {
    if (showOnlineExitDialog.value) {
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
                        text = "Do you want quit the game?",
                        color = MaterialTheme.colorScheme.onBackground,
                        style = MaterialTheme.typography.bodyLarge,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "You will the lose the match.",
                        color = MaterialTheme.colorScheme.onBackground,
                        style = MaterialTheme.typography.bodySmall,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(15.dp))
                    Button(
                        onClick = {
                            val matchesNew = userData!!.matchesPlayed + 1
                            val matchType = gameType.toString()
                            val (userIdOne, userIdTwo) = getUserIds(onlineViewModel = onlineViewModel, matchType = matchType, userData = userData)
                            val eloRatingTwo = if (userIdOne == userData.id) roomData!!.rankPlayerTwo!! else roomData!!.rankPlayerOne

                            val results = if(startColor == Set.WHITE){
                                1
                            } else{ //startColor == Set.BLACK
                                0
                            };

                            val eloRankNew = RankingManager.eloRating(
                                matchesNew,
                                userData.eloRank,
                                eloRatingTwo,
                                false
                            )

                            onlineViewModel.viewModelScope.launch {
                                val matchesWon = userData.matchesWon
                                authUIClient!!.update(
                                    userData = userData.copy(
                                        matchesPlayed = matchesNew,
                                        matchesWon = matchesWon,
                                        eloRank =  eloRankNew
                                    )
                                )

                                val roomId = if(roomData.roomId == "-1") generateRandomString(10) else  roomData.roomId
                                val match = Match(roomId = roomId, matchType = matchType, userIdOne = userIdOne, userIdTwo = userIdTwo, results = results )
                                val updatedUserData = getNewUserData(userData = userData, results = results)
                                MatchesUIClient(userData = userData, matchesViewModel = matchesViewModel!!).insertMatch(match = match, signInViewModel = signInViewModel, userDataNew = updatedUserData )
                            }

                            onlineUIClient?.updateRoomData(onlineViewModel.roomData.value.copy(gameState = RoomStatus.FINISHED))
                            showOnlineExitDialog.value = false
                            onlineUIClient!!.stopListeningToRoomData()
                            onlineViewModel.setRoomData(RoomData())
                            onlineViewModel.setFullViewPage("")
                            toggleFullView()
                        },
                        colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.error)
                    ) {
                        Icon(
                            imageVector = Icons.Default.ErrorOutline,
                            modifier = Modifier.size(8.dp),
                            contentDescription = "quit icon",
                            tint = MaterialTheme.colorScheme.onError
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Quit Game",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onError
                        )
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    Button(
                        onClick = {
                            showOnlineExitDialog.value = false
                        }
                    ) {
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = "Resume", style = MaterialTheme.typography.bodySmall)
                    }
                }
            }
        }
    }
}

@Composable
fun ManagedPromotionDialog(
    showPromotionDialog: Boolean,
    gameController: GameController,
    startColor: Set? = null,
    gameType : GameType? = null,
) {
    if (showPromotionDialog) {
        val set = gameController.toMove
        if (gameType == GameType.TWO_OFFLINE || startColor == set) {
            PromotionDialog(set) { piece ->
                gameController.onPromotionPieceSelected(piece)
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
    showOnlineExitDialog: MutableState<Boolean>,
    gameState: GameState,
    gamePlayState : GamePlayState,
    gameController: GameController,
    toggleFullView: () -> Unit = {},
    onlineViewModel: OnlineViewModel,
    gameType: GameType,
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
                    showGameDialog.value = false
                    showOnlineExitDialog.value = true
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