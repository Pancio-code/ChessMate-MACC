package com.example.chessmate.ui.utils

import kotlin.math.pow

class RankingManager(eloRanking: Float, matchesPlayed : Int) {
    private val k : Int = getDevelopmentCoefficient(eloRanking,matchesPlayed)

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

    fun eloRating(Ra : Float,Rb : Float, d : Boolean) : Pair<Float,Float> {
        val Pb : Float = probability(Ra, Rb)
        val Pa : Float = probability(Rb, Ra)

        return if (d) {
            Pair(Ra + k * (1 - Pa), Rb + k * (0 - Pb))
        } else {
            Pair(Ra + k * (0 - Pa), Rb + k * (1 - Pb))
        }
    }
}