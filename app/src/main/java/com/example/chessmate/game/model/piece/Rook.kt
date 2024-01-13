package com.example.chessmate.game.model.piece

import com.example.chessmate.R
import com.example.chessmate.game.model.game.state.GameSnapshotState
import com.example.chessmate.game.model.move.BoardMove
import com.example.chessmate.game.model.piece.Set.BLACK
import com.example.chessmate.game.model.piece.Set.WHITE
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize

@Parcelize
class Rook(override val set: Set) : Piece {

    @IgnoredOnParcel
    override val value: Int = 5

    @IgnoredOnParcel
    override val asset: Int =
        when (set) {
            WHITE -> R.drawable.chess_rook_light
            BLACK -> R.drawable.chess_rook_dark
        }

    @IgnoredOnParcel
    override val symbol: String = when (set) {
        WHITE -> "♖"
        BLACK -> "♜"
    }

    @IgnoredOnParcel
    override val textSymbol: String = "R"

    override fun pseudoLegalMoves(gameSnapshotState: GameSnapshotState, checkCheck: Boolean): List<BoardMove> =
        lineMoves(gameSnapshotState, directions)

    companion object {
        val directions = listOf(
            0 to -1,
            0 to 1,
            -1 to 0,
            1 to 0,
        )
    }
}
