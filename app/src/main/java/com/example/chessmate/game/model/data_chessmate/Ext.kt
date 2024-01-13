package com.example.chessmate.game.model.data_chessmate

import androidx.compose.runtime.compositionLocalOf
import com.example.chessmate.game.model.data_chessmate.impl.ActivePieces
import com.example.chessmate.game.model.data_chessmate.impl.BlackKingsEscape
import com.example.chessmate.game.model.data_chessmate.impl.CheckmateCount
import com.example.chessmate.game.model.data_chessmate.impl.Influence
import com.example.chessmate.game.model.data_chessmate.impl.KnightsMoveCount
import com.example.chessmate.game.model.data_chessmate.impl.BlockedPieces
import com.example.chessmate.game.model.data_chessmate.impl.None
import com.example.chessmate.game.model.data_chessmate.impl.WhiteKingsEscape

val datasetVisualisations = listOf(
    None,
    ActivePieces,
    BlockedPieces,
    Influence,
    BlackKingsEscape,
    WhiteKingsEscape,
    KnightsMoveCount,
    CheckmateCount
)

val LocalActiveDatasetVisualisation  = compositionLocalOf<DatasetVisualisation> { None }
