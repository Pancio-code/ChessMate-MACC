package com.example.chessmate.game.model.data_chessmate

import android.os.Parcelable
import com.example.chessmate.game.model.board.Position
import com.example.chessmate.game.model.game.state.GameSnapshotState

interface DatasetVisualisation : Parcelable {

    val name: Int

    val minValue: Int

    val maxValue: Int

    fun dataPointAt(position: Position, state: GameSnapshotState, cache: MutableMap<Any, Any>): Datapoint?
}

