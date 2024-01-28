package com.example.chessmate.game.model.board

import android.os.Parcelable
import com.example.chessmate.game.model.board.Position.a1
import com.example.chessmate.game.model.board.Position.a2
import com.example.chessmate.game.model.board.Position.a7
import com.example.chessmate.game.model.board.Position.a8
import com.example.chessmate.game.model.board.Position.b1
import com.example.chessmate.game.model.board.Position.b2
import com.example.chessmate.game.model.board.Position.b7
import com.example.chessmate.game.model.board.Position.b8
import com.example.chessmate.game.model.board.Position.c1
import com.example.chessmate.game.model.board.Position.c2
import com.example.chessmate.game.model.board.Position.c7
import com.example.chessmate.game.model.board.Position.c8
import com.example.chessmate.game.model.board.Position.d1
import com.example.chessmate.game.model.board.Position.d2
import com.example.chessmate.game.model.board.Position.d7
import com.example.chessmate.game.model.board.Position.d8
import com.example.chessmate.game.model.board.Position.e1
import com.example.chessmate.game.model.board.Position.e2
import com.example.chessmate.game.model.board.Position.e7
import com.example.chessmate.game.model.board.Position.e8
import com.example.chessmate.game.model.board.Position.entries
import com.example.chessmate.game.model.board.Position.f1
import com.example.chessmate.game.model.board.Position.f2
import com.example.chessmate.game.model.board.Position.f7
import com.example.chessmate.game.model.board.Position.f8
import com.example.chessmate.game.model.board.Position.g1
import com.example.chessmate.game.model.board.Position.g2
import com.example.chessmate.game.model.board.Position.g7
import com.example.chessmate.game.model.board.Position.g8
import com.example.chessmate.game.model.board.Position.h1
import com.example.chessmate.game.model.board.Position.h2
import com.example.chessmate.game.model.board.Position.h7
import com.example.chessmate.game.model.board.Position.h8
import com.example.chessmate.game.model.game.converter.FenConverter
import com.example.chessmate.game.model.move.PieceEffect
import com.example.chessmate.game.model.piece.Bishop
import com.example.chessmate.game.model.piece.King
import com.example.chessmate.game.model.piece.Knight
import com.example.chessmate.game.model.piece.Pawn
import com.example.chessmate.game.model.piece.Piece
import com.example.chessmate.game.model.piece.Queen
import com.example.chessmate.game.model.piece.Rook
import com.example.chessmate.game.model.piece.Set
import com.example.chessmate.game.model.piece.Set.BLACK
import com.example.chessmate.game.model.piece.Set.WHITE
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize

@Parcelize
data class Board(
    val pieces: Map<Position, Piece>,
    val stringFEN: String? = null
) : Parcelable {

    constructor() : this(
        pieces = initialPieces
    )
    constructor(stringFEN: String) : this(
        pieces = FenConverter.fenToMap(stringFEN)
    )


    @IgnoredOnParcel
    val squares = entries.associateWith { position ->
        Square(position, pieces[position])
    }

    operator fun get(position: Position): Square =
        squares[position]!!

    operator fun get(file: File, rank: Int): Square? =
        get(file.ordinal + 1, rank)

    operator fun get(file: Int, rank: Int): Square? {
        return try {
            val position = Position.from(file, rank)
            squares[position]
        } catch (e: IllegalArgumentException) {
            null
        }
    }

    fun find(piece: Piece): Square? =
        squares.values.firstOrNull { it.piece == piece }

    inline fun <reified T : Piece> find(set: Set): List<Square> =
        squares.values.filter {
            it.piece != null &&
                it.piece::class == T::class &&
                it.piece.set == set
        }

    fun pieces(set: Set): Map<Position, Piece> =
        pieces.filter { (_, piece) -> piece.set == set }

    fun apply(effect: PieceEffect?): Board =
        effect?.applyOn(this) ?: this
}

private val initialPieces = mapOf(
    a8 to Rook(BLACK),
    b8 to Knight(BLACK),
    c8 to Bishop(BLACK),
    d8 to Queen(BLACK),
    e8 to King(BLACK),
    f8 to Bishop(BLACK),
    g8 to Knight(BLACK),
    h8 to Rook(BLACK),

    a7 to Pawn(BLACK),
    b7 to Pawn(BLACK),
    c7 to Pawn(BLACK),
    d7 to Pawn(BLACK),
    e7 to Pawn(BLACK),
    f7 to Pawn(BLACK),
    g7 to Pawn(BLACK),
    h7 to Pawn(BLACK),

    a2 to Pawn(WHITE),
    b2 to Pawn(WHITE),
    c2 to Pawn(WHITE),
    d2 to Pawn(WHITE),
    e2 to Pawn(WHITE),
    f2 to Pawn(WHITE),
    g2 to Pawn(WHITE),
    h2 to Pawn(WHITE),

    a1 to Rook(WHITE),
    b1 to Knight(WHITE),
    c1 to Bishop(WHITE),
    d1 to Queen(WHITE),
    e1 to King(WHITE),
    f1 to Bishop(WHITE),
    g1 to Knight(WHITE),
    h1 to Rook(WHITE),
)
