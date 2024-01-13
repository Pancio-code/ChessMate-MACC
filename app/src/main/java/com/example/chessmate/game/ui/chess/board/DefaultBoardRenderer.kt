package com.example.chessmate.game.ui.chess.board

import com.example.chessmate.game.ui.chess.pieces.Pieces
import com.example.chessmate.game.ui.chess.board.decoration.DecoratedSquares
import com.example.chessmate.game.ui.chess.square.DefaultSquareRenderer

object DefaultBoardRenderer : BoardRenderer {

    override val decorations: List<BoardDecoration> =
        listOf(
            DecoratedSquares(DefaultSquareRenderer.decorations),
            Pieces
        )
}
