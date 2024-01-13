package com.example.chessmate.game.model.move

import com.example.chessmate.game.model.board.Position

fun List<BoardMove>.targetPositions(): List<Position> =
    map { it.to }
