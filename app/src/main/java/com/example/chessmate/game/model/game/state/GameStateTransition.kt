package com.example.chessmate.game.model.game.state

import com.example.chessmate.game.model.move.AppliedMove

data class GameStateTransition(
    val fromSnapshotState: GameSnapshotState,
    val toSnapshotState: GameSnapshotState,
    val move: AppliedMove
)
