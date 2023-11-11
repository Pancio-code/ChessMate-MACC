package com.example.chessmate.ui.pages

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.chessmate.R
@Composable

fun HomePage(
    modifier: Modifier = Modifier
) {
    val scroll = rememberScrollState(0)
    Column(
        modifier = modifier.fillMaxSize().verticalScroll(scroll),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_chessmate_foreground),
            contentDescription = stringResource(id = R.string.logo),
            modifier = Modifier.size(200.dp),
            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            modifier = Modifier.padding(12.dp),
            text = stringResource(id = R.string.app_name),
            style = MaterialTheme.typography.titleLarge,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = { /* Handle 1vs1 online button click */ },
            modifier = Modifier
                .fillMaxWidth(fraction = 0.6f)
                .height(65.dp)
        ) {
            Text(text = "Play 1vs1 online")
        }
        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { /* Handle Play against Stockfish button click */ },
            modifier = Modifier
                .fillMaxWidth(fraction = 0.6f)
                .height(65.dp)
        ) {
            Text(text = "Play against Stockfish")
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = { /* Handle Resume game button click */ },
            modifier = Modifier
                .fillMaxWidth(fraction = 0.6f)
                .height(65.dp)
        ) {
            Text(text = "Resume game")
        }
    }
}

@Preview
@Composable
fun HomePagePreview() {
    HomePage()
}

