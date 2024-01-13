package com.example.chessmate.game.model.game.preset

import com.example.chessmate.game.model.piece.King
import com.example.chessmate.game.model.piece.Knight
import com.example.chessmate.game.model.piece.Pawn
import com.example.chessmate.game.model.piece.Queen
import com.example.chessmate.game.model.piece.Rook
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.chessmate.game.model.board.Board
import com.example.chessmate.game.ui.app.Preset
import com.example.chessmate.game.model.board.Position.*
import com.example.chessmate.game.model.game.controller.GameController
import com.example.chessmate.game.model.game.state.GameSnapshotState
import com.example.chessmate.game.model.piece.Set.BLACK
import com.example.chessmate.game.model.piece.Set.WHITE
import com.example.chessmate.ui.theme.ChessMateTheme

object AmbiguityCheckPreset : Preset {

    override fun apply(gameController: GameController) {
        gameController.apply {
            val board = Board(
                pieces = mapOf(
                    e2 to King(BLACK),
                    c3 to Pawn(BLACK),
                    h1 to King(WHITE),
                    e4 to Knight(WHITE),
                    a4 to Knight(WHITE),
                    a2 to Knight(WHITE),
                    b1 to Knight(WHITE),
                    d1 to Knight(WHITE),
                    b7 to Rook(WHITE),
                    c8 to Rook(WHITE),
                    d7 to Rook(WHITE),
                    g8 to Queen(WHITE),
                    h8 to Queen(WHITE),
                    h7 to Queen(WHITE),
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
fun AmbiguityCheckPresetPreview() {
    ChessMateTheme {
        Preset(AmbiguityCheckPreset)
    }
}

