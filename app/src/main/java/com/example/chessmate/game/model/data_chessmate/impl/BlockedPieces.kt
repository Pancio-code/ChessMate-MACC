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
 * Shows a colour only if the number is 0, meaning the piece is blocked from moving.
 */
@Parcelize
object BlockedPieces : DatasetVisualisation {

    @IgnoredOnParcel
    override val name = R.string.ChessMateblocked_pieces

    @IgnoredOnParcel
    override val minValue: Int = 0

    @IgnoredOnParcel
    override val maxValue: Int = 31

    override fun dataPointAt(
        position: Position,
        state: GameSnapshotState,
        cache: MutableMap<Any, Any>
    ): Datapoint? =
        valueAt(position, state)?.let { value ->
            Datapoint(
                value = value,
                label = null,
                colorScale = when (value) {
                    0 -> Color.Red.copy(alpha = 0.35f) to Color.Unspecified
                    else -> Color.Unspecified to Color.Unspecified
                },
            )
        }

    private fun valueAt(position: Position, state: GameSnapshotState): Int? =
        when {
            state.board[position].isEmpty -> null
            else -> state.legalMovesFrom(position).size
        }
}
