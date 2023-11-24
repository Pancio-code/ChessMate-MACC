package com.example.chessmate.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.chessmate.ui.theme.light_primary
import com.example.chessmate.ui.theme.iconScore

@Composable
@Preview
fun Score() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        ScoreCard("Singleplayer", 2)
        Spacer(modifier = Modifier.width(16.dp))
        ScoreCard("Multiplayer", 10)
        Spacer(modifier = Modifier.width(16.dp))
        ScoreCard("Total Wins", 5)
    }
}


@Composable
fun ScoreCard(
    name: String,
    count: Int = 0
) {
    OutlinedCard(
        colors = CardDefaults.cardColors(
            containerColor = light_primary,
        ),
        border = BorderStroke(1.dp, Color.Black),
        modifier = Modifier
            .size(width = 100.dp, height = 100.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxHeight()
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(1f),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ){
                Text(
                    text = "$name",
                    textAlign = TextAlign.Center,
                    fontSize = 14.sp,
                    color = Color.LightGray
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            Row(
                modifier = Modifier.fillMaxWidth(1f),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ){
                if(name == "Singleplayer") {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "User Icon",
                        modifier = Modifier.size(24.dp),
                        tint = Color.Red
                    )
                } else if (name == "Multiplayer"){
                    Icon(
                        imageVector = Icons.Default.Group,
                        contentDescription = "Two People Icon",
                        modifier = Modifier
                            .size(24.dp),
                        tint = Color.Blue
                    )
                } else {
                    Icon(
                        imageVector = Icons.Default.EmojiEvents,
                        contentDescription = "Win",
                        modifier = Modifier
                            .size(24.dp),
                        tint = Color.Green
                    )
                }
            }
            Spacer(modifier = Modifier.height(4.dp))
            Row(
                modifier = Modifier.fillMaxWidth(1f),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ){
                Text(
                    text = "$count",
                    textAlign = TextAlign.Center,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }

    }
}