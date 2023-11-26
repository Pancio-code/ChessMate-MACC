package com.example.chessmate.ui.pages.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.chessmate.R
import com.example.chessmate.sign_in.AuthUIClient
import com.example.chessmate.sign_in.UserData
import com.example.chessmate.ui.components.MenuCountryPicker
import com.example.chessmate.ui.utils.ChessMateNavigationType
import com.example.chessmate.ui.theme.light_primary
import com.example.chessmate.ui.theme.dark_primaryContainer

@Composable
fun ProfileEditMode(
    userData: UserData?,
    authHandler: AuthUIClient? = null,
    modifier: Modifier = Modifier,
    navigationType: ChessMateNavigationType,
    toggler: () -> Unit
) {
    var isConfirmMode by remember { mutableStateOf(false) }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(light_primary)
    ) {
        //header
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(dark_primaryContainer),
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
                        onClick = toggler,
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
        CustomRowEdit(title = "Avatar", placeholder = "ok", isConfirmMode = {isConfirmMode = true})
        CustomRowEdit(title = "Username", placeholder = userData?.username.toString(), isConfirmMode = {isConfirmMode = true})
        CustomRowEdit(title = "Name", placeholder = "Jhon", isConfirmMode = {isConfirmMode = true})
        CustomRowEdit(title = "Surname", placeholder = "Smith", isConfirmMode = {isConfirmMode = true})
        CustomRowEdit(title = "Email", placeholder = userData?.email.toString(), isConfirmMode = {isConfirmMode = true})
        CustomRowEdit(title = "Country", placeholder = "", isConfirmMode = {isConfirmMode = true})
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomRowEdit(title: String, placeholder: String, isConfirmMode: () -> Unit){
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
                .background(light_primary)
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
                .background(light_primary),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            when(title) {
                "Avatar" ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Start
                    ) {
                        Avatar(imageResourceId = R.drawable.profile_picture)
                    }
                "Country" ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Start
                    ) {
                        MenuCountryPicker(isConfirmMode)
                    }
                else ->
                    OutlinedTextField(
                        value = textValue,
                        onValueChange = { textValue = it; isConfirmMode() },
                        modifier = Modifier.fillMaxWidth(),
                        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Text),
                        textStyle = LocalTextStyle.current.copy(fontSize = 16.sp, color = Color.White),
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            focusedBorderColor = light_primary, // Change the border color when focused
                            unfocusedBorderColor = light_primary, // Change the border color when unfocused
                            disabledBorderColor = light_primary, // Change the border color when disabled
                            containerColor = light_primary
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

// to delete
@Composable
fun Avatar(imageResourceId: Int) {
    Surface(
        modifier = Modifier
            .size(80.dp)
            .clip(CircleShape),
        color = Color.Transparent
    ) {
        Image(
            painter = painterResource(id = imageResourceId),
            contentDescription = null,
            modifier = Modifier
                .size(80.dp)
                .clip(CircleShape)
        )
    }
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