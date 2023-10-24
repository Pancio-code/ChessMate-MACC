package com.example.chessmate.ui.pages

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.chessmate.R
@Composable

fun MainPage(
    width: WindowWidthSizeClass,
    navController: NavController,
    innerPadding: PaddingValues
) {
        when(width) {
            WindowWidthSizeClass.Expanded -> {
                // orientation is landscape in most devices including foldables (width 840dp+)
            }

            WindowWidthSizeClass.Medium -> {
                // Most tablets are in landscape, larger unfolded inner displays in portrait (width 600dp+)
            }

            WindowWidthSizeClass.Compact -> {
                    Column(
                        modifier = Modifier
                            .padding(innerPadding).padding(horizontal = Dp(10F)).fillMaxHeight(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // Logo image (replace R.drawable.logo with your logo)
                        Image(
                            painter = painterResource(id = R.drawable.ic_chessmate_foreground),
                            contentDescription = null, // Provide a description
                            modifier = Modifier.size(200.dp),
                            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary)
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        // Title
                        Text(
                            text = "ChessMate",
                            style = MaterialTheme.typography.headlineMedium,
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        // Three buttons
                        Button(
                            onClick = { /* Handle 1vs1 online button click */ },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(65.dp)
                        ) {
                            Text(text = "Play 1vs1 online")
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Button(
                            onClick = { /* Handle Play against Stockfish button click */ },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(65.dp)
                        ) {
                            Text(text = "Play against Stockfish")
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Button(
                            onClick = { /* Handle Resume game button click */ },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(65.dp)
                        ) {
                            Text(text = "Resume game")
                        }
                    }
        }
    }
}
