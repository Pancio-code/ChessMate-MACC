package com.example.chessmate.game.model.piece

import com.example.chessmate.R
import com.example.chessmate.game.model.game.state.GameSnapshotState
import com.example.chessmate.game.model.move.BoardMove
import com.example.chessmate.game.model.piece.Set.BLACK
import com.example.chessmate.game.model.piece.Set.WHITE
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize

@Parcelize
class Queen(override val set: Set) : Piece {

    @IgnoredOnParcel
    override val value: Int = 9

    @IgnoredOnParcel
    override val asset: Int =
        when (set) {
            WHITE -> R.drawable.chess_queen_light
            BLACK -> R.drawable.chess_queen_dark
        }

    @IgnoredOnParcel
    override val symbol: String = when (set) {
        WHITE -> "♕"
        BLACK -> "♛"
    }

    @IgnoredOnParcel
    override val textSymbol: String = "Q"

    override fun pseudoLegalMoves(gameSnapshotState: GameSnapshotState, checkCheck: Boolean): List<BoardMove> =
        lineMoves(gameSnapshotState, Rook.directions + Bishop.directions)
}
