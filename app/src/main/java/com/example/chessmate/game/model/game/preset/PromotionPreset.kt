package com.example.chessmate.game.model.game.preset

import com.example.chessmate.game.model.piece.King
import com.example.chessmate.game.model.piece.Knight
import com.example.chessmate.game.model.piece.Pawn
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.chessmate.game.model.board.Board
import com.example.chessmate.game.model.board.Position.*
import com.example.chessmate.game.model.game.controller.GameController
import com.example.chessmate.game.model.game.state.GameSnapshotState
import com.example.chessmate.game.ui.app.Preset
import com.example.chessmate.game.model.piece.Set.BLACK
import com.example.chessmate.game.model.piece.Set.WHITE
import com.example.chessmate.ui.theme.ChessMateTheme

object PromotionPreset : Preset {

    override fun apply(gameController: GameController) {
        gameController.apply {
            val board = Board(
                pieces = mapOf(
                    a7 to King(BLACK),
                    f8 to Knight(BLACK),
                    g2 to King(WHITE),
                    g7 to Pawn(WHITE),
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
fun PromotionPresetPreview() {
    ChessMateTheme {
        Preset(PromotionPreset)
    }
}

