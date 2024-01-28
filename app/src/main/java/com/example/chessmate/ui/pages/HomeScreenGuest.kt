package com.example.chessmate.ui.pages

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.SmartToy
import androidx.compose.material.icons.filled.WifiOff
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.chessmate.R
import com.example.chessmate.multiplayer.OnlineViewModel
import com.example.chessmate.ui.navigation.ChessMateRoute

@Composable

fun HomePageGuest(
    modifier: Modifier = Modifier,
    toggleFullView: () -> Unit = {},
    onlineViewModel: OnlineViewModel
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
            onClick = {
                onlineViewModel.setFullViewPage(ChessMateRoute.OFFLINE_GAME)
                toggleFullView()
              },
            modifier = Modifier
                .fillMaxWidth(fraction = 0.6f)
                .height(65.dp)
        ) {
            Icon(
                imageVector = Icons.Default.WifiOff,
                modifier = Modifier.size(16.dp),
                contentDescription = "Offline icon"
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = "Play 1vs1 offline")
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
                onlineViewModel.setFullViewPage(ChessMateRoute.SELECT_COLOR)
                toggleFullView()
            },
            modifier = Modifier
                .fillMaxWidth(fraction = 0.6f)
                .height(65.dp)
        ) {
            Icon(
                imageVector = Icons.Default.SmartToy,
                modifier = Modifier.size(16.dp),
                contentDescription = "Robot icon"
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = "Play against AI")
        }
    }
}

@Preview
@Composable
fun HomePageGuestPreview() {
    HomePageGuest(onlineViewModel = OnlineViewModel())
}

