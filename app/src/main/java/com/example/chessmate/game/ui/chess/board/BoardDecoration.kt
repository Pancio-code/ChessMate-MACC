package com.example.chessmate.game.ui.chess.board

import androidx.compose.runtime.Composable

interface BoardDecoration {

    @Composable
    fun render(properties: BoardRenderProperties)
}
