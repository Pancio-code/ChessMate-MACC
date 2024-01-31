package com.example.chessmate.game.model.game.controller

import android.util.Log
import com.example.chessmate.game.model.board.Position
import com.example.chessmate.game.model.data_chessmate.DatasetVisualisation
import com.example.chessmate.game.model.game.state.GameMetaInfo
import com.example.chessmate.game.model.game.state.GamePlayState
import com.example.chessmate.game.model.game.state.GameSnapshotState
import com.example.chessmate.game.model.game.state.GameState
import com.example.chessmate.game.model.game.state.PromotionState
import com.example.chessmate.game.model.game.state.UiState
import com.example.chessmate.game.model.move.BoardMove
import com.example.chessmate.game.model.piece.Piece

object Reducer {

    sealed class Action {
        object StepForward : Action()
        object StepBackward : Action()
        data class GoToMove(val moveIndex: Int) : Action()
        data class ResetTo(val gameSnapshotState: GameSnapshotState, val gameMetaInfo: GameMetaInfo) : Action()
        data class ToggleSelectPosition(val position: Position) : Action()
        data class ApplyMove(val boardMove: BoardMove) : Action()
        data class RequestPromotion(val at: Position) : Action()
        data class PromoteTo(val piece: Piece) : Action()
        data class SetVisualisation(val visualisation: DatasetVisualisation) : Action()
    }

    operator fun invoke(gamePlayState: GamePlayState, action: Action): GamePlayState {
        val gameState = gamePlayState.gameState
        val currentSnapshotState = gameState.currentSnapshotState

        return when (action) {
            is Action.StepForward -> gamePlayState.stepBy(1)
            is Action.StepBackward -> gamePlayState.stepBy(-1)
            is Action.GoToMove -> gamePlayState.goToSnapshot(action.moveIndex + 1)
            is Action.ResetTo -> {
                GamePlayState(
                    gameState = GameState(
                        gameMetaInfo = action.gameMetaInfo,
                        states = listOf(action.gameSnapshotState)
                    ),
                    visualisation = gamePlayState.visualisation
                )
            }
            is Action.ToggleSelectPosition -> {
                Log.d("Ciao","")
                if (gamePlayState.uiState.selectedPosition == action.position) {
                    Log.d("Ciao2","")
                    gamePlayState.copy(
                        uiState = gamePlayState.uiState.copy(
                            selectedPosition = null
                        )
                    )
                } else {
                    Log.d("Ciao3","")
                    gamePlayState.copy(
                        uiState = gamePlayState.uiState.copy(
                            selectedPosition = action.position
                        )
                    )
                }
            }
            is Action.ApplyMove -> {
                var states = gameState.states.toMutableList()
                val currentIndex = gameState.currentIndex
                val transition = currentSnapshotState.calculateAppliedMove(
                    boardMove = action.boardMove,
                    statesSoFar = states.subList(0, currentIndex + 1)
                )

                states[currentIndex] = transition.fromSnapshotState
                states = states.subList(0, currentIndex + 1)
                states.add(transition.toSnapshotState)

                gamePlayState.copy(
                    gameState = gameState.copy(
                        states = states,
                        currentIndex = states.lastIndex,
                        lastActiveState = currentSnapshotState,
                        gameMetaInfo = gameState.gameMetaInfo.withResolution(
                            resolution = transition.toSnapshotState.resolution,
                            lastMoveBy = transition.fromSnapshotState.toMove
                        )
                    ),
                    uiState = UiState(transition.toSnapshotState),
                    promotionState = PromotionState.None
                )
            }
            is Action.RequestPromotion -> {
                gamePlayState.copy(
                    uiState = gamePlayState.uiState.copy(
                        showPromotionDialog = true
                    ),
                    promotionState = PromotionState.Await(action.at)
                )
            }
            is Action.PromoteTo -> {
                gamePlayState.copy(
                    uiState = gamePlayState.uiState.copy(
                        showPromotionDialog = false
                    ),
                    promotionState = PromotionState.ContinueWith(action.piece)
                )
            }
            is Action.SetVisualisation -> {
                gamePlayState.copy(
                    visualisation = action.visualisation
                )
            }
        }
    }

    private fun GamePlayState.stepBy(step: Int): GamePlayState {
        val newIndex = gameState.currentIndex + step
        Log.d("move", newIndex.toString())
        if (newIndex !in 0..gameState.states.lastIndex) return this
        return goToSnapshot(newIndex)
    }

    private fun GamePlayState.goToSnapshot(index: Int): GamePlayState {
        if (index !in 0..gameState.states.lastIndex) return this

        return copy(
            gameState = gameState.copy(
                currentIndex = index,
                lastActiveState = gameState.currentSnapshotState
            ),
            uiState = UiState(gameState.states[index])
        )
    }
}
