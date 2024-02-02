package com.example.chessmate.game.model.game.controller

import android.util.Log
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import com.example.chessmate.game.model.board.Position
import com.example.chessmate.game.model.board.Square
import com.example.chessmate.game.model.data_chessmate.DatasetVisualisation
import com.example.chessmate.game.model.game.Resolution
import com.example.chessmate.game.model.game.controller.Reducer.Action
import com.example.chessmate.game.model.game.converter.FenConverter
import com.example.chessmate.game.model.game.preset.Preset
import com.example.chessmate.game.model.game.state.GameMetaInfo
import com.example.chessmate.game.model.game.state.GamePlayState
import com.example.chessmate.game.model.game.state.GameSnapshotState
import com.example.chessmate.game.model.game.state.PromotionState
import com.example.chessmate.game.model.move.BoardMove
import com.example.chessmate.game.model.move.Promotion
import com.example.chessmate.game.model.move.targetPositions
import com.example.chessmate.game.model.piece.Piece
import com.example.chessmate.game.model.piece.Queen
import com.example.chessmate.game.model.piece.Set
import com.example.chessmate.game.ui.app.convertToPiece
import com.example.chessmate.multiplayer.GameType
import com.example.chessmate.multiplayer.OnlineUIClient
import com.example.chessmate.multiplayer.RoomData
import com.example.chessmate.multiplayer.RoomStatus
import com.example.chessmate.ui.utils.HelperClassStockFish
import com.example.chessmate.ui.utils.StockFishAPI
import com.example.chessmate.ui.utils.StockFishData
import kotlinx.coroutines.launch
import retrofit2.Response
import java.util.Locale
import kotlin.coroutines.cancellation.CancellationException
import kotlin.random.Random

