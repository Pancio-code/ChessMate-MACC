package com.example.chessmate.game.model.game.converter

import com.example.chessmate.game.model.board.Position
import com.example.chessmate.game.model.game.state.GameMetaInfo
import com.example.chessmate.game.model.game.state.GamePlayState
import com.example.chessmate.game.model.game.state.GameSnapshotState
import com.example.chessmate.game.model.game.state.GameState
import com.example.chessmate.game.model.piece.Bishop
import com.example.chessmate.game.model.piece.King
import com.example.chessmate.game.model.piece.Knight
import com.example.chessmate.game.model.piece.Pawn
import com.example.chessmate.game.model.piece.Piece
import com.example.chessmate.game.model.piece.Queen
import com.example.chessmate.game.model.piece.Rook
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

    fun fenToMap(fen: String): Map<Position, Piece> {
        val parts = fen.split(" ")
        val rows = parts[0].split("/")
        val pieceMap = mutableMapOf<Position, Piece>()

        for ((rowIndex, row) in rows.withIndex()) {
            var columnIndex = 0
            for (char in row) {
                val position = enumValueOf<Position>("${('a' + columnIndex)}${8 - rowIndex}")
                when (char) {
                    'r' -> pieceMap[position] = Rook(Set.BLACK)
                    'n' -> pieceMap[position] = Knight(Set.BLACK)
                    'b' -> pieceMap[position] = Bishop(Set.BLACK)
                    'q' -> pieceMap[position] = Queen(Set.BLACK)
                    'k' -> pieceMap[position] = King(Set.BLACK)
                    'p' -> pieceMap[position] = Pawn(Set.BLACK)
                    'R' -> pieceMap[position] = Rook(Set.WHITE)
                    'N' -> pieceMap[position] = Knight(Set.WHITE)
                    'B' -> pieceMap[position] = Bishop(Set.WHITE)
                    'Q' -> pieceMap[position] = Queen(Set.WHITE)
                    'K' -> pieceMap[position] = King(Set.WHITE)
                    'P' -> pieceMap[position] = Pawn(Set.WHITE)
                    else -> columnIndex += char.toString().toInt() - 1
                }
                columnIndex++
            }
        }

        return pieceMap
    }

    fun getFenFromSnapshot(gameSnapshotState: GameSnapshotState): String {
        val pieces: Map<Position, Piece> = gameSnapshotState.board.pieces
        val toMove: Char = if (gameSnapshotState.toMove == Set.WHITE) 'w' else 'b'

        val whiteCastling = gameSnapshotState.castlingInfo[Set.WHITE].let {
            "${if (it.canCastleKingSide) "K" else ""}${if (it.canCastleQueenSide) "Q" else ""}"
        }
        val blackCastling = gameSnapshotState.castlingInfo[Set.BLACK].let {
            "${if (it.canCastleKingSide) "k" else ""}${if (it.canCastleQueenSide) "q" else ""}"
        }
        val castlingAvailability = if (whiteCastling.isEmpty() && blackCastling.isEmpty()) "-" else "$whiteCastling$blackCastling"

        val enPassantTarget = "-"
        val halfMoveClock = 0
        val fullMoveNumber = 1

        val board = Array(8) { CharArray(8) { ' ' } }

        // Place pieces on the board
        for ((position, piece) in pieces) {
            val rank = 7 - (position.ordinal / 8) // Convert to 0-based index, with rank 8 at the top
            val file = position.ordinal % 8
            board[rank][file] = piece.toFenChar()
        }

        // Convert the board to FEN piece placement
        val piecePlacement = board.joinToString("/") { row ->
            row.joinToString("").replace(Regex(" +")) { match -> match.value.length.toString() }
        }

        return "$piecePlacement $toMove $castlingAvailability $enPassantTarget $halfMoveClock $fullMoveNumber"
    }

    private fun Piece.toFenChar(): Char = when (this) {
        is King -> if (this.set == Set.WHITE) 'K' else 'k'
        is Queen -> if (this.set == Set.WHITE) 'Q' else 'q'
        is Rook -> if (this.set == Set.WHITE) 'R' else 'r'
        is Bishop -> if (this.set == Set.WHITE) 'B' else 'b'
        is Knight -> if (this.set == Set.WHITE) 'N' else 'n'
        is Pawn -> if (this.set == Set.WHITE) 'P' else 'p'
        else -> '?'
    }
}