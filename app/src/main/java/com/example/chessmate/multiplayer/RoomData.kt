package com.example.chessmate.multiplayer

data class RoomData(
    val roomId: String = "-1",
    val playerOneId: String = "",
    val playerTwoId: String? = null,
    val playerOneUsername: String = "",
    val playerTwoUsername: String? = null,
    val pictureUrlOne : String = "",
    val pictureUrlTwo : String? = null,
    val gameState: RoomStatus = RoomStatus.WAITING,
    val rankPlayerOne: Float = 0.0f,
    val rankPlayerTwo: Float? = null,
    val currentTurn: String? = null,
    val boardState: String = RoomDataHelper.PNG_START, // We use PNG notation to represent current board situation.
    val lastMove: String? = null,
    val winner: String = "",
    val termination: String= "",
    val fen: String = ""
)
{
    constructor() : this("-1", "", null,"",null,"",null,RoomStatus.WAITING,0.0f,null,null,RoomDataHelper.PNG_START,null,"","") {}
}

object RoomDataHelper {
    const val PNG_START = ""
    const val FEN_START = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1"
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

enum class GameDifficulty {
    EASY,
    MEDIUM,
    HARD,
    EXTREME,
    IMPOSSIBLE
}