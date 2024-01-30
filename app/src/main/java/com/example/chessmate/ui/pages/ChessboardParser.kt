package com.example.chessmate.ui.pages

import android.content.Context
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.SportsEsports
import androidx.compose.material.icons.filled.Upload
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import com.example.chessmate.multiplayer.OnlineViewModel
import com.example.chessmate.multiplayer.ParseChessBoardUIClient
import com.example.chessmate.ui.navigation.ChessMateRoute
import com.example.chessmate.ui.pages.profile.LoadingDialog
import com.example.chessmate.ui.pages.profile.generateRandomString
import com.example.chessmate.ui.pages.profile.getImageFile
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import java.io.File
import java.io.InputStream

@Composable
fun ChessboardParser(
    modifier: Modifier = Modifier,
    onlineViewModel: OnlineViewModel,
    toggleFullView: () -> Unit = {},
    ) {
    val context: Context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    var isUploaded by remember { mutableStateOf(false) }
    var fen by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var screenshotFile by remember { mutableStateOf(File("","")) }
    val getContent = rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) {
        newValue: Uri? -> val imageStream: InputStream? = context.contentResolver.openInputStream(newValue!!)
        screenshotFile = getImageFile(context = context, path = "${generateRandomString(10)}.jpg", imageStream = imageStream)
        isUploaded = true
    }


    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Chessboard Parser",
            style = MaterialTheme.typography.titleLarge,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.primary
        )
        Text(
            modifier = Modifier.padding(bottom = 24.dp),
            text = "From your image to a playable board",
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.inversePrimary
        )
        if (isUploaded && screenshotFile.path != ""){
            Button(
                modifier = Modifier
                    .padding(bottom = 8.dp)
                    .fillMaxWidth(fraction = 0.6f)
                    .height(65.dp),
                colors = ButtonDefaults.buttonColors(Color(android.graphics.Color.parseColor("#008b00"))),
                onClick = {
                    lifecycleOwner.lifecycleScope.launch {
                        isLoading = true
                        fen = ParseChessBoardUIClient().uploadScreenshot(screenshotFile)
                        joinAll()
                        isLoading = false
                        onlineViewModel.setImportedFen(fen)
                        onlineViewModel.setFullViewPage(ChessMateRoute.OFFLINE_GAME)
                        toggleFullView()
                    }
                }
            ) {
                Icon(
                    imageVector = Icons.Default.SportsEsports,
                    modifier = Modifier.size(16.dp),
                    contentDescription = "Start the game"
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = "Start game")
            }
            Text(
                modifier = Modifier.padding(bottom = 16.dp),
                text = screenshotFile.name,
                style = MaterialTheme.typography.bodySmall,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.tertiary
            )
        } else {
            Button(
                modifier = Modifier
                    .padding(bottom = 24.dp)
                    .fillMaxWidth(fraction = 0.6f)
                    .height(65.dp),
                onClick = {getContent.launch("image/*") }
            ) {
                Icon(
                    imageVector = Icons.Default.Upload,
                    modifier = Modifier.size(16.dp),
                    contentDescription = "Upload"
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = "Upload Screenshot")
            }
        }
        Text(
            modifier = Modifier.padding(horizontal = 8.dp),
            text = "Press the button and upload a screenshot of a chessboard\nYou will start a game with the same board",
            style = MaterialTheme.typography.bodySmall,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.secondary
        )
        if (isLoading) {
            LoadingDialog()
        }
    }
}
