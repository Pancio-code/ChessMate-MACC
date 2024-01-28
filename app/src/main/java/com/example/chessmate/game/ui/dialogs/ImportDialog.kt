package com.example.chessmate.game.ui.dialogs

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.chessmate.game.model.game.converter.FenConverter
import com.example.chessmate.game.model.game.converter.PgnConverter

@Composable
fun ImportPgnDialog(
    onDismiss: () -> Unit,
    onImportPgn: (String) -> Unit,
) {
    MaterialTheme {
        Dialog(
            onDismissRequest = onDismiss,
            properties = DialogProperties(
                dismissOnBackPress = true,
                dismissOnClickOutside = true
            )
        ) {
            ImportPgnDialogContent(
                validate = { PgnConverter.preValidate(it) },
                onCancel = onDismiss,
                onDone = onImportPgn
            )
        }
    }
}

@Composable
fun ImportFenDialog(
    onDismiss: () -> Unit,
    onImportFen: (String) -> Unit,
) {
    MaterialTheme {
        Dialog(
            onDismissRequest = onDismiss,
            properties = DialogProperties(
                dismissOnBackPress = true,
                dismissOnClickOutside = true
            )
        ) {
            ImportFenDialogContent(
                validate = { FenConverter.preValidate(it) },
                onCancel = onDismiss,
                onDone = onImportFen
            )
        }
    }
}
@Preview
@Composable
private fun ImportFenDialogContent(
    validate: (String) -> Boolean = { true },
    onCancel: () -> Unit = {},
    onDone: (String) -> Unit = {},
) {
    Column(
        modifier = Modifier
            .padding(48.dp)
            .background(
                color = MaterialTheme.colorScheme.surface,
                shape = MaterialTheme.shapes.medium
            ),
        horizontalAlignment = Alignment.Start
    ) {
        var text by rememberSaveable { mutableStateOf("") }
        val isValid = remember(text) { validate(text) }

        TextField(
            value = text,
            onValueChange = { text = it },
            isError = !isValid,
            label = @Composable {
                Text(
                    text = "FEN",
                    fontWeight = FontWeight.Bold
                )
            },
            placeholder = @Composable {
                Text(text = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1")
            }
        )

        Row(
            horizontalArrangement = Arrangement.End
        ) {
            Button(
                onClick = onCancel,
                modifier = Modifier.padding(8.dp)
            ) {
                Text(text = "Cancel")
            }
            Button(
                onClick = { onDone(text) },
                modifier = Modifier.padding(8.dp),
                enabled = isValid
            ) {
                Text(text = "Done")
            }
        }
    }
}

@Preview
@Composable
private fun ImportPgnDialogContent(
    validate: (String) -> Boolean = { true },
    onCancel: () -> Unit = {},
    onDone: (String) -> Unit = {},
) {
    Column(
        modifier = Modifier
            .padding(48.dp)
            .background(
                color = MaterialTheme.colorScheme.surface,
                shape = MaterialTheme.shapes.medium
            ).heightIn(max=200.dp),
        horizontalAlignment = Alignment.Start
    ) {
        var text by rememberSaveable { mutableStateOf("") }
        val isValid = remember(text) { validate(text) }

        Box(modifier = Modifier.weight(1f).fillMaxWidth()) {
            TextField(
                value = text,
                onValueChange = { text = it },
                isError = !isValid,
                modifier = Modifier.fillMaxWidth(), // Make sure TextField uses available space
                label = @Composable {
                    Text(
                        text = "PGN",
                        fontWeight = FontWeight.Bold
                    )
                },
                placeholder = @Composable {
                    Text(text = "1. d4 f5 2. Bf4 e6 3. Nf3 Nf6 4. h3 Nd5 5. Bh2 Nc6 6. e3 b5")
                }
            )
        }

        Row(
            horizontalArrangement = Arrangement.End
        ) {
            Button(
                onClick = onCancel,
                modifier = Modifier.padding(8.dp)
            ) {
                Text(text = "Cancel")
            }
            Button(
                onClick = { onDone(text) },
                modifier = Modifier.padding(8.dp),
                enabled = isValid
            ) {
                Text(text = "Done")
            }
        }
    }
}