class GameController(
    val getGamePlayState: () -> GamePlayState,
    private val setGamePlayState: ((GamePlayState) -> Unit)? = null,
    preset: Preset? = null,
    private val startColor: Set? = null,
    private val gameType : GameType? = null,
    private val onlineUIClient: OnlineUIClient? = null,
    private val roomData: RoomData? = null
) {
    private val stockFishService : StockFishAPI = HelperClassStockFish.getIstance()
    private val mode : String = "bestmove"
    private var promotionPiece : String = "q"

    init {
        preset?.let { applyPreset(it) }
    }

    private val gamePlayState: GamePlayState
        get() = getGamePlayState()

    private val gameSnapshotState: GameSnapshotState
        get() = gamePlayState.gameState.currentSnapshotState

    val toMove: Set
        get() = gameSnapshotState.toMove

    fun reset(
        gameSnapshotState: GameSnapshotState = GameSnapshotState(),
        gameMetaInfo: GameMetaInfo = GameMetaInfo.createWithDefaults()
    ) {
        setGamePlayState?.invoke(
            Reducer(gamePlayState, Action.ResetTo(gameSnapshotState, gameMetaInfo))
        )
    }

    private fun applyPreset(preset: Preset) {
        reset()
        preset.apply(this)
    }

    fun square(position: Position): Square =
        gameSnapshotState.board[position]

    private fun Position.hasOwnPiece() =
        square(this).hasPiece(gameSnapshotState.toMove)

    fun onClick(position: Position) {
        if (gameSnapshotState.resolution != Resolution.IN_PROGRESS) { return}
        if (gameType != GameType.TWO_OFFLINE && startColor != gameSnapshotState.toMove)  { return}
        if (position.hasOwnPiece()) {
            toggleSelectPosition(position)
        } else if (canMoveTo(position)) {
            val selectedPosition = gamePlayState.uiState.selectedPosition
            requireNotNull(selectedPosition)
            applyMove(selectedPosition, position)
            if (gameType == GameType.ONLINE && gamePlayState.promotionState is PromotionState.None ) {
                Log.d("ONLINE",position.toString())
                roomData?.let {
                    onlineUIClient!!.updateRoomData(
                        model = it.copy(
                            gameState = if (gameSnapshotState.resolution != Resolution.IN_PROGRESS) RoomStatus.FINISHED else RoomStatus.INPROGRESS,
                            currentTurn =  gameSnapshotState.toMove.name,
                            lastMove = "${gameSnapshotState.lastMove?.from} ${gameSnapshotState.lastMove?.to}",
                            fen = FenConverter.getFenFromSnapshot(gameSnapshotState,gamePlayState),
                            winner =  if (!gamePlayState.gameState.gameMetaInfo.result.isNullOrEmpty()) gamePlayState.gameState.gameMetaInfo.result!!  else "",
                            termination =  if (!gamePlayState.gameState.gameMetaInfo.termination.isNullOrEmpty()) gamePlayState.gameState.gameMetaInfo.termination!! else ""
                        )
                    )
                }
            }
        }
    }

    fun onResponse(lastMove : String) {
        Log.d("MOVE",lastMove)
        val moves = lastMove.split(" ")
        if(moves.size == 2) {
            val fromPosition = enumValueOf<Position>(moves[0])
            val toPosition = enumValueOf<Position>(moves[1])
            toggleSelectPosition(fromPosition)
            if (canMoveTo(toPosition)) {
                val selectedPosition = gamePlayState.uiState.selectedPosition
                requireNotNull(selectedPosition)
                applyMove(selectedPosition, toPosition)
            }
        } else {
            promotionPiece = moves[0]
            val piece: Piece = convertToPiece(gameSnapshotState.toMove, this)
            onPromotionPieceSelected(piece)
        }
    }



     fun onPcTurn(lifecycleOwner: LifecycleOwner, depth: Int = 5) {
        lifecycleOwner.lifecycleScope.launch {
            makePcMove(depth = depth)
        }
    }

    private suspend fun makePcMove(depth: Int = 5) {
        try {
            val stockFishResponse : Response<StockFishData> = stockFishService.get(fen = FenConverter.getFenFromSnapshot(gameSnapshotState,gamePlayState), depth = depth, mode= mode)
            val responseBody : StockFishData = stockFishResponse.body() ?: throw Exception("No reply from API")
            val bestMove = responseBody.data?.substringAfter("bestmove ")?.substringBefore("ponder") ?: throw Exception("Invalid move format")
            Log.d("AI",responseBody.data)
            val fromPosition =  enumValueOf<Position>(bestMove.substring(0,2))
            val toPosition = enumValueOf<Position>(bestMove.substring(2,4))
            val promotion = if(bestMove.length > 4) bestMove.substring(4,5) else " "
            if (promotion != " ") {
                promotionPiece = promotion
            }

            toggleSelectPosition(fromPosition)
            if (canMoveTo(toPosition)) {
                val selectedPosition = gamePlayState.uiState.selectedPosition
                requireNotNull(selectedPosition)
                applyMove(selectedPosition, toPosition)
            } else {
                throw Exception("Illegal Move")
            }
        } catch (e : Exception) {
            e.printStackTrace()
            if (e is CancellationException) throw e
            val availablePieces = gameSnapshotState.board.pieces.filter { x ->
                gameSnapshotState.legalMovesFrom(
                    x.key
                ).isNotEmpty()
            }
            val fromPosition = availablePieces.keys.elementAt(Random.nextInt(availablePieces.size))
            val toPosition = gameSnapshotState.legalMovesFrom(fromPosition)[0].to

            toggleSelectPosition(fromPosition)
            if (canMoveTo(toPosition)) {
                val selectedPosition = gamePlayState.uiState.selectedPosition
                requireNotNull(selectedPosition)
                applyMove(selectedPosition, toPosition)
            }
        }
    }

    private fun toggleSelectPosition(position: Position) {
        setGamePlayState?.invoke(
            Reducer(gamePlayState, Action.ToggleSelectPosition(position))
        )
    }

    fun getPcPromotionPiece() : String {
        return promotionPiece;
    }

    private fun canMoveTo(position: Position) =
        position in gamePlayState.uiState.possibleMoves().targetPositions()

    fun applyMove(from: Position, to: Position) {
        val boardMove = findBoardMove(from, to) ?: return
        applyMove(boardMove)
    }

    fun applyMove(boardMove: BoardMove) {
        setGamePlayState?.invoke(
            Reducer(gamePlayState, Action.ApplyMove(boardMove))
        )
    }

    private fun findBoardMove(from: Position, to: Position): BoardMove? {
        val legalMoves = gameSnapshotState
            .legalMovesFrom(from)
            .filter { it.to == to }

        return when {
            legalMoves.isEmpty() -> {
                throw IllegalArgumentException("No legal moves exist between $from -> $to")
            }
            legalMoves.size == 1 -> {
                legalMoves.first()
            }
            legalMoves.all { it.consequence is Promotion } -> {
                handlePromotion(to, legalMoves)
            }
            else -> {
                throw IllegalStateException("Legal moves: $legalMoves")
            }
        }
    }

    private fun handlePromotion(at: Position, legalMoves: List<BoardMove>): BoardMove? {
        var promotionState = gamePlayState.promotionState
        if (setGamePlayState == null && promotionState == PromotionState.None) {
            promotionState = PromotionState.ContinueWith(Queen(gameSnapshotState.toMove))
        }

        when (val promotion = promotionState) {
            is PromotionState.None -> {
                setGamePlayState?.invoke(
                    Reducer(gamePlayState, Action.RequestPromotion(at))
                )
            }
            is PromotionState.Await -> {
                throw IllegalStateException()
            }
            is PromotionState.ContinueWith -> {
                return legalMoves.find { move ->
                    (move.consequence as Promotion).let {
                        it.piece::class == promotion.piece::class
                    }
                }
            }
        }
        return null
    }

    fun onPromotionPieceSelected(piece: Piece) {
        val state = gamePlayState.promotionState
        if (state !is PromotionState.Await) error("Not in expected state: $state")
        val position = state.position
        setGamePlayState?.invoke(
            Reducer(gamePlayState, Action.PromoteTo(piece))
        )
        if(gameType == GameType.TWO_OFFLINE || startColor == gameSnapshotState.toMove) {
            onClick(position)
        } else {
            val selectedPosition = gamePlayState.uiState.selectedPosition
            requireNotNull(selectedPosition)
            applyMove(selectedPosition, position)
        }
        if (gameType == GameType.ONLINE) {
            roomData?.let {
                onlineUIClient!!.updateRoomData(
                    model = it.copy(
                        gameState = if (gamePlayState.gameState.resolution != Resolution.IN_PROGRESS) RoomStatus.FINISHED else RoomStatus.INPROGRESS,
                        currentTurn =  gamePlayState.gameState.toMove.name,
                        fen = FenConverter.getFenFromSnapshot(gamePlayState.gameState.currentSnapshotState,gamePlayState),
                        lastMove = piece.textSymbol.lowercase(Locale.ROOT)
                    )
                )
            }
        }
    }

    fun setVisualisation(visualisation: DatasetVisualisation) {
        setGamePlayState?.invoke(
            Reducer(gamePlayState, Action.SetVisualisation(visualisation))
        )
    }

    fun stepForward() {
        setGamePlayState?.invoke(
            Reducer(gamePlayState, Action.StepForward)
        )
    }

    fun stepBackward() {
        setGamePlayState?.invoke(
            Reducer(gamePlayState, Action.StepBackward)
        )
    }

    fun goToMove(index: Int) {
        setGamePlayState?.invoke(
            Reducer(gamePlayState, Action.GoToMove(index))
        )
    }

    fun selectBySpeech(position: Position, onFinish: (Boolean) -> Unit, onError: () -> Unit){
        if (position.hasOwnPiece()) {
            toggleSelectPosition(position)
            if (gamePlayState.uiState.possibleMoves().isEmpty()){
                toggleSelectPosition(position)
                onError()
                return
            }
            onFinish(true)
        } else {
            onError()
        }
    }

    fun moveBySpeech(position: Position, onFinish: (Boolean) -> Unit, onError: () -> Unit){
        if (canMoveTo(position)) {
            val selectedPosition = gamePlayState.uiState.selectedPosition
            requireNotNull(selectedPosition)
            applyMove(selectedPosition, position)
            onFinish(false)
        } else {
            onError()
        }
    }

}

