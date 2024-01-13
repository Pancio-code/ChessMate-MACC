package com.example.chessmate.game.ui.chess.square

import androidx.compose.runtime.Composable

interface SquareDecoration {

    @Composable
    fun Render(properties: SquareRenderProperties)
}

