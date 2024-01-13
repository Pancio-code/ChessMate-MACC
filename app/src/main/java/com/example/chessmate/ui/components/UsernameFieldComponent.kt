package com.example.chessmate.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue

@Composable
fun UsernameFieldComponent(
    username: TextFieldValue,
    onUsernameValueChange: (newValue: TextFieldValue) -> Unit,
    error: String? = null
) {
    val focusRequester = FocusRequester()

    OutlinedTextField(
        value = username,
        onValueChange = { newValue ->
            onUsernameValueChange(newValue)
        },
        label = {
            Text(
                text = "Username"
            )
        },
        singleLine = true,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Text
        ),
        modifier = Modifier.focusRequester(focusRequester),
        supportingText = {
            if (error != null) {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = error,
                    color = MaterialTheme.colorScheme.error
                )
            }
        }
    )

}