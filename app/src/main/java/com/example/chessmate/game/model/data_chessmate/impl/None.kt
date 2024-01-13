package com.example.chessmate.game.model.data_chessmate.impl

import com.example.chessmate.R
import com.example.chessmate.game.model.board.Position
import com.example.chessmate.game.model.data_chessmate.Datapoint
import com.example.chessmate.game.model.data_chessmate.DatasetVisualisation
import com.example.chessmate.game.model.game.state.GameSnapshotState
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize

@Parcelize
object None : DatasetVisualisation {

    @IgnoredOnParcel
    override val name = R.string.ChessMatenone

    @IgnoredOnParcel
    override val minValue: Int = Int.MIN_VALUE

    @IgnoredOnParcel
    override val maxValue: Int = Int.MAX_VALUE

    override fun dataPointAt(
        position: Position,
        state: GameSnapshotState,
        cache: MutableMap<Any, Any>
    ): Datapoint? =
        null
}
