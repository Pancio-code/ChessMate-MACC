package com.example.chessmate.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.Alignment
import androidx.compose.foundation.Image
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.chessmate.R
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.chessmate.sign_in.UserData
import com.example.chessmate.ui.theme.light_primary

@Composable
fun CardProfile(
    userData: UserData?,
    toggler: () -> Unit
){
    ElevatedCard(
        elevation = CardDefaults.cardElevation(
            defaultElevation = 6.dp
        ),
        colors = CardDefaults.cardColors(
            containerColor = light_primary,
        ),
        modifier = Modifier
            .size(width = 300.dp, height = 120.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(1f)
                .padding(start = 8.dp, top = 8.dp, end = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            Column(
                modifier = Modifier.padding(end=8.dp),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.Center,
            ){
                ProfileImage(imageResourceId = R.drawable.profile_picture)
            }
            Column(
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.Center
            ) {
                if (userData?.username != null) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column{
                            Text(
                                text = userData.username,
                                fontWeight = FontWeight.Medium,
                                fontSize = 20.sp,
                                color = Color.White
                            )
                        }
                        Column(
                            modifier = Modifier.fillMaxWidth().padding(start=10.dp),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.End
                        ) {
                            IconButton(
                                onClick = toggler,
                                modifier = Modifier.size(20.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Settings,
                                    contentDescription = "Settings",
                                    tint = Color.LightGray
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = userData.email.toString(),
                        fontSize = 14.sp,
                        color = Color.LightGray
                    )
                }
                Spacer(modifier = Modifier.height(2.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_italian_flag), // Replace with your actual resource ID
                        contentDescription = "Italian Flag",
                        modifier = Modifier.size(10.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Italy",
                        fontWeight = FontWeight.Normal,
                        fontSize = 14.sp,
                        color = Color.LightGray
                    )
                }
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Column {
                Text(
                    text = "Signup on 22 Nov 2023",
                    fontWeight = FontWeight.Normal,
                    fontSize = 12.sp,
                    color = Color.LightGray,
                    modifier = Modifier.padding(start = 16.dp)
                )
            }
        }
    }
}




@Composable
fun ProfileImage(imageResourceId: Int) {
    Surface(
        modifier = Modifier
            .size(80.dp)
            .clip(CircleShape),
        color = Color.Transparent
    ) {
        Image(
            painter = painterResource(id = imageResourceId),
            contentDescription = null,
            modifier = Modifier
                .size(80.dp)
                .clip(CircleShape)
        )
    }
}

@Preview
@Composable
fun CardProfilePreview() {
    val userData = UserData(
        id = "1",
        profilePictureUrl = null,
        username = "Nome Cognome",
        email = "andrea.pancio00@gmail.com",
        emailVerified = false,
        provider = null
    )
    CardProfile(userData, toggler = {})
}