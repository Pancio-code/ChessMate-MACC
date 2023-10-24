package com.example.chessmate.ui.pages
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.chessmate.ui.components.NavBarComponent
import com.example.chessmate.ui.components.TopBarComponent
import com.example.chessmate.ui.theme.ChessMateTheme

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Composable
fun ProfilePage(
    width: WindowWidthSizeClass,
    navController: NavController,
    innerPadding: PaddingValues
) {
    ChessMateTheme {
        when(width) {
            WindowWidthSizeClass.Expanded -> {
                // orientation is landscape in most devices including foldables (width 840dp+)
            }

            WindowWidthSizeClass.Medium -> {
                // Most tablets are in landscape, larger unfolded inner displays in portrait (width 600dp+)
            }

            WindowWidthSizeClass.Compact -> {
                            // Title
                            Text(
                                modifier = Modifier.padding(innerPadding),
                                text = "ChessMate",
                                style = MaterialTheme.typography.headlineMedium,
                            )
            }
        }
    }
}
