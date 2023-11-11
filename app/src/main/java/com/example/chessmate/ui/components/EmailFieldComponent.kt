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
fun EmailFieldComponent(
    email: TextFieldValue,
    onEmailValueChange: (newValue: TextFieldValue) -> Unit,
    errors: String? = null
) {
    val focusRequester = FocusRequester()

    OutlinedTextField(
        value = email,
        onValueChange = { newValue ->
            onEmailValueChange(newValue)
        },
        label = {
            Text(
                text = "Email"
            )
        },
        singleLine = true,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Email
        ),
        modifier = Modifier.focusRequester(focusRequester),
        isError = errors != null,
        supportingText = {
            if (errors != null) {
                Text(
                    text = errors,
                    color = MaterialTheme.colorScheme.error
                )
            }
        }
    )

}