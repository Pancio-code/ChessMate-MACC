package com.example.chessmate.game.ui.dialogs

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.chessmate.R
import com.example.chessmate.multiplayer.GameType

@Composable
fun GameDialog(
    onDismiss: () -> Unit,
    onNewGame: () -> Unit,
    onExitGame: () -> Unit,
    onImportPgnGame: () -> Unit,
    onImportFenGame: () -> Unit,
    onExportGamePgn: () -> Unit,
    onExportGameFen: () -> Unit,
    gameType: GameType
) {
    val offlineContents = if (gameType != GameType.ONLINE) listOf(
            stringResource(R.string.game_new) to onNewGame,
            stringResource(R.string.game_import_pgn) to onImportPgnGame,
            stringResource(R.string.game_import_fen) to onImportFenGame
        ) else listOf()
    ClickableListItemsDialog(
        onDismiss = onDismiss,
        items =
            offlineContents +
            listOf(
                stringResource(R.string.game_export_pgn) to onExportGamePgn,
                stringResource(R.string.game_export_fen) to onExportGameFen,
                stringResource(R.string.game_exit) to onExitGame),
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
        onExportGamePgn = {},
        onExportGameFen = {},
        onExitGame = {},
        gameType = GameType.TWO_OFFLINE
    )
}
