package com.example.chessmate.game.ui.chess

import androidx.compose.foundation.layout.offset
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.Dp
import com.example.chessmate.R
import com.example.chessmate.game.model.board.Coordinate
import com.example.chessmate.game.model.game.Resolution
import com.example.chessmate.game.model.game.state.GameState
import com.example.chessmate.game.model.piece.Set

fun Coordinate.toOffset(squareSize: Dp): Offset =
    Offset(
        x = (x - 1) * squareSize.value,
        y = (y - 1) * squareSize.value
    )


fun Coordinate.toOffsetModifier(squareSize: Dp): Modifier =
    Modifier
        .offset(
            Dp((x - 1) * squareSize.value),
            Dp((y - 1) * squareSize.value)
        )

fun Offset.toModifier(): Modifier =
    Modifier.offset(Dp(x), Dp(y))

fun GameState.resolutionText(): Int =
    when (resolution) {
        Resolution.IN_PROGRESS -> when (toMove) {
            Set.WHITE -> R.string.resolution_white_to_move
            Set.BLACK -> R.string.resolution_black_to_move
        }
        Resolution.CHECKMATE -> when (toMove) {
            Set.WHITE -> R.string.resolution_black_wins
            Set.BLACK -> R.string.resolution_white_wins
        }
        Resolution.STALEMATE -> R.string.resolution_stalemate
        Resolution.DRAW_BY_REPETITION -> R.string.resolution_draw_by_repetition
        Resolution.INSUFFICIENT_MATERIAL -> R.string.resolution_insufficient_material
    }
