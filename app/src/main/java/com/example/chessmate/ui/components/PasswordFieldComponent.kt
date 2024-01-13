package com.example.chessmate.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp

@Composable
fun PasswordFieldComponent(
    password: TextFieldValue,
    label: String,
    onPasswordValueChange: (newValue: TextFieldValue) -> Unit,
    error: String? = null,
) {
    var passwordIsVisible by remember { mutableStateOf(false) }
    OutlinedTextField(
        value = password,
        onValueChange = { newValue ->
            onPasswordValueChange(newValue)
        },
        label = {
            Text(
                text = label
            )
        },
        singleLine = true,
        visualTransformation = if (passwordIsVisible) {
            VisualTransformation.None
        } else {
            PasswordVisualTransformation()
        },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Password
        ),
        trailingIcon = {
            val icon = if (passwordIsVisible) {
                Icon(
                    imageVector = Icons.Default.Visibility,
                    modifier = Modifier.size(16.dp),
                    contentDescription = "Visibility on icon"
                )
            } else {
                Icon(
                    imageVector = Icons.Default.VisibilityOff,
                    modifier = Modifier.size(16.dp),
                    contentDescription = "Visibility off icon"
                )
            }
            IconButton(
                onClick = {
                    passwordIsVisible = !passwordIsVisible
                }
            ) {
                icon
            }
        },
        isError = error != null,
        supportingText = {
            if (error != null) {
                Text(
                    modifier = Modifier.fillMaxWidth(0.8f),
                    text = error,
                    color = MaterialTheme.colorScheme.error
                )
            }
        }
    )
}