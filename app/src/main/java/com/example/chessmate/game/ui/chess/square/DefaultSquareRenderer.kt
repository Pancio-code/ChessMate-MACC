package com.example.chessmate.game.ui.chess.square

import com.example.chessmate.game.ui.chess.square.decoration.DatasetVisualiser
import com.example.chessmate.game.ui.chess.square.decoration.DefaultHighlightSquare
import com.example.chessmate.game.ui.chess.square.decoration.DefaultSquareBackground
import com.example.chessmate.game.ui.chess.square.decoration.DefaultSquarePositionLabel
import com.example.chessmate.game.ui.chess.square.decoration.TargetMarks

object DefaultSquareRenderer : SquareRenderer {

    override val decorations: List<SquareDecoration> =
        listOf(
            DefaultSquareBackground,
            DefaultHighlightSquare,
            DefaultSquarePositionLabel,
            DatasetVisualiser,
            TargetMarks
        )
}
