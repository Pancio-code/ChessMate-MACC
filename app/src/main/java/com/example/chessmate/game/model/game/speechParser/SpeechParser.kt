package com.example.chessmate.game.model.game.speechParser

import com.example.chessmate.game.model.board.Position
import java.util.Locale


object SpeechParser{

    fun parseSpeechToMove(textSpoken: String): Position? {

        val textSpokenParsed = parseString(textSpoken)
        if (textSpokenParsed.isNullOrEmpty()){
            return null
        }

        val position: Position? = try {
            Position.valueOf(textSpokenParsed)
        } catch (e: IllegalArgumentException) {
            return null
        }

        return position
    }

    private fun parseString(textSpoken: String): String? {
        val possibleValue = "abcdefgh"
        val row: String
        val column: String
        if (textSpoken.length > 2 && textSpoken.contains(' ')){
            if(textSpoken.split(" ")[0].length != 1) {
                row = textSpoken.split(" ")[0][0].toString()
                column = convertNumericWordsToDigits(textSpoken.split(" ")[1])
                if(possibleValue.indexOf(row) == -1){
                    return null
                }
            } else {
                row = textSpoken.split(" ")[0]
                column = convertNumericWordsToDigits(textSpoken.split(" ")[1])
            }
        } else if (textSpoken.length > 2 && !textSpoken.contains(' ')){
            row = textSpoken[0].toString()
            column = convertNumericWordsToDigits(textSpoken.substring(1))
        } else if (textSpoken.length == 2) {
            row = textSpoken[0].toString()
            column = textSpoken[1].toString()
        } else {
            return null
        }
        return "${row}${column}".lowercase()
    }

    private fun convertNumericWordsToDigits(textSpoken: String): String {

        val wordToDigitMap = mapOf(
            "one" to "1",
            "two" to "2",
            "three" to "3",
            "four" to "4",
            "five" to "5",
            "six" to "6",
            "seven" to "7",
            "eight" to "8",
            "uno" to "1",
            "due" to "2",
            "tre" to "3",
            "quattro" to "4",
            "cinque" to "5",
            "sei" to "6",
            "sette" to "7",
            "otto" to "8",
        )

        return textSpoken.split(" ").joinToString(" ") { word ->
            wordToDigitMap[word.lowercase(Locale.getDefault())] ?: word
        }
    }

}