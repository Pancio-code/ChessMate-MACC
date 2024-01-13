package com.example.chessmate.game.model.game.preset

import com.example.chessmate.game.model.piece.Bishop
import com.example.chessmate.game.model.piece.King
import com.example.chessmate.game.model.piece.Rook
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.chessmate.game.model.board.Board
import com.example.chessmate.game.model.board.Position.*
import com.example.chessmate.game.model.game.controller.GameController
import com.example.chessmate.game.model.game.state.GameSnapshotState
import com.example.chessmate.game.model.piece.Set.BLACK
import com.example.chessmate.game.model.piece.Set.WHITE
import com.example.chessmate.ui.theme.ChessMateTheme
import com.example.chessmate.game.ui.app.Preset

object InsufficientMaterialPreset2 : Preset {

    override fun apply(gameController: GameController) {
        gameController.apply {
            val board = Board(
                pieces = mapOf(
                    a7 to King(BLACK),
                    g8 to Rook(BLACK),
                    g2 to King(WHITE),
                    d5 to Bishop(WHITE),
                    c2 to Bishop(BLACK),
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
fun InsufficientMaterialPreset2Preview() {
    ChessMateTheme {
        Preset(InsufficientMaterialPreset2)
    }
}

