package com.example.chessmate.ui.components

import androidx.compose.foundation.ExperimentalFoundationApi
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.chessmate.R

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun RecentMatches(matchList: Array<Match>){
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        LazyColumn(
            modifier = Modifier
                .clip(shape = RoundedCornerShape(15.dp, 15.dp, 0.dp, 0.dp))
                .size(width = 300.dp, height = 320.dp)
        ) {
            stickyHeader {
                Row(
                    modifier = Modifier
                        .height(32.dp)
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.primaryContainer),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = "Recent Games",
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        fontSize = 16.sp,
                        modifier = Modifier.padding(horizontal = 8.dp)
                    )
                }
            }
            for(match in matchList) {
                item {
                    MatchRow(match)
                    HorizontalDivider(
                        color = MaterialTheme.colorScheme.onPrimary,
                        thickness = 1.dp,
                        modifier = Modifier
                            .width(250.dp)
                            .padding(start = 60.dp)
                    )
                }
            }
        }
    }
}



@Composable
fun MatchRow(match: Match) {
    Row(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.primary)
            .fillMaxWidth()
            .padding(start = 8.dp)
            .height(40.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if(match.type == 0) {
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = "User Icon",
                modifier = Modifier.size(32.dp),
                tint = MaterialTheme.colorScheme.inversePrimary
            )
        } else {
            Icon(
                imageVector = Icons.Default.Group,
                contentDescription = "Two People Icon",
                modifier = Modifier
                    .size(32.dp),
                tint = MaterialTheme.colorScheme.onPrimary
            )
        }
        Spacer(modifier = Modifier.width(24.dp))
        ProfileImage2(imageResourceId = R.drawable.profile_picture)
        Spacer(modifier = Modifier.width(24.dp))
        Column(
            modifier = Modifier.width(140.dp)
        ) {
            Text(text = match.username, color = MaterialTheme.colorScheme.onPrimary)
        }
        if(match.result == 0) {
            Icon(
                imageVector = Icons.Default.Done,
                contentDescription = "Done Icon",
                modifier = Modifier.size(32.dp),
                tint = MaterialTheme.colorScheme.outline
            )
        } else {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "Close Icon",
                modifier = Modifier
                    .size(32.dp),
                tint = MaterialTheme.colorScheme.error
            )
        }
    }
}


@Preview
@Composable
fun RecentMatchesPreview() {
    val recentGames = arrayOf(
        Match(0,"avatar","Awenega",1),
        Match(1,"avatar","Username",0),
        Match(1,"avatar","Francesco Sudoso",0),
        Match(0,"avatar","Jhon Doe",1),
        Match(0,"avatar","Andrew Smith",0),
        Match(1,"avatar","Nome Cognome",1),
        Match(1,"avatar","Nome Cognome",1),
        Match(1,"avatar","Nome Cognome",1),
        Match(1,"avatar","Nome Cognome",1),
        Match(1,"avatar","Nome Cognome",1),
        Match(1,"avatar","Nome Cognome",1),
        Match(1,"avatar","Nome Cognome",1),
        Match(1,"avatar","Nome Cognome",1),
    )
    RecentMatches(recentGames)
}

@Composable
fun ProfileImage2(imageResourceId: Int) {
    Surface(
        modifier = Modifier
            .size(32.dp)
            .clip(CircleShape),
        color = Color.Transparent
    ) {
        Image(
            painter = painterResource(id = imageResourceId),
            contentDescription = null,
            modifier = Modifier
                .size(32.dp)
                .clip(CircleShape)
        )
    }
}

data class Match(
    val type: Int, //0 for single - 1 for multi
    val profileUrlId: String,
    val username: String,
    val result: Int, //0 for win - 1 for lose
)