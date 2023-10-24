package com.example.chessmate.ui.components
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import com.example.chessmate.ui.theme.ChessMateTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBarComponent() {
    ChessMateTheme {
        TopAppBar(
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(
                    NavigationBarDefaults.Elevation),
                titleContentColor = MaterialTheme.colorScheme.primary,
            ),
            title = {
                Text("ChessMate")
            }
        )
    }
}