package com.example.chessmate.ui.navigation

import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
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
import androidx.compose.ui.res.stringResource
import com.example.chessmate.R
import kotlin.system.exitProcess

@Composable
fun ExitApplicationComponent(activity:Activity) {
    var openDialog by remember { mutableStateOf(false) }
    BackHandler(enabled = true) {
        openDialog = true
    }

    if (openDialog) {
    AlertDialog(
        onDismissRequest = {openDialog = false},
        icon = {
            Icon(imageVector = Icons.Default.ExitToApp, contentDescription = stringResource(id = R.string.exit_button))
        },
        title = {
            Text(text = stringResource(id = R.string.exit_pop_up_content))
        },
        confirmButton = {
            TextButton(
                onClick = {
                   activity.finishAffinity()
                }
            ) {
                Text(text = stringResource(id = R.string.confirm))
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    openDialog = false
                }
            ) {
                Text(text = stringResource(id = R.string.negate))
            }
        }
    )
}
}