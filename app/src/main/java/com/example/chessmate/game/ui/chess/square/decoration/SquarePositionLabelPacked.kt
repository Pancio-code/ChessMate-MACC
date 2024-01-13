package com.example.chessmate.game.ui.chess.square.decoration

import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import com.example.chessmate.game.model.board.Coordinate
import com.example.chessmate.game.ui.chess.square.SquareRenderProperties
import com.example.chessmate.game.ui.chess.square.SquareDecoration

open class SquarePositionLabelPacked(
    private val display: (Coordinate) -> Boolean,
) : SquareDecoration {

    @Composable
    override fun Render(properties: SquareRenderProperties) {
        if (display(properties.coordinate)) {
            PositionLabel(
                text = properties.position.toString(),
                alignment = Alignment.TopStart,
                modifier = properties.sizeModifier
            )
        }
    }
}
