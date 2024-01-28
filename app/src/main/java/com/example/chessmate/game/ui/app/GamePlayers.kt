package com.example.chessmate.game.ui.app

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.chessmate.R
import com.example.chessmate.game.model.piece.Set
import com.example.chessmate.multiplayer.GameType
import com.example.chessmate.multiplayer.RoomData
import com.example.chessmate.sign_in.UserData

@Composable
fun GamePlayers(gameType: GameType, roomData: RoomData? = null, userData: UserData? = null, startColor : Set? = Set.WHITE) {
    val playerOne : String = when(gameType) {
        GameType.TWO_OFFLINE -> "Player 1"
        GameType.ONE_OFFLINE -> {
            if (startColor == Set.WHITE) {
                if (userData != null) {
                    userData.username!!
                } else {
                    "Guest"
                }
            } else {
                "AI Player"
            }
        }
        GameType.ONLINE -> roomData!!.playerOneUsername
    }
    val playerTwo : String = when(gameType) {
        GameType.TWO_OFFLINE -> "Player 2"
        GameType.ONE_OFFLINE ->  {
            if (startColor == Set.BLACK) {
                if (userData != null) {
                    userData.username!!
                } else {
                    "Guest"
                }
            } else {
                "AI Player"
            }
        }
        GameType.ONLINE ->  roomData!!.playerTwoUsername!!
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(36.dp)
            .background(MaterialTheme.colorScheme.primaryContainer),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column (
            modifier = Modifier.fillMaxWidth(0.5f)
        ){
            Row (
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ){
                Text(
                    text = playerOne,
                    modifier = Modifier.padding(start = 16.dp),
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
                Spacer(modifier = Modifier.width(10.dp))
                Image(
                    painter = painterResource(id = R.drawable.chess_bishop_light),
                    contentDescription = "Icon WHITE",
                    modifier = Modifier.size(17.dp)
                )
            }
        }
        Column (
            modifier = Modifier.fillMaxWidth()
        ){
            Row (
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Absolute.Right
            ){
                Text(
                    text = playerTwo,
                    modifier = Modifier.padding(start = 16.dp),
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
                Spacer(modifier = Modifier.width(10.dp))
                Image(
                    painter = painterResource(id = R.drawable.chess_bishop_dark),
                    contentDescription = "Icon WHITE",
                    modifier = Modifier.size(17.dp)
                )
                Spacer(modifier = Modifier.width(10.dp))
            }
        }
    }
}