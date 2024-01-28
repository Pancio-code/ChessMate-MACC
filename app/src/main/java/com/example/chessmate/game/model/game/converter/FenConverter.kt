package com.example.chessmate.game.model.game.converter

import com.example.chessmate.game.model.game.state.GameMetaInfo
import com.example.chessmate.game.model.game.state.GamePlayState
import com.example.chessmate.game.model.game.state.GameState
import com.example.chessmate.game.model.piece.Set

object FenConverter : Converter{
     override fun preValidate(text: String): Boolean {
         val parts = text.trim().split(Regex("\\s+"))
         if (parts.size != 6) return false

         val rows = parts[0].split('/')
         if (rows.size != 8 || rows.any { row -> row.sumOf { ch -> if (ch.isDigit()) ch.toString().toInt() else 1 } != 8 }) return false

         if (!parts[1].matches(Regex("^[wb]$"))) return false

         if (!parts[2].matches(Regex("^(KQ?k?q?|Qk?q?|kq?|q|-)$"))) return false

         if (!parts[3].matches(Regex("^(-|[abcdefgh][36])$"))) return false

         parts[4].toIntOrNull()?.let {
             if (it < 0) return false
         } ?: return false

         parts[5].toIntOrNull()?.let {
             if (it < 1) return false
         } ?: return false

         return true
    }

     override fun import(text: String): ImportResult {
        val gamePlayState = GamePlayState(
                    stringFEN = text,
                    gameState = GameState(
                        stringFEN = text,
                        gameMetaInfo = GameMetaInfo.createWithDefaults()
                    )
        )

        return ImportResult.ImportedGame(gamePlayState.gameState)
    }

    override fun export(gameState: GameState): String {
        TODO("Not yet implemented")
    }

    fun startColor(text: String) : Set {
        if (!preValidate(text = text)) {
            return Set.WHITE
        }
        val fields = text.split(" ")
        return when (fields[1]) {
            "w" -> Set.WHITE
            "b" -> Set.BLACK
            else -> throw IllegalArgumentException("Invalid color field in FEN string.")
        }
    }
}