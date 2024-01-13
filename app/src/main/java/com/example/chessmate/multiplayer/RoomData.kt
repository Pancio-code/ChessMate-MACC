package com.example.chessmate.multiplayer

data class RoomData(
    val roomId: String? = null,
    val playerOneId: String? = null,
    val playerTwoId: String? = null,
    val isFree: Boolean = true,
    val gameState: String? = RoomDataHelper.GAME_STATE,
    val lastOnlinePlayerOne: String? = null,
    val lastOnlinePlayerTwo: String? = null,
    val rankPlayerOne: Float? = null,
    val rankPlayerTwo: Float? = null,
    val dataCreation: String? = null,
    val currentTurn: String? = null,
    val boardState: String? = RoomDataHelper.FEN_START, // We use FEN notation to represent current board situation.
    val lastMove: String? = null,
    val winner: String = "",
    val termination: String= ""
)

object RoomDataHelper {
    const val FEN_START = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1"
    const val GAME_STATE = "Pending"
}