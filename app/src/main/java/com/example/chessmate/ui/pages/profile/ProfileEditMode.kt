package com.example.chessmate.ui.pages.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Undo
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.chessmate.sign_in.AuthUIClient
import com.example.chessmate.sign_in.UserData
import com.example.chessmate.ui.utils.ChessMateNavigationType
import kotlinx.coroutines.launch

@Composable
fun ProfileEditMode(
    userData: UserData?,
    authHandler: AuthUIClient? = null,
    modifier: Modifier = Modifier,
    navigationType: ChessMateNavigationType,
    toggler: () -> Unit
) {
    val lyfescope = rememberCoroutineScope()
    val scroll = rememberScrollState(0)
    val context = LocalContext.current
    var haveUserEdit by remember { mutableStateOf(false) }
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scroll),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(32.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = "Edit Profile",
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.primary
            )
        }
        Spacer(modifier = Modifier.height(32.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Button(
                onClick = toggler,
                colors = ButtonDefaults.buttonColors(
                    contentColor = MaterialTheme.colorScheme.onSecondaryContainer,
                    containerColor = MaterialTheme.colorScheme.secondaryContainer
                )
            ) {
                Icon(
                    imageVector = Icons.Default.Cancel,
                    modifier = Modifier.size(16.dp),
                    contentDescription = "Cancel icon"
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = "Cancel")
            }
            Spacer(modifier = Modifier.height(32.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = "Profile image")
                Spacer(modifier = Modifier.width(16.dp))
                Text(text = "Cancel")
            }
            Spacer(modifier = Modifier.width(32.dp))
            Button(
                enabled = haveUserEdit,
                onClick = toggler,
                colors = ButtonDefaults.buttonColors(
                    contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            ) {
                Icon(
                    imageVector = Icons.Default.Save,
                    modifier = Modifier.size(16.dp),
                    contentDescription = "Confirm icon"
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = "Confirm")
            }
        }
    }
}

@Preview
@Composable
fun ProfileEditModePreview() {
    ProfileEditMode(
        userData = UserData(
            userId = "1",
            profilePictureUrl = null,
            username = "Andrea",
            email = "andrea.pancio00@gmail.com",
            emailVerified = false,
            provider = null
        ),
        modifier = Modifier,
        authHandler = null,
        navigationType = ChessMateNavigationType.BOTTOM_NAVIGATION,
        toggler = {}
    )
}

@Preview
@Composable
fun ProfileEditModeTabletPreview() {
    ProfileEditMode(
        userData = UserData(
            userId = "1",
            profilePictureUrl = null,
            username = "Andrea",
            email = "andrea.pancio00@gmail.com",
            emailVerified = false,
            provider = null
        ),
        modifier = Modifier,
        authHandler = null,
        navigationType = ChessMateNavigationType.NAVIGATION_RAIL,
        toggler = {}
    )
}