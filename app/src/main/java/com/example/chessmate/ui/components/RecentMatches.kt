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
import androidx.compose.material.icons.automirrored.filled.StarHalf
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.SmartToy
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.example.chessmate.matches.Match
import com.example.chessmate.matches.MatchesViewModel
import com.example.chessmate.sign_in.UserDataHelper

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun RecentMatches(
    matchesViewModel: MatchesViewModel
){
    var matchList by remember { mutableStateOf<List<Match>>(emptyList()) }
    LaunchedEffect(Unit) {
        matchList = matchesViewModel.getMatches()
    }

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        LazyColumn(
            modifier = Modifier
                .clip(shape = RoundedCornerShape(15.dp, 15.dp, 15.dp, 15.dp))
                .size(width = 300.dp, height = 320.dp)
        ) {
            stickyHeader {
                Row(
                    modifier = Modifier
                        .height(32.dp)
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.primaryContainer),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
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
            if (matchesViewModel.matchList.value.isEmpty()){
                item{
                    EmptyMatchList()
                }
            } else {
                for (match in matchesViewModel.matchList.value) {
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
}

@Composable
fun EmptyMatchList(){
    Row(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.primary)
            .height(290.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Text(
            text = "No Recent Games Found",
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.inversePrimary,
            fontSize = 16.sp,
            modifier = Modifier.padding(horizontal = 8.dp)
        )
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
        IconMatch(match.matchType)
        Spacer(modifier = Modifier.width(24.dp))
        ProfilePictureMatch(matchType = match.matchType, userIdTwo = match.userIdTwo, profilePictureUrlUserTwo = match.profilePictureUrlUserTwo)
        Spacer(modifier = Modifier.width(24.dp))
        UsernameMatch(matchType = match.matchType, usernameUserTwo = match.usernameUserTwo)
        IconResultMatch(result = match.results)
    }
}


@Composable
fun ProfilePictureMatch(matchType: String, userIdTwo: String, profilePictureUrlUserTwo: String?) {
    var painter = rememberAsyncImagePainter(null)
    when (matchType){
        "ONLINE" -> {
            if (profilePictureUrlUserTwo!!.startsWith("http")) {
                painter = rememberAsyncImagePainter("$profilePictureUrlUserTwo")
            } else {
                painter = rememberAsyncImagePainter("${UserDataHelper.AVATAR_URL}/${userIdTwo}/${profilePictureUrlUserTwo}")
            }
        }
        "ONE_OFFLINE" -> {
            painter = rememberAsyncImagePainter("${UserDataHelper.AVATAR_URL}/robot.jpg")
        }
        "TWO_OFFLINE" -> {
            painter = rememberAsyncImagePainter("${UserDataHelper.AVATAR_URL}/twoOffline.jpg")
        }
    }

    Surface(
        modifier = Modifier
            .size(32.dp)
            .clip(CircleShape),
        color = Color.Transparent
    ) {
        Image(
            painter = painter,
            contentDescription = null,
            modifier = Modifier
                .size(32.dp)
                .clip(CircleShape)
        )
    }
}

@Composable
fun IconMatch(matchType: String) {
    when (matchType) {
        "ONLINE" -> {
            return Icon(
                imageVector = Icons.Filled.Wifi,
                contentDescription = "Wifi Icon",
                modifier = Modifier.size(32.dp),
                tint = MaterialTheme.colorScheme.onPrimary
            )
        }
        "ONE_OFFLINE" -> {
            return Icon(
                imageVector = Icons.Filled.SmartToy,
                contentDescription = "Robot",
                modifier = Modifier.size(32.dp),
                tint = MaterialTheme.colorScheme.onPrimary
            )
        }
        "TWO_OFFLINE" -> {
            return Icon(
                imageVector = Icons.Filled.Group,
                contentDescription = "Two People Icon",
                modifier = Modifier.size(32.dp),
                tint = MaterialTheme.colorScheme.onPrimary
            )
        }
        else -> {
            return Icon(imageVector = Icons.Filled.Close, contentDescription = null)
        }
    }
}

@Composable
fun UsernameMatch(matchType: String, usernameUserTwo: String?){
    var username = ""
    when (matchType){
        "ONLINE" -> {
            username = usernameUserTwo.toString()
        }
        "TWO_OFFLINE" -> {
            username = "Local Friend"
        }
        "ONE_OFFLINE" ->{
            username = "AI Player"
        }
    }

    Column(
        modifier = Modifier.width(140.dp)
    ) {
        Text(text = username, color = MaterialTheme.colorScheme.onPrimary)
    }
}

@Composable
fun IconResultMatch(result: Int){
    when (result){
        0 -> {
            return Icon(
                imageVector = Icons.Filled.Done,
                contentDescription = "Done Icon",
                modifier = Modifier.size(32.dp),
                tint = Color.Green.copy(alpha = 0.7f)
            )
        }
        1 -> {
            return Icon(
                imageVector = Icons.Filled.Close,
                contentDescription = "Done Icon",
                modifier = Modifier.size(32.dp),
                tint = Color.Red.copy(alpha = 0.7f)
            )
        }
        2 ->{
            return Icon(
                imageVector = Icons.AutoMirrored.Filled.StarHalf,
                contentDescription = "Done Icon",
                modifier = Modifier.size(32.dp),
                tint = Color.Black.copy(alpha = 0.7f)
            )
        }
    }
}
