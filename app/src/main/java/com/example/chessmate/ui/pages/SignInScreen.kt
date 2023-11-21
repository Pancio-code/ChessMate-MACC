package com.example.chessmate.ui.pages

import android.app.Activity
import android.widget.Toast
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.result.ActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import com.example.chessmate.R
import com.example.chessmate.sign_in.AuthUIClient
import com.example.chessmate.sign_in.SignInState
import com.example.chessmate.sign_in.SignInViewModel
import com.example.chessmate.ui.components.EmailFieldComponent
import com.example.chessmate.ui.components.FacebookButton
import com.example.chessmate.ui.components.PasswordFieldComponent
import com.example.chessmate.ui.navigation.ChessMateNavigationActions
import com.example.chessmate.ui.navigation.ChessMateRoute
import com.example.chessmate.ui.utils.ChessMateNavigationType
import kotlinx.coroutines.launch

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun SignInScreen(
    state: SignInState? = SignInState(),
    modifier: Modifier,
    navigationType: ChessMateNavigationType,
    authHandler: AuthUIClient? = null,
    authViewModel: SignInViewModel? = null,
    googleIntentLaucher: ManagedActivityResultLauncher<IntentSenderRequest, ActivityResult>? = null,
    navController: NavController? = null
) {
    val lyfescope = rememberCoroutineScope()
    val scroll = rememberScrollState(0)
    val context = LocalContext.current
    LaunchedEffect(key1 = state!!.signInError) {
        state.signInError?.let { error ->
            Toast.makeText(
                context,
                error,
                Toast.LENGTH_LONG
            ).show()
        }
    }


    var email by rememberSaveable(
        stateSaver = TextFieldValue.Saver,
        init = {
            mutableStateOf(
                value = TextFieldValue(
                    text = ""
                )
            )
        }
    )
    var password by rememberSaveable(
        stateSaver = TextFieldValue.Saver,
        init = {
            mutableStateOf(
                value = TextFieldValue(
                    text = ""
                )
            )
        }
    )
    val keyboard = LocalSoftwareKeyboardController.current
    if (navigationType == ChessMateNavigationType.BOTTOM_NAVIGATION) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .verticalScroll(scroll),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            EmailFieldComponent(
                email = email,
                onEmailValueChange = { newValue ->
                    email = newValue
                    state.signInError = null
                },
                errors = state.signInError
            )
            Spacer(modifier = Modifier.size(8.dp))
            PasswordFieldComponent(
                password = password,
                label = "Password",
                onPasswordValueChange = { newValue ->
                    password = newValue
                    state.signInError = null
                },
                error = state.signInError
            )
            Spacer(modifier = Modifier.size(8.dp))
            Button(
                onClick = {
                    keyboard?.hide()
                    if(email.text == "") {
                        state.signInError = "Email is needed"
                        state.fieldType = "Email"
                        return@Button
                    }
                    if (password.text == "") {
                        state.signInError = "Password is needed"
                        state.fieldType = "Password"
                        return@Button
                    }

                    lyfescope.launch {
                        val signInResult = authHandler?.firebaseSignInWithEmailAndPassword(
                            email.text,
                            password.text
                        )
                        authViewModel?.onSignInResult(
                            signInResult!!
                        )
                    }
                }
            ) {
                Icon(
                    imageVector = Icons.Default.Email,
                    modifier = Modifier.size(16.dp),
                    contentDescription = "Email icon"
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = "Sign In")
            }
            Spacer(modifier = Modifier.size(8.dp))
                Text(
                    modifier = Modifier.clickable {
                        navController?.navigate(ChessMateRoute.CONTACT) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                    text = "Have you forgotten your password?",
                    style = MaterialTheme.typography.bodySmall,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.outline,
                    fontSize = 15.sp
                )
                Spacer(modifier = Modifier.size(8.dp))
                Text(
                    modifier = Modifier.clickable {
                        navController?.navigate(ChessMateRoute.SIGN_UP) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                    text = "Do you want Sign Up?",
                    style = MaterialTheme.typography.bodySmall,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.outline,
                    fontSize = 15.sp
                )
            Spacer(modifier = Modifier.size(30.dp))
                Button(onClick = {
                    lyfescope.launch {
                        val signInIntentSender =
                        authHandler!!.signIn()
                        googleIntentLaucher!!.launch(
                        IntentSenderRequest.Builder(
                            signInIntentSender
                                ?: return@launch
                        ).build()
                    )}}
                ) {
                    Icon(
                        painterResource(id = R.drawable.ic_google),
                        modifier = Modifier.size(16.dp),
                        contentDescription = "Google icon"
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = "Sign in")
                }
            Spacer(modifier = Modifier.size(8.dp))
            FacebookButton(authHandler = authHandler!!,authViewModel = authViewModel, scope = lyfescope)
        }
    } else {
        Row(
            modifier = modifier
                .fillMaxSize(),
        ) {
            Column(
                modifier = modifier
                    .fillMaxSize()
                    .verticalScroll(scroll),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                EmailFieldComponent(
                    email = email,
                    onEmailValueChange = { newValue ->
                        email = newValue
                        state.signInError = null
                    },
                    errors = state.signInError
                )
                Spacer(modifier = Modifier.size(8.dp))
                PasswordFieldComponent(
                    password = password,
                    label = "Password",
                    onPasswordValueChange = { newValue ->
                        password = newValue
                        state.signInError = null
                    },
                    error = state.signInError
                )
                Spacer(modifier = Modifier.size(8.dp))
                Button(
                    onClick = {
                        keyboard?.hide()
                        if (email.text == "") {
                            state.signInError = "Email is needed"
                            state.fieldType = "Email"
                            return@Button
                        }
                        if (password.text == "") {
                            state.signInError = "Password is needed"
                            state.fieldType = "Password"
                            return@Button
                        }

                        lyfescope.launch {
                            val signInResult = authHandler?.firebaseSignInWithEmailAndPassword(
                                email.text,
                                password.text
                            )
                            authViewModel?.onSignInResult(
                                signInResult!!
                            )
                        }
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Email,
                        modifier = Modifier.size(16.dp),
                        contentDescription = "Email icon"
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = "Sign In")
                }
                Spacer(modifier = Modifier.size(8.dp))
                Text(
                    modifier = Modifier.clickable {
                        navController?.navigate(ChessMateRoute.CONTACT) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                    text = "Have you forgotten your password?",
                    style = MaterialTheme.typography.bodySmall,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.outline,
                    fontSize = 15.sp
                )
                Spacer(modifier = Modifier.size(8.dp))
                Text(
                    modifier = Modifier.clickable {
                        navController?.navigate(ChessMateRoute.SIGN_UP) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                    text = "Do you want Sign Up?",
                    style = MaterialTheme.typography.bodySmall,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.outline,
                    fontSize = 15.sp
                )
            }
            Column(
                modifier = modifier
                    .fillMaxSize()
                    .verticalScroll(scroll),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Button(onClick = {
                    lyfescope.launch {
                        val signInIntentSender = authHandler!!.signIn()
                        googleIntentLaucher!!.launch(
                            IntentSenderRequest.Builder(
                                signInIntentSender
                                    ?: return@launch
                            ).build()
                        )
                    }
                }
                ) {
                    Icon(
                        painterResource(id = R.drawable.ic_google),
                        modifier = Modifier.size(16.dp),
                        contentDescription = "Google icon"
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = "Sign in")
                }
                Spacer(modifier = Modifier.size(8.dp))
                FacebookButton(authHandler = authHandler!!,authViewModel = authViewModel, scope = lyfescope)
            }
        }
    }
}

@Preview
@Composable
fun SignInPagePreview() {
    SignInScreen(modifier = Modifier, navigationType = ChessMateNavigationType.BOTTOM_NAVIGATION)
}

@Preview
@Composable
fun SignInPageTabletPreview() {
    SignInScreen(modifier = Modifier, navigationType = ChessMateNavigationType.PERMANENT_NAVIGATION_DRAWER)
}