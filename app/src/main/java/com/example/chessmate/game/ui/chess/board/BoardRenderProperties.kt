package com.example.chessmate.game.ui.chess.board

import androidx.compose.ui.unit.Dp
import com.example.chessmate.game.model.board.Position
import com.example.chessmate.game.model.game.state.GameSnapshotState
import com.example.chessmate.game.model.game.state.UiState

data class BoardRenderProperties(
    val fromState: GameSnapshotState,
    val toState: GameSnapshotState,
    val uiState: UiState,
    val isFlipped: Boolean,
    val squareSize: Dp,
    val onClick: (Position) -> Unit,
) {
    val cache: MutableMap<Any, Any> = mutableMapOf()
}
