package com.example.chessmate.game.ui.chess.square.decoration

import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.example.chessmate.game.model.board.isDarkSquare
import com.example.chessmate.game.ui.chess.square.SquareRenderProperties
import com.example.chessmate.game.ui.chess.square.SquareDecoration

open class SquareBackground(
    private val lightSquareColor: Color,
    private val darkSquareColor: Color,
) : SquareDecoration {

    @Composable
    override fun render(properties: SquareRenderProperties) {
        Canvas(properties.sizeModifier) {
            drawRect(color = if (properties.position.isDarkSquare()) darkSquareColor else lightSquareColor)
        }
    }
}

