package com.example.chessmate.game.model.move

import com.example.chessmate.game.model.piece.Piece
import android.os.Parcelable
import com.example.chessmate.game.model.board.Position
import com.example.chessmate.game.model.piece.Set.WHITE
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize

@Parcelize
data class AppliedMove(
    val boardMove: BoardMove,
    val effect: MoveEffect? = null
) : Parcelable {

    @IgnoredOnParcel
    val move: PrimaryMove = boardMove.move

    @IgnoredOnParcel
    val from: Position = move.from

    @IgnoredOnParcel
    val to: Position = move.to

    @IgnoredOnParcel
    val piece: Piece = move.piece

    override fun toString(): String =
        toString(
            useFigurineNotation = true,
            includeResult = true
        )

    fun toString(useFigurineNotation: Boolean, includeResult: Boolean): String {
        val postFix = when {
            effect == MoveEffect.CHECK -> "+"
            includeResult && effect == MoveEffect.CHECKMATE -> "#  ${if (boardMove.move.piece.set == WHITE) "1-0" else "0-1"}"
            includeResult && effect == MoveEffect.DRAW -> "  ½ - ½"
            else -> ""
        }
        return "${boardMove.toString(useFigurineNotation)}$postFix"
    }
}
