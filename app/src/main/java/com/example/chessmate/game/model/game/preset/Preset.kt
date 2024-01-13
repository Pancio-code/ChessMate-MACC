package com.example.chessmate.game.model.game.preset

import com.example.chessmate.game.model.game.controller.GameController

interface Preset {
    fun apply(gameController: GameController)
}
