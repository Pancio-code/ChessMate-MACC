package com.example.chessmate.ui.pages.profile

import android.Manifest
import android.content.Context
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import coil.compose.AsyncImage
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.example.chessmate.camera.photo_capture.CameraScreen
import com.example.chessmate.matches.MatchesViewModel
import com.example.chessmate.sign_in.AuthUIClient
import com.example.chessmate.sign_in.SignInViewModel
import com.example.chessmate.sign_in.UserData
import com.example.chessmate.sign_in.UserDataHelper
import com.example.chessmate.ui.components.MenuCountryPicker
import com.example.chessmate.ui.theme.light_primary
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import kotlinx.coroutines.delay
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import java.io.File
import java.io.InputStream

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun ProfileEditMode(
    modifier: Modifier = Modifier,
    userData: UserData?,
    authHandler: AuthUIClient? = null,
    authViewModel: SignInViewModel? = null,
    matchesViewModel: MatchesViewModel? = null,
    toggler: () -> Unit,
    painter: AsyncImagePainter
) {
    var isConfirmMode by remember { mutableStateOf(false) }
    var showCamera by remember { mutableStateOf(false) }
    var showDialog by remember { mutableStateOf(false) }
    var showDialogScore by remember { mutableStateOf(false) }
    var expanded by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }
    val cameraPermissionState: PermissionState = rememberPermissionState(Manifest.permission.CAMERA)
    val lifecycleOwner = LocalLifecycleOwner.current
    var newUsername by remember { mutableStateOf(userData!!.username.toString()) }
    var newEmail by remember { mutableStateOf(userData!!.email.toString()) }
    var newCounty by remember { mutableStateOf(userData!!.country.toString()) }
    var newAvatarPath by remember { mutableStateOf(userData!!.profilePictureUrl) }
    var tmpAvatarPath by remember { mutableStateOf("") }
    var newAvatarFile by remember { mutableStateOf(File(""))}
    val context: Context = LocalContext.current

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.inverseOnSurface)
    ) {
        //header
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.primaryContainer),
        ) {
            IconButton(
                onClick = toggler,
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .padding(end = 8.dp)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = Color.LightGray
                )
            }
            Box(
                modifier = Modifier.align(Alignment.Center),
            ) {
                Text(
                    text = "Edit Profile",
                    modifier = Modifier.align(Alignment.Center),
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                )
            }
            if (isConfirmMode) {
                Box(
                    modifier = Modifier.align(Alignment.CenterEnd)
                ) {
                    IconButton(
                        onClick = {
                            isLoading = true
                            lifecycleOwner.lifecycleScope.launch {
                                authHandler!!.confirmEdits(userId = userData?.id, newEmail = newEmail, profilePictureUrl = newAvatarPath , newUsername = newUsername, country = newCounty, newAvatarFile = newAvatarFile, userData = userData!!)
                                delay(1000)
                                joinAll()
                                toggler()
                            }
                        },
                        modifier = Modifier.padding(end = 40.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = "Check",
                            tint = Color.Green
                        )
                    }
                    IconButton(
                        onClick = toggler,
                        modifier = Modifier.padding(start = 40.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close",
                            tint = Color.Red
                        )
                    }
                }
            }
        }
        CustomRowEdit(title = "Avatar", placeholder = newAvatarPath!!, isConfirmMode = {isConfirmMode = true}, isExpanded = {expanded = true}, onValueChange = {newAvatarPath = it}, tmpAvatarPath = tmpAvatarPath, painter = painter)
        CustomRowEdit(title = "Username", placeholder = newUsername, isConfirmMode = {isConfirmMode = true}, onValueChange = {newUsername = it})
        CustomRowEdit(title = "Email", placeholder = newEmail, isConfirmMode = {isConfirmMode = true}, onValueChange = {newEmail = it})
        CustomRowEdit(title = "Country", placeholder = newCounty, isConfirmMode = {isConfirmMode = true}, onValueChange = {newCounty = it})
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp, horizontal = 24.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ){
            ElevatedButton(
                colors = ButtonColors(
                    containerColor = MaterialTheme.colorScheme.errorContainer,
                    contentColor = Color.White,
                    disabledContainerColor = Color.Green,
                    disabledContentColor = Color.Green),
                onClick = {showDialog = true}) {
                Text("Delete Account", fontSize = 16.sp)
            }
            ElevatedButton(
                colors = ButtonColors(
                    containerColor = MaterialTheme.colorScheme.errorContainer,
                    contentColor = Color.White,
                    disabledContainerColor = Color.Green,
                    disabledContentColor = Color.Green),
                onClick = {showDialogScore = true}) {
                Text("Reset Score", fontSize = 16.sp)
            }
        }
        if (showDialog) {
            DeleteDialog(
                onConfirm = {
                    isLoading = true
                    lifecycleOwner.lifecycleScope.launch {
                        authHandler!!.deleteUser(userId = userData?.id)
                        val signInResult = authHandler.signOut()
                        authViewModel?.onSignInResult(
                            signInResult,
                            context = context
                        )
                        delay(1000)
                        joinAll()
                    }
                    showDialog = false
                },
                onDismiss = {
                    showDialog = false
                }
            )
        }
        if (showDialogScore){
            DeleteDialog(
                onConfirm = {
                    isLoading = true
                    lifecycleOwner.lifecycleScope.launch {
                        authHandler!!.resetScore(userData = userData!!)
                        matchesViewModel!!.setMatches(emptyList())
                        delay(1000)
                        joinAll()
                        toggler()
                    }
                    showDialogScore = false
                },
                onDismiss = {
                    showDialogScore = false
                }
            )
        }
        if (expanded) {
            ChangeAvatar(
                onDismiss = {expanded = false},
                showCamera = {showCamera = true}, //called when user click on take a picture
                onUpload = { //called when user click on upload image from gallery
                    newValue:Uri -> val imageStream: InputStream? = context.contentResolver.openInputStream(newValue) //the uri of the new image
                    newAvatarPath = "${generateRandomString(10)}.jpg" //path to send to backend
                    tmpAvatarPath = newValue.toString() //local path for the preview of the image in the avatar field
                    newAvatarFile = getImageFile(context = context, path = newAvatarPath!!, imageStream = imageStream) //the File to send and store in the backend
                    isConfirmMode = true
                }
            )
        }
        if (showCamera){
            ShowAskPermissionAndCamera(
                hasPermission = cameraPermissionState.status.isGranted,
                cameraPermissionState = cameraPermissionState,
                lifecycleOwner = lifecycleOwner,
                onDismiss = {showCamera = false;expanded = false},
                onNewAvatar = { //called when user after taking a picture with the camera
                    newValue:Uri -> val imageStream: InputStream? = context.contentResolver.openInputStream(newValue) //the uri of the new image
                    newAvatarPath = "${generateRandomString(10)}.jpg" //path to send to backend
                    tmpAvatarPath = newValue.toString() //local path for the preview of the image in the avatar field
                    newAvatarFile = getImageFile(context = context, path = newAvatarPath!!, imageStream = imageStream) //the File to send and store in the backend
                    isConfirmMode = true
                }
            )
        }
        if (isLoading) {
            LoadingDialog()
        }

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomRowEdit(title: String, placeholder: String, isConfirmMode: () -> Unit, isExpanded: () -> Unit = { }, onValueChange: (String) -> Unit, tmpAvatarPath: String = "", painter: AsyncImagePainter? = null){
    var textValue by remember { mutableStateOf(placeholder) }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .height(52.dp),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ){
        Column(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.inverseOnSurface)
                .fillMaxHeight()
                .padding(vertical = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Row(
                modifier = Modifier.width(100.dp)
            ) {
                Text(text = title, color = Color.LightGray, fontSize = 16.sp)
            }
        }
        Column(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.inverseOnSurface),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            when(title) {
                "Avatar" ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Start
                    ) {
                        Avatar(painter = painter!!, isExpanded, uploadedImagePath = tmpAvatarPath)
                    }
                "Country" ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Start
                    ) {
                        MenuCountryPicker(isConfirmMode, onValueChange, placeholder)
                    }
                else ->
                    OutlinedTextField(
                        value = textValue,
                        onValueChange = { textValue = it; onValueChange(textValue); isConfirmMode() },
                        modifier = Modifier.fillMaxWidth(),
                        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Text),
                        textStyle = LocalTextStyle.current.copy(fontSize = 16.sp, color = Color.White),
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            focusedBorderColor = MaterialTheme.colorScheme.inverseOnSurface, // Change the border color when focused
                            unfocusedBorderColor = MaterialTheme.colorScheme.inverseOnSurface, // Change the border color when unfocused
                            disabledBorderColor = MaterialTheme.colorScheme.inverseOnSurface, // Change the border color when disabled
                            containerColor = MaterialTheme.colorScheme.inverseOnSurface
                        )
                    )
            }
        }
    }
    HorizontalDivider(
        color = Color.Gray,
        thickness = 1.dp,
        modifier = Modifier
            .fillMaxWidth()
    )
}


@Composable
fun ChangeAvatar(
    onDismiss: () -> Unit,
    showCamera: () -> Unit,
    onUpload: (Uri) -> Unit
) {
    val getContent = rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) { uri: Uri? ->
        onUpload(uri!!)
        onDismiss()
    }
    Dialog(onDismissRequest = { onDismiss() }) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(
                containerColor = light_primary,
            ),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "Take a picture",
                        color = Color.White,
                        modifier = Modifier.clickable {showCamera()})
                }
                HorizontalDivider(
                    color = Color.Gray,
                    thickness = 1.dp,
                    modifier = Modifier
                        .fillMaxWidth()
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "Choose a photo",
                        color = Color.White,
                        modifier = Modifier.clickable { getContent.launch("image/*") })
                }
            }
        }
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun ShowAskPermissionAndCamera(
    hasPermission: Boolean,
    cameraPermissionState: PermissionState,
    lifecycleOwner: LifecycleOwner,
    onDismiss: () -> Unit,
    onNewAvatar: (Uri) -> Unit
    ) {
    if (hasPermission) {
        Dialog(onDismissRequest = { onDismiss() }) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1000.dp),
                shape = RoundedCornerShape(16.dp),
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    CameraScreen(onImageTaken = onDismiss, onNewAvatar = onNewAvatar)
                }
            }
        }
    } else {
        LaunchedEffect(lifecycleOwner) {
            cameraPermissionState.launchPermissionRequest()
        }
    }
}

@Composable
fun DeleteDialog(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
) {
    Dialog(onDismissRequest = { onDismiss() }){
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp),
            shape = RoundedCornerShape(16.dp),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete",
                    tint = Color.LightGray,
                    modifier = Modifier.padding(bottom = 24.dp)
                )
                Text(
                    text = "Do you confirm the operation?",
                    modifier = Modifier.padding(horizontal = 16.dp),
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
                    horizontalArrangement = Arrangement.Center,
                ) {
                    TextButton(
                        onClick = { onDismiss() },
                        modifier = Modifier.padding(horizontal = 8.dp),
                    ) {
                        Text("Dismiss")
                    }
                    TextButton(
                        onClick = { onConfirm() },
                        modifier = Modifier.padding(horizontal = 8.dp),
                    ) {
                        Text("Confirm")
                    }
                }
            }
        }
    }
}

@Composable
fun LoadingDialog(
) {
    Dialog(onDismissRequest = { null  }) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(90.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .size(40.dp)
                        .padding(8.dp),
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text("Updating...", color = Color.White)
            }
        }
    }
}


// to delete
@Composable
fun Avatar(painter: AsyncImagePainter, isExpanded: () -> Unit, uploadedImagePath: String?) {
    Surface(
        modifier = Modifier
            .size(80.dp)
            .clip(CircleShape)
            .clickable { isExpanded() },
        color = Color.Transparent
    ) {
        if(uploadedImagePath!!.startsWith("content://")){
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(uploadedImagePath)
                    .crossfade(true)
                    .build(),
                contentDescription = "Uploaded Image",
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop,
            )
        } else {
            if(uploadedImagePath == ""){
                Image(
                    painter = painter,
                    contentDescription = "Default Image",
                    modifier = Modifier
                        .size(80.dp)
                        .clip(CircleShape),
                )
            } else {
                Image(
                    rememberAsyncImagePainter(uploadedImagePath),
                    contentDescription = "Image from camera",
                    modifier = Modifier
                        .size(80.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop,
                )
            }
        }
    }
}

fun getImageFile(context: Context, path: String, imageStream: InputStream?): File{
    val imageFile = File(context.cacheDir, path)
    imageStream?.use { inputStream ->
        imageFile.outputStream().use { outputStream ->
            inputStream.copyTo(outputStream)
        }
    }
    return imageFile
}

fun generateRandomString(length: Int): String {
    val allowedChars = ('A'..'Z') + ('a'..'z') + ('0'..'9')
    return (1..length)
        .map { allowedChars.random() }
        .joinToString("")
}

@Preview
@Composable
fun ProfileEditModePreview() {
    val userData = UserData(
        id = "1",
        profilePictureUrl = null,
        username = "Nome Cognome",
        email = "andrea.pancio00@gmail.com",
        emailVerified = false,
        provider = null
    )
    ProfileEditMode(
        userData = userData,
        modifier = Modifier,
        authHandler = null,
        toggler = {},
        painter = rememberAsyncImagePainter("${UserDataHelper.AVATAR_URL}/${userData.id}/${userData.profilePictureUrl}")
    )
}

@Preview
@Composable
fun ProfileEditModeTabletPreview() {
    val userData = UserData(
        id = "1",
        profilePictureUrl = null,
        username = "Nome Cognome",
        email = "andrea.pancio00@gmail.com",
        emailVerified = false,
        provider = null
    )
    ProfileEditMode(
        userData = userData,
        modifier = Modifier,
        authHandler = null,
        toggler = {},
        painter = rememberAsyncImagePainter("${UserDataHelper.AVATAR_URL}/${userData.id}/${userData.profilePictureUrl}")
    )
}