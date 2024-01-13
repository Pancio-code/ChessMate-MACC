package com.example.chessmate.game.model.game.converter

import com.example.chessmate.game.model.game.state.GameState

sealed class ImportResult {
    class ValidationError(val msg: String) : ImportResult()
    class ImportedGame(val gameState: GameState) : ImportResult()
}
