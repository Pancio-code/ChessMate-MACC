package com.example.chessmate.game.model.game.state

import android.os.Parcelable
import com.example.chessmate.game.model.board.Position
import com.example.chessmate.game.model.move.BoardMove
import com.example.chessmate.game.model.move.Capture
import com.example.chessmate.game.model.move.targetPositions
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize

@Parcelize
data class UiState(
    private val gameSnapshotState: GameSnapshotState,
    val selectedPosition: Position? = null,
    val showPromotionDialog: Boolean = false
) : Parcelable {

    @IgnoredOnParcel
    private val lastMovePositions: List<Position> =
        gameSnapshotState.lastMove?.let { listOf(it.from, it.to) } ?: emptyList()

    @IgnoredOnParcel
    private val uiSelectedPositions: List<Position> =
        selectedPosition?.let { listOf(it) } ?: emptyList()

    @IgnoredOnParcel
    val highlightedPositions: List<Position> =
        lastMovePositions + uiSelectedPositions

    @IgnoredOnParcel
    private val ownPiecePositions: List<Position> =
        gameSnapshotState.board.pieces
            .filter { (_, piece) -> piece.set == gameSnapshotState.toMove }
            .map { it.key }

    @IgnoredOnParcel
    val possibleCaptures: List<Position> =
        possibleMoves { it.preMove is Capture }.targetPositions()

    @IgnoredOnParcel
    val possibleMovesWithoutCaptures: List<Position> =
        possibleMoves { it.preMove !is Capture }.targetPositions()

    fun possibleMoves(predicate: (BoardMove) -> Boolean = { true }) =
        selectedPosition?.let {
            gameSnapshotState.legalMovesFrom(it)
                .filter(predicate)
        } ?: emptyList()

    @IgnoredOnParcel
    val clickablePositions: List<Position> =
        ownPiecePositions +
            possibleCaptures +
            possibleMovesWithoutCaptures

    private fun deselect(): UiState = copy(
        selectedPosition = null
    )

    fun select(position: Position): UiState =
        if (selectedPosition == position) {
            deselect()
        } else {
            copy(
                selectedPosition = position
            )
        }
}
