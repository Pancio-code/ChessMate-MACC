package com.example.chessmate.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import com.example.chessmate.BuildConfig
import com.example.chessmate.sign_in.UserData

@Composable
fun CardProfile(
    userData: UserData?,
    toggler: () -> Unit,
    painter: AsyncImagePainter
){
    ElevatedCard(
        elevation = CardDefaults.cardElevation(
            defaultElevation = 6.dp
        ),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primary,
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
                modifier = Modifier.padding(end=8.dp, top = 8.dp),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.Center,
            ){
                ProfileImage(painter = painter)
            }
            Column(
                horizontalAlignment = Alignment.Start,
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
                                color = MaterialTheme.colorScheme.onPrimary
                            )
                        }
                        Column(
                            modifier = Modifier.fillMaxWidth().padding(start=10.dp),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.End
                        ) {
                            IconButton(
                                onClick = toggler,
                                modifier = Modifier.size(24.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Settings,
                                    contentDescription = "Settings",
                                    tint = MaterialTheme.colorScheme.inversePrimary
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(0.dp))
                    Text(
                        text = userData.email.toString(),
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.inversePrimary
                    )
                }
                Spacer(modifier = Modifier.height(0.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Image(
                        painter = painterResource(id = getFlags(userData?.country.toString())),
                        contentDescription = "Flag",
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = getNameOfCountry(userData?.country.toString()),
                        fontWeight = FontWeight.Normal,
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.inversePrimary
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
                    text = "Signup on ${userData?.signupDate}",
                    fontWeight = FontWeight.Normal,
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.inversePrimary,
                    modifier = Modifier.padding(start = 16.dp)
                )
            }
        }
    }
}




@Composable
fun ProfileImage(painter: AsyncImagePainter) {
    Surface(
        modifier = Modifier
            .size(80.dp)
            .clip(CircleShape),
        color = Color.Transparent
    ) {
        Image(
            painter = painter,
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
        profilePictureUrl = "",
        username = "Username",
        email = "andrea.pancio00@gmail.com",
        emailVerified = false,
        provider = null,
        country = "it",
        signupDate = "26 Jan 2024"
    )
    val painter = rememberAsyncImagePainter("${BuildConfig.API_URL}/api/v1/user/avatar/${userData.id}")
    CardProfile(userData, toggler = {}, painter)
}