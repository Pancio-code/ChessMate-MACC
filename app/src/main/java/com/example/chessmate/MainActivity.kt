package com.example.chessmate

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.chessmate.ui.theme.ChessMateTheme

class MainActivity : ComponentActivity() {
    private var consumerKey: String = BuildConfig .CONSUMER_KEY
     private var consumerSecret: String = BuildConfig.CONSUMER_SECRET

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ChessMateTheme {
                Scaffold(
                    topBar = {
                            TopAppBar(
                                colors = topAppBarColors(
                                    containerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(NavigationBarDefaults.Elevation),
                                    titleContentColor = MaterialTheme.colorScheme.primary,
                                ),
                                title = {
                                    Text("ChessMate")
                                }
                            )
                    },
                    bottomBar = {

                        var selectedItem by remember { mutableIntStateOf(0) }
                        val items = listOf("Songs", "Artists", "Playlists")

                        NavigationBar {
                            items.forEachIndexed { index, item ->
                                NavigationBarItem(
                                    icon = {
                                        Icon(
                                            Icons.Filled.Favorite,
                                            contentDescription = item
                                        )
                                    },
                                    label = { Text(item) },
                                    selected = selectedItem == index,
                                    onClick = { selectedItem = index }
                                )
                            }
                        }
                    }
                ) { innerPadding -> ChessMainMenuScreenPortrait(consumerKey, consumerSecret,innerPadding)
                }
            }
        }
    }
}

@Composable
fun ChessMainMenuScreenPortrait(
    consumerKey: String,
    consumerSecret: String,
    innerPadding: PaddingValues
) {
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
                colorFilter =  ColorFilter.tint(MaterialTheme.colorScheme.primary)
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


@Preview(showBackground = true)
@Composable
fun ChessMainMenuScreenPortraitPreview() {
    ChessMateTheme {
        Scaffold(
            bottomBar = {
                var selectedItem by remember { mutableIntStateOf(0) }
                val items = listOf("Songs", "Artists", "Playlists")

                NavigationBar {
                    items.forEachIndexed { index, item ->
                        NavigationBarItem(
                            icon = {
                                Icon(
                                    Icons.Filled.Favorite,
                                    contentDescription = item
                                )
                            },
                            label = { Text(item) },
                            selected = selectedItem == index,
                            onClick = { selectedItem = index }
                        )
                    }
                }
            },
        ) { innerPadding -> ChessMainMenuScreenPortrait("consumerKey", "consumerSecret",innerPadding)
        }
    }
}
