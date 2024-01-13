package com.example.chessmate.game.model.move

import com.example.chessmate.game.model.board.Position

data class MoveIntention(
    val from: Position,
    val to: Position
)
