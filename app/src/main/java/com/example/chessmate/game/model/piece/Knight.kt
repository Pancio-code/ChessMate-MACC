package com.example.chessmate.game.model.piece

import com.example.chessmate.R
import com.example.chessmate.game.model.game.state.GameSnapshotState
import com.example.chessmate.game.model.move.BoardMove
import com.example.chessmate.game.model.piece.Set.BLACK
import com.example.chessmate.game.model.piece.Set.WHITE
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize

@Parcelize
class Knight(override val set: Set) : Piece {

    @IgnoredOnParcel
    override val value: Int = 3

    @IgnoredOnParcel
    override val asset: Int =
        when (set) {
            WHITE -> R.drawable.chess_knight_light
            BLACK -> R.drawable.chess_knight_dark
        }

    @IgnoredOnParcel
    override val symbol: String = when (set) {
        WHITE -> "♘"
        BLACK -> "♞"
    }

    @IgnoredOnParcel
    override val textSymbol: String = "N"

    override fun pseudoLegalMoves(gameSnapshotState: GameSnapshotState, checkCheck: Boolean): List<BoardMove> =
        targets.mapNotNull { singleCaptureMove(gameSnapshotState, it.first, it.second) }

    companion object {
        val targets = listOf(
            -2 to 1,
            -2 to -1,
            2 to 1,
            2 to -1,
            1 to 2,
            1 to -2,
            -1 to 2,
            -1 to -2
        )
    }
}
