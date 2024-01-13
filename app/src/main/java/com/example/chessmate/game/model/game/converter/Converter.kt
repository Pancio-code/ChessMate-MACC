package com.example.chessmate.game.model.game.converter

import com.example.chessmate.game.model.game.state.GameState

interface Converter {

    fun preValidate(text: String): Boolean

    fun import(text: String): ImportResult

    fun export(gameState: GameState): String
}

