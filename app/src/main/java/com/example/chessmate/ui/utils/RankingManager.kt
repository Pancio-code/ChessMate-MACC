package com.example.chessmate.ui.utils

import kotlin.math.pow

object RankingManager{

    private fun getDevelopmentCoefficient(ranking: Float, matches : Int) :Int {
        return when{
            matches <= 30 -> 40
            ranking <= 2400f -> 20
            else -> 10
        }
    }
    private fun probability(rating1: Float, rating2: Float): Float {
        return 1.0f * 1.0f / (1 + 1.0f * 10.0.pow((1.0f * (rating1 - rating2) / 400).toDouble()).toFloat())
    }

    fun eloRating(matchesPlayed : Int, Ra : Float,Rb : Float, d : Boolean) : Float {
        val k : Int = getDevelopmentCoefficient(Ra,matchesPlayed)
        val Pa : Float = probability(Rb, Ra)

        return if (d) {
            Ra + k * (1 - Pa)
        } else {
            Ra + k * (0 - Pa)
        }
    }
}