package com.example.chessmate.game.model.game.preset

import com.example.chessmate.game.model.piece.King
import com.example.chessmate.game.model.piece.Rook
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.chessmate.game.model.board.Board
import com.example.chessmate.game.model.board.Position.*
import com.example.chessmate.game.ui.app.Preset
import com.example.chessmate.game.model.game.controller.GameController
import com.example.chessmate.game.model.game.state.GameSnapshotState
import com.example.chessmate.game.model.piece.Set.BLACK
import com.example.chessmate.game.model.piece.Set.WHITE
import com.example.chessmate.ui.theme.ChessMateTheme

object InsufficientMaterialPreset3 : Preset {

    override fun apply(gameController: GameController) {
        gameController.apply {
            val board = Board(
                pieces = mapOf(
                    a7 to King(BLACK),
                    d5 to Rook(BLACK),
                    e4 to King(WHITE),
                )
            )
            reset(
                GameSnapshotState(
                    board = board,
                    toMove = WHITE
                )
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun InsufficientMaterialPreset3Preview() {
    ChessMateTheme {
        Preset(InsufficientMaterialPreset3)
    }
}

