package com.example.chessmate.game.model.data_chessmate.impl

import androidx.compose.ui.graphics.Color
import com.example.chessmate.R
import com.example.chessmate.game.model.board.Position
import com.example.chessmate.game.model.data_chessmate.Datapoint
import com.example.chessmate.game.model.data_chessmate.DatasetVisualisation
import com.example.chessmate.game.model.game.state.GameSnapshotState
import com.example.chessmate.game.model.piece.Set
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize

/**
 * Calculates simplified influence based on current game state:
 * - Calculates how many pieces can move to a square
 * - Uses different colour scale for dominating side
 * - Does not take into account defenders, as they're blocked by the piece being defended
 */
@Parcelize
object Influence : DatasetVisualisation {

    @IgnoredOnParcel
    override val name = R.string.ChessMateinfluence_simplified

    @IgnoredOnParcel
    override val minValue: Int = -5

    @IgnoredOnParcel
    override val maxValue: Int = 5

    @IgnoredOnParcel
    private val redScale = Color.Red.copy(alpha = 0.5f) to Color.Transparent

    @IgnoredOnParcel
    private val blueScale = Color.Transparent to Color.Blue.copy(alpha = 0.5f)

    override fun dataPointAt(
        position: Position,
        state: GameSnapshotState,
        cache: MutableMap<Any, Any>
    ): Datapoint {
        val square = state.board[position]
        val legalMovesTo = state.legalMovesTo(position)
        if (legalMovesTo.isEmpty() && square.isNotEmpty) {
            return Datapoint(
                value = if (square.hasPiece(Set.WHITE)) 1 else -1,
                label = null,
                colorScale = if (square.hasPiece(Set.WHITE)) blueScale else redScale,
            )
        }

        val sum = legalMovesTo
            .map { if (it.piece.set == Set.WHITE) 1 else -1 }
            .sum()

        return Datapoint(
            value = sum,
            label = sum.toString(),
            colorScale = when {
                sum > 0 -> blueScale
                sum < 0 -> redScale
                else -> Color.Transparent to Color.Transparent
            }
        )
    }
}
