package com.example.chessmate.game.model.game.state

import android.os.Parcelable
import com.example.chessmate.game.model.game.Resolution
import com.example.chessmate.game.model.game.converter.FenConverter
import com.example.chessmate.game.model.move.AppliedMove
import com.example.chessmate.game.model.piece.Set
import kotlinx.parcelize.Parcelize

@Parcelize
data class GameState(
    val stringFEN: String? = null,
    val gameMetaInfo: GameMetaInfo,
    val states: List<GameSnapshotState> = if (stringFEN != null) listOf(GameSnapshotState(stringFEN = stringFEN,toMove = FenConverter.startColor(stringFEN))) else listOf(GameSnapshotState()),
    val currentIndex: Int = 0,
    val lastActiveState: GameSnapshotState = states.first(),
) : Parcelable {
    val hasPrevIndex: Boolean
        get() = currentIndex > 0

    val hasNextIndex: Boolean
        get() = currentIndex < states.lastIndex

    val currentSnapshotState: GameSnapshotState
        get() = states[currentIndex]

    val toMove: Set
        get() = currentSnapshotState.toMove

    val resolution: Resolution
        get() = currentSnapshotState.resolution

    fun moves(): List<AppliedMove> =
        states.mapNotNull { gameState -> gameState.move }
}

