package com.example.chessmate.game.model.game.state

import android.os.Parcelable
import com.example.chessmate.game.model.board.Board
import com.example.chessmate.game.model.piece.Set
import kotlinx.parcelize.Parcelize

@Parcelize
data class RepetitionRelevantState(
    val board: Board,
    val toMove: Set,
    val castlingInfo: CastlingInfo,
) : Parcelable
