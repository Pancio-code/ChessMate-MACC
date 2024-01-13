package com.example.chessmate.game.model.data_chessmate.impl

import androidx.compose.ui.graphics.Color
import com.example.chessmate.R
import com.example.chessmate.game.model.board.Position
import com.example.chessmate.game.model.data_chessmate.Datapoint
import com.example.chessmate.game.model.data_chessmate.DatasetVisualisation
import com.example.chessmate.game.model.game.state.GameSnapshotState
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize


/**
 * Calculates how many legal moves a piece can take based on the current game state.
 * Shows a proportionally stronger colour the larger this humber is.
 */
@Parcelize
object ActivePieces : DatasetVisualisation {

    @IgnoredOnParcel
    override val name = R.string.ChessMateactive_pieces

    @IgnoredOnParcel
    override val minValue: Int = 2

    @IgnoredOnParcel
    override val maxValue: Int = 10

    override fun dataPointAt(
        position: Position,
        state: GameSnapshotState,
        cache: MutableMap<Any, Any>
    ): Datapoint? =
        valueAt(position, state)?.let { value ->
            Datapoint(
                value = value,
                label = null,
                colorScale = Color.Green.copy(alpha = 0.025f) to Color.Green.copy(alpha = 0.85f)
            )
        }

    private fun valueAt(position: Position, state: GameSnapshotState): Int? =
        when {
            state.board[position].isEmpty -> null
            else -> state.legalMovesFrom(position).size
        }
}
