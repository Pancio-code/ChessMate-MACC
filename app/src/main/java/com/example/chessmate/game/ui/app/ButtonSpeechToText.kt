package com.example.chessmate.game.ui.app

import android.Manifest
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedContent
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Mic
import androidx.compose.material.icons.rounded.Stop
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import com.example.chessmate.ui.utils.VoiceToTextParser

@Composable
fun ButtonSpeechToText(setSpokenText: (String) -> Unit){
    val voiceToTextParser = VoiceToTextParser(LocalContext.current)

    val state by voiceToTextParser.state.collectAsState()
    var canRecord by remember { mutableStateOf(false) }
    val recordAudioLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted -> canRecord = isGranted}
    )
    LaunchedEffect(key1 = recordAudioLauncher){
        recordAudioLauncher.launch(Manifest.permission.RECORD_AUDIO)
    }

    FloatingActionButton(
        onClick = {
            if (state.isSpeaking) {
                voiceToTextParser.stopListening()
            } else {
                voiceToTextParser.startListening()
            }
        }
    ) {
        AnimatedContent(targetState = state.isSpeaking, label = "") { isSpeaking ->
            if (isSpeaking){
                Icon(imageVector = Icons.Rounded.Stop, contentDescription = null)
            } else {
                Icon(imageVector = Icons.Rounded.Mic, contentDescription = null)
                if (state.spokenText.isNotEmpty()){
                    setSpokenText(state.spokenText)
                }
            }
        }
    }
}
