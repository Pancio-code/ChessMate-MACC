package com.example.chessmate.ui.components

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.chessmate.R
import com.example.chessmate.sign_in.AuthUIClient
import com.example.chessmate.sign_in.SignInResult
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import kotlinx.coroutines.launch

@Composable
fun FacebookButton(
    authHandler: AuthUIClient,
    modifier: Modifier = Modifier,
) {
    val scope = rememberCoroutineScope()
    val loginManager = LoginManager.getInstance()
    val callbackManager = remember { CallbackManager.Factory.create() }
    val launcher = rememberLauncherForActivityResult(
        loginManager.createLogInActivityResultContract(callbackManager, null)
    ) {
        // nothing to do. handled in FacebookCallback
    }

    DisposableEffect(Unit) {
        loginManager.registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
            override fun onCancel() {
                // do nothing
            }

            override fun onError(error: FacebookException) {
                error.printStackTrace()
                SignInResult(
                    data = null,
                    errorMessage = error.message
                )
            }

            override fun onSuccess(result: LoginResult) {
                scope.launch {
                    authHandler.firebaseSignInWithFacebook(result)
                }
            }
        })

        onDispose {
            loginManager.unregisterCallback(callbackManager)
        }
    }
    Button(
        modifier = modifier,
        onClick = {
            launcher.launch(listOf("email", "public_profile"))
        }) {
            Icon(
                painterResource(id = R.drawable.ic_facebook),
                modifier = Modifier.size(16.dp),
                contentDescription = "Facebook icon"
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = "Sign in")
    }
}