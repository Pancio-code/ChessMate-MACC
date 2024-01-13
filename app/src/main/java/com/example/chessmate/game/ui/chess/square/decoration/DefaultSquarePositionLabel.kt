package com.example.chessmate.game.ui.chess.square.decoration

import com.example.chessmate.game.model.board.Coordinate

object DefaultSquarePositionLabel : SquarePositionLabelSplit(
    displayFile = { coordinate -> coordinate.y == Coordinate.max.y },
    displayRank = { coordinate -> coordinate.x == Coordinate.min.x }
)
