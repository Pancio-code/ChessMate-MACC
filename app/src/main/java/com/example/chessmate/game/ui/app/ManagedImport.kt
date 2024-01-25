package com.example.chessmate.game.ui.app

import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.ui.platform.LocalContext
import com.example.chessmate.game.model.game.converter.ImportResult
import com.example.chessmate.game.model.game.converter.PgnConverter
import com.example.chessmate.game.model.game.state.GamePlayState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Composable
fun ManagedImport(
    pngToImport: MutableState<String?>,
    gamePlayState: MutableState<GamePlayState>,
) {
    val png = pngToImport.value
    val context = LocalContext.current
    val genericError = "Error while importing game"

    if (png != null) {
        LoadingSpinner()
        LaunchedEffect(png) {
            withContext(Dispatchers.IO) {
                when (val result = PgnConverter.import(png)) {
                    is ImportResult.ImportedGame -> gamePlayState.value = GamePlayState(gameState =result.gameState)
                    is ImportResult.ValidationError -> {
                        withContext(Dispatchers.Main) {
                            Log.e("ChessMate PNGCOnverter", result.msg)
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
    }
}
