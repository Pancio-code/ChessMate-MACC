package com.example.chessmate.game.ui.chess.board

import androidx.compose.runtime.Composable

interface BoardDecoration {

    @Composable
    fun Render(properties: BoardRenderProperties)
}
