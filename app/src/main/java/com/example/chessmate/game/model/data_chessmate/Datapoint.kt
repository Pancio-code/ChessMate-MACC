package com.example.chessmate.game.model.data_chessmate

import androidx.compose.ui.graphics.Color

data class Datapoint(
    val value: Int?,
    val label: String?,
    val colorScale: Pair<Color, Color>,
)
