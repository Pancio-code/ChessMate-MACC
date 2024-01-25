package com.example.chessmate.multiplayer

import com.google.firebase.firestore.Exclude

data class RoomData(
    val roomId: String = "-1",
    val playerOneId: String = "",
    val playerTwoId: String? = null,
    val playerOneUsername: String = "",
    val playerTwoUsername: String? = null,
    val gameState: RoomStatus = RoomStatus.WAITING,
    val rankPlayerOne: Float = 0.0f,
    val rankPlayerTwo: Float? = null,
    val currentTurn: String? = null,
    val boardState: String = RoomDataHelper.PNG_START, // We use PNG notation to represent current board situation.
    val lastMove: String? = null,
    val winner: String = "",
    val termination: String= ""
)
{
    constructor() : this("-1", "", null,"",null,RoomStatus.WAITING,0.0f,null,null,RoomDataHelper.PNG_START,null,"","") {}
    @Exclude
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "roomId" to roomId,
            "playerOneId" to playerOneId,
            "playerTwoId" to playerTwoId,
            "playerOneUsername" to playerOneUsername,
            "playerTwoUsername" to playerTwoUsername,
            "gameState" to gameState,
            "rankPlayerOne" to rankPlayerOne,
            "rankPlayerTwo" to rankPlayerTwo,
            "currentTurn" to currentTurn,
            "boardState" to boardState,
            "lastMove" to lastMove,
            "winner" to winner,
            "termination" to termination
        )
    }
}

object RoomDataHelper {
    const val PNG_START = ""
}

enum class RoomStatus {
    WAITING,
    CREATED,
    JOINED,
    INPROGRESS,
    FINISHED
}

enum class GameType {
    ONLINE,
    TWO_OFFLINE,
    ONE_OFFLINE
}