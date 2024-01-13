package com.example.chessmate.game.model.data_chessmate.impl

import androidx.compose.ui.graphics.Color
import com.example.chessmate.R
import com.example.chessmate.game.model.piece.Set
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize

@Parcelize
object WhiteKingsEscape : KingsEscapeSquares(
    set = Set.WHITE,
    colorScale = Color.Unspecified to Color(0xBB6666EE)
) {
    @IgnoredOnParcel
    override val name = R.string.ChessMatewhite_kings_escape_squares
}
