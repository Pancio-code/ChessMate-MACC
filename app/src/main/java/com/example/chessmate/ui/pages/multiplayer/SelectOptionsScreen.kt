package com.example.chessmate.ui.pages.multiplayer

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.Start
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.chessmate.game.model.piece.Set
import com.example.chessmate.multiplayer.GameDifficulty
import com.example.chessmate.multiplayer.OnlineViewModel
import com.example.chessmate.ui.navigation.ChessMateRoute

@Composable
fun SelectOptionsScreen(
    modifier: Modifier = Modifier,
    onlineViewModel: OnlineViewModel,
    toggleFullView: () -> Unit = {},
) {
    var selectedColor by remember { mutableStateOf(Set.WHITE) }
    var selectedDifficulty by remember { mutableStateOf(GameDifficulty.EASY) }
    var checked by remember { mutableStateOf(true) }
    var expanded by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.inverseOnSurface),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            "Select your color and difficulty:",
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(20.dp))
        Row(
            modifier = modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
        Text(text = if (checked) "WHITE" else "BLACK", color = MaterialTheme.colorScheme.inverseSurface)
            Spacer(Modifier.width(10.dp))
        Switch(
            checked = checked,
            onCheckedChange = {
                checked = it
                selectedColor = if (it) Set.WHITE else Set.BLACK
            },
            colors = SwitchDefaults.colors(
                checkedThumbColor = MaterialTheme.colorScheme.primary,
                checkedTrackColor = MaterialTheme.colorScheme.primaryContainer,
                uncheckedThumbColor = MaterialTheme.colorScheme.secondary,
                uncheckedTrackColor = MaterialTheme.colorScheme.secondaryContainer,
            )
        )
        }
        Spacer(modifier = Modifier.height(20.dp))
        Button(
            onClick = { expanded = true }
        ) {
            Text(selectedDifficulty.name)
            Spacer(modifier = Modifier.width(8.dp))
            Icon(
                imageVector = Icons.Default.ArrowDropDown,
                contentDescription = "Drop icon",
                tint = MaterialTheme.colorScheme.onTertiary
            )
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                GameDifficulty.entries.forEach { difficulty ->
                    DropdownMenuItem(
                        text = { Text(difficulty.name) },
                        onClick = {
                            selectedDifficulty = difficulty
                            expanded = false
                        }
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(60.dp))
        Button(
            onClick = {
                onlineViewModel.setStartColor(selectedColor)
                val depth = when(selectedDifficulty) {
                    GameDifficulty.EASY -> 5
                    GameDifficulty.MEDIUM -> 7
                    GameDifficulty.HARD -> 9
                    GameDifficulty.EXTREME -> 11
                    GameDifficulty.IMPOSSIBLE -> 13
                }
                onlineViewModel.setDepth(depth)
                onlineViewModel.setFullViewPage(ChessMateRoute.AI_GAME)
            },
            colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.tertiary)
        ) {
            Text(text = "Start", color = MaterialTheme.colorScheme.onTertiary)
            Spacer(modifier = Modifier.width(8.dp))
            Icon(
                imageVector = Icons.Default.Start,
                contentDescription = "Start icon",
                tint = MaterialTheme.colorScheme.onTertiary
            )
        }
        Spacer(modifier = Modifier.height(20.dp))
        Button(
            onClick = {
                onlineViewModel.setFullViewPage("")
                toggleFullView()
            },
            colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.error)
        ) {
            Icon(
                imageVector = Icons.Default.ChevronLeft,
                contentDescription = "Back icon",
                tint = MaterialTheme.colorScheme.onError
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = "Back home", color = MaterialTheme.colorScheme.onError)
        }
    }
}