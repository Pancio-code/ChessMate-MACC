package com.example.chessmate.game.model.game.state

import com.example.chessmate.game.model.piece.Piece
import android.os.Parcelable
import com.example.chessmate.game.model.board.Position
import kotlinx.parcelize.Parcelize

sealed class PromotionState : Parcelable {
    @Parcelize
    data object None : PromotionState()

    @Parcelize
    data class Await(val position: Position) : PromotionState()

    @Parcelize
    data class ContinueWith(val piece: Piece) : PromotionState()
}
