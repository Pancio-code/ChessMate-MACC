package com.example.chessmate.game.ui.app

import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.ui.platform.LocalContext
import com.example.chessmate.game.model.game.converter.FenConverter
import com.example.chessmate.game.model.game.converter.ImportResult
import com.example.chessmate.game.model.game.converter.PgnConverter
import com.example.chessmate.game.model.game.state.GamePlayState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Composable
fun ManagedImport(
    pngToImport: MutableState<String?>,
    fenToImport: MutableState<String?>,
    gamePlayState: MutableState<GamePlayState>,
) {
    val pgn = pngToImport.value
    val fen = fenToImport.value
    val context = LocalContext.current
    val genericError = "Error while importing game"

    if (pgn != null) {
        LoadingSpinner(loadingText = "Loading game from PGN...")
        LaunchedEffect(pgn) {
            withContext(Dispatchers.IO) {
                when (val result = PgnConverter.import(pgn)) {
                    is ImportResult.ImportedGame -> gamePlayState.value = GamePlayState(gameState = result.gameState)
                    is ImportResult.ValidationError -> {
                        withContext(Dispatchers.Main) {
                            Log.e("ChessMate PgnConverter", result.msg)
                            Toast.makeText(
                                context,
                                genericError.replace("%s", result.msg),
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
                }

                pngToImport.value = null
            }
        }
    } else if (fen != null) {
        LoadingSpinner(loadingText = "Loading game from FEN...")
        LaunchedEffect(fen) {
            withContext(Dispatchers.IO) {
                when (val result = FenConverter.import(fen)) {
                    is ImportResult.ImportedGame -> gamePlayState.value = GamePlayState(gameState = result.gameState)
                    is ImportResult.ValidationError -> {
                        withContext(Dispatchers.Main) {
                            Log.e("ChessMate Fen Converter", result.msg)
                            Toast.makeText(
                                context,
                                genericError.replace("%s", result.msg),
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
                }

                fenToImport.value = null
            }
        }
    }
}
