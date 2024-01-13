package com.example.chessmate.game.model.game.converter

import com.example.chessmate.game.model.game.state.GameMetaInfo

data class PgnImportDataHolder(
    val metaInfo: GameMetaInfo,
    val moves: List<String>
)
