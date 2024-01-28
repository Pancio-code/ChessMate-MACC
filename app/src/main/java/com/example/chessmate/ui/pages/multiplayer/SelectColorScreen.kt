package com.example.chessmate.ui.pages.multiplayer

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.chessmate.game.model.piece.Set
import com.example.chessmate.multiplayer.OnlineViewModel
import com.example.chessmate.ui.navigation.ChessMateRoute

@Composable
fun SelectColorScreen(
    modifier: Modifier = Modifier,
    onlineViewModel: OnlineViewModel,
    togglefullView: () -> Unit = {},
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.inverseOnSurface),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            "Select your color:",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(modifier = Modifier.height(20.dp))
        Button(
            onClick = {
                onlineViewModel.setStartColor(Set.WHITE)
                onlineViewModel.setFullViewPage(ChessMateRoute.AI_GAME)
            },
            colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.primary)) {
            Text(text = "WHITE", color = MaterialTheme.colorScheme.onPrimary)
        }
        Spacer(modifier = Modifier.height(20.dp))
        Button(
            onClick = {
                onlineViewModel.setStartColor(Set.BLACK)
                onlineViewModel.setFullViewPage(ChessMateRoute.AI_GAME)
            },
            colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.secondary)) {
            Text(text = "BLACK", color = MaterialTheme.colorScheme.onSecondary)
        }
        Spacer(modifier = Modifier.height(40.dp))
        Button(
            onClick = {
                onlineViewModel.setFullViewPage("")
                togglefullView()
            },
            colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.error)) {
            Icon(
                imageVector = Icons.Default.ChevronLeft,
                modifier = Modifier.size(16.dp),
                contentDescription = "Back icon",
                tint = MaterialTheme.colorScheme.onError
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = "Back home", color = MaterialTheme.colorScheme.onError)
        }
    }
}