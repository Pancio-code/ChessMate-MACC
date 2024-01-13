package com.example.chessmate.game.ui.chess

import com.example.chessmate.game.model.piece.Bishop
import com.example.chessmate.game.model.piece.Knight
import com.example.chessmate.game.model.piece.Pawn
import com.example.chessmate.game.model.piece.Queen
import com.example.chessmate.game.model.piece.Rook
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.chessmate.game.model.game.state.GameMetaInfo
import com.example.chessmate.game.model.game.state.GameState
import com.example.chessmate.game.model.game.state.GameSnapshotState
import com.example.chessmate.game.model.piece.Piece
import com.example.chessmate.game.model.piece.Set
import com.example.chessmate.game.model.piece.Set.BLACK
import com.example.chessmate.game.model.piece.Set.WHITE
import com.example.chessmate.ui.theme.ChessMateTheme
import kotlin.math.absoluteValue

@Composable
fun CapturedPieces(
    gameState: GameState,
    capturedBy: Set,
    arrangement: Arrangement.Horizontal,
    scoreAlignment: Alignment.Horizontal,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(36.dp)
            .padding(top = 2.dp, bottom = 2.dp)
            .background(MaterialTheme.colorScheme.background)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 16.dp, end = 16.dp),
            horizontalArrangement = arrangement,
            verticalAlignment = Alignment.CenterVertically
        ) {
            val capturedPieces = gameState.currentSnapshotState.capturedPieces
                .filter { it.set == capturedBy.opposite() }
                .sortedWith { t1, t2 ->
                    if (t1.value == t2.value) t1.symbol.hashCode() - t2.symbol.hashCode()
                    else t1.value - t2.value
                }

            val score = gameState.currentSnapshotState.score
            val displayScore = (capturedBy == BLACK && score < 0) || (capturedBy == WHITE && score > 0)
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (scoreAlignment == Alignment.Start && displayScore) {
                    Score(score = score)
                }
                CapturedPieceList(
                    capturedPieces = capturedPieces,
                )
                if (scoreAlignment == Alignment.End && displayScore) {
                    Score(score = score)
                }
            }
        }
    }
}

@Composable
private fun CapturedPieceList(
    capturedPieces: List<Piece>
) {
    val stringBuilder = StringBuilder()
    capturedPieces.forEach { piece ->
        stringBuilder.append(piece.symbol)
    }

    Text(
        text = stringBuilder.toString(),
        color = MaterialTheme.colorScheme.onBackground,
        fontSize = 20.sp
    )
}

@Composable
private fun Score(score: Int) {
    Text(
        text = "+${score.absoluteValue}",
        color = MaterialTheme.colorScheme.onBackground,
        fontSize = 12.sp,
        modifier = Modifier.padding(
            start = 8.dp,
            end = 8.dp
        ),
    )
}

@Preview
@Composable
fun TakenPiecesPreview() {
    ChessMateTheme {
        CapturedPieces(
            gameState = GameState(
                gameMetaInfo = GameMetaInfo.createWithDefaults(),
                states = listOf(
                    GameSnapshotState(
                        capturedPieces = listOf(
                            Pawn(WHITE),
                            Pawn(WHITE),
                            Pawn(WHITE),
                            Pawn(WHITE),
                            Knight(WHITE),
                            Knight(WHITE),
                            Bishop(WHITE),
                            Queen(WHITE),

                            Pawn(BLACK),
                            Pawn(BLACK),
                            Pawn(BLACK),
                            Pawn(BLACK),
                            Knight(BLACK),
                            Rook(BLACK),
                        )
                    )
                )
            ),
            capturedBy = BLACK,
            Arrangement.End,
            Alignment.Start
        )
    }
}
