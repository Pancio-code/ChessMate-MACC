package com.example.chessmate.ui.components

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import com.example.chessmate.R
import kotlin.system.exitProcess

@Composable
fun ExitApplicationComponent() {
    var openDialog by remember { mutableStateOf(false) }
    BackHandler(enabled = true) {
        openDialog = true
    }

    if (openDialog) {
    AlertDialog(
        onDismissRequest = {openDialog = false},
        icon = {
            Image(
                painter = painterResource(id = R.drawable.ic_exit_to_app),
                contentDescription = "Exit application",
                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary)
            )
        },
        title = {
            Text(text = "Do you want exit ChessMate?")
        },
        confirmButton = {
            TextButton(
                onClick = {
                   exitProcess(0)
                }
            ) {
                Text("Yes")
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    openDialog = false
                }
            ) {
                Text("No")
            }
        }
    )
}
}