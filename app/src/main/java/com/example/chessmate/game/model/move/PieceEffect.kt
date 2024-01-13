package com.example.chessmate.game.model.move

import com.example.chessmate.game.model.piece.Piece
import android.os.Parcelable
import com.example.chessmate.game.model.board.Board
import com.example.chessmate.game.model.board.Position
import kotlinx.parcelize.Parcelize

interface PieceEffect : Parcelable {

    val piece: Piece

    fun applyOn(board: Board): Board
}

interface PreMove : PieceEffect

interface PrimaryMove : PieceEffect {

    val from: Position

    val to: Position
}

interface Consequence : PieceEffect

@Parcelize
data class Move(
    override val piece: Piece,
    override val from: Position,
    override val to: Position
) : PrimaryMove, Consequence {

    constructor(
        piece: Piece,
        intent: MoveIntention,
    ) : this(
        piece = piece,
        from = intent.from,
        to = intent.to
    )

    override fun applyOn(board: Board): Board =
        board.copy(
            pieces = board.pieces
                .minus(from)
                .plus(to to piece)
        )
}

@Parcelize
data class KingSideCastle(
    override val piece: Piece,
    override val from: Position,
    override val to: Position
) : PrimaryMove {

    override fun applyOn(board: Board): Board =
        board.copy(
            pieces = board.pieces
                .minus(from)
                .plus(to to piece)
        )
}

@Parcelize
data class QueenSideCastle(
    override val piece: Piece,
    override val from: Position,
    override val to: Position
) : PrimaryMove {

    override fun applyOn(board: Board): Board =
        board.copy(
            pieces = board.pieces
                .minus(from)
                .plus(to to piece)
        )
}

@Parcelize
data class Capture(
    override val piece: Piece,
    val position: Position,
) : PreMove {

    override fun applyOn(board: Board): Board =
        board.copy(
            pieces = board.pieces.minus(position)
        )
}

@Parcelize
data class Promotion(
    val position: Position,
    override val piece: Piece,
) : Consequence {

    override fun applyOn(board: Board): Board =
        board.copy(
            pieces = board.pieces
                .minus(position)
                .plus(position to piece)
        )
}
