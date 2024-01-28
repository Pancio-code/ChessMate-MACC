package com.example.chessmate.game.ui.dialogs

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.chessmate.R

@Composable
fun GameDialog(
    onDismiss: () -> Unit,
    onNewGame: () -> Unit,
    onExitGame: () -> Unit,
    onImportPgnGame: () -> Unit,
    onImportFenGame: () -> Unit,
    onExportGame: () -> Unit,
) {
    ClickableListItemsDialog(
        onDismiss = onDismiss,
        items = listOf(
            stringResource(R.string.game_new) to onNewGame,
            stringResource(R.string.game_import_pgn) to onImportPgnGame,
            stringResource(R.string.game_import_fen) to onImportFenGame,
            stringResource(R.string.game_export) to onExportGame,
            stringResource(R.string.game_exit) to onExitGame,
        )
    )
}

@Preview
@Composable
private fun GameDialogContent() {
    GameDialog(
        onDismiss = {},
        onNewGame = {},
        onImportPgnGame = {},
        onImportFenGame = {},
        onExportGame = {},
        onExitGame = {}
    )
}
