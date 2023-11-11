package com.example.chessmate.ui.pages
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.chessmate.sign_in.UserData
import com.example.chessmate.ui.theme.ChessMateTheme

@Composable
fun ProfileScreen(
    userData: UserData?,
    onSignOut: () -> Unit,
    innerPadding: PaddingValues,
    width: WindowWidthSizeClass,
) {
    ChessMateTheme {
        when (width) {
            WindowWidthSizeClass.Expanded -> {
                // orientation is landscape in most devices including foldables (width 840dp+)
            }

            WindowWidthSizeClass.Medium -> {
                // Most tablets are in landscape, larger unfolded inner displays in portrait (width 600dp+)
            }

            WindowWidthSizeClass.Compact -> {
                Column(
                    modifier = Modifier.fillMaxSize().padding(innerPadding),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    if (userData?.profilePictureUrl != null) {
                        AsyncImage(
                            model = userData.profilePictureUrl,
                            contentDescription = "Profile picture",
                            modifier = Modifier
                                .size(150.dp)
                                .clip(CircleShape),
                            contentScale = ContentScale.Crop
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                    if (userData?.username != null) {
                        Text(
                            text = userData.username,
                            textAlign = TextAlign.Center,
                            fontSize = 36.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                    Button(onClick = onSignOut) {
                        Text(text = "Sign out")
                    }
                }
            }
        }
    }
}