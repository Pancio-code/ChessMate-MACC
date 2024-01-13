package com.example.chessmate.game.model.game.state

import android.os.Parcelable
import com.example.chessmate.game.model.data_chessmate.DatasetVisualisation
import com.example.chessmate.game.model.data_chessmate.impl.None
import kotlinx.parcelize.Parcelize

@Parcelize
data class GamePlayState(
    val gameState: GameState = GameState(GameMetaInfo.createWithDefaults()),
    val uiState: UiState = UiState(gameState.currentSnapshotState),
    val promotionState: PromotionState = PromotionState.None,
    val visualisation: DatasetVisualisation = None
) : Parcelable
