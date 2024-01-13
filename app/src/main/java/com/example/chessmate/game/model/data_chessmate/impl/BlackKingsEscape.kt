package com.example.chessmate.game.model.data_chessmate.impl

import androidx.compose.ui.graphics.Color
import com.example.chessmate.R
import com.example.chessmate.game.model.piece.Set
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize

@Parcelize
object BlackKingsEscape : KingsEscapeSquares(
    set = Set.BLACK,
    colorScale = Color.Unspecified to Color(0xBBEE6666)
) {
    @IgnoredOnParcel
    override val name = R.string.ChessMateblack_kings_escape_squares
}
