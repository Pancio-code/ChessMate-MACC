package com.example.chessmate.sign_in

import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.widget.Toast
import com.example.chessmate.BuildConfig
import com.example.chessmate.ui.utils.HelperClassUser
import com.example.chessmate.ui.utils.UserAPI
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.tasks.await
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

class AuthUIClient(
    private val context: Context,
    private val oneTapClient: SignInClient,
    private val loginToogle : () -> Unit,
    private val loadingText : (s : String) -> Unit,
    private val signInViewModel: SignInViewModel
    ) {
    private val auth = Firebase.auth
    private val userRemoteService : UserAPI = HelperClassUser.getIstance()
    private val token = BuildConfig.TOKEN
    private val gson = Gson()

    suspend fun signIn(): IntentSender? {
        val result = try {
            oneTapClient.beginSignIn(
                buildSignInRequest()
            ).await()
        } catch(e: Exception) {
            e.printStackTrace()
            if(e is CancellationException) throw e
            null
        }
        return result?.pendingIntent?.intentSender
    }

    suspend fun firebaseSignInWithEmailAndPassword(
        email: String, password: String
    ) = try {
        loadingText("Try to log in with email...")
        loginToogle()
        val user = auth.signInWithEmailAndPassword(email,password).await().user
        SignInResult(
            data = user?.run {
                UserData(
                    id = uid,
                    username = displayName,
                    profilePictureUrl = photoUrl?.toString(),
                    email = email,
                )
            },
            errorMessage = null
        )
    } catch(e: Exception) {
        e.printStackTrace()
        if(e is CancellationException) throw e
        val error: String = when(e) {
            is FirebaseAuthInvalidCredentialsException -> "Invalid email or password"
            is FirebaseAuthInvalidUserException -> "User not found"
            else -> "Authentication failed. Please try again."
        }
        SignInResult(
            data = null,
            errorMessage = error
        )
    }

    suspend fun firebaseSignInWithFacebook(result: LoginResult) : SignInResult {
        val token = result.accessToken.token
        val credential = FacebookAuthProvider.getCredential(token)
        return try {
            loadingText("Try to log in with facebook...")
            loginToogle()
            val user = Firebase.auth.signInWithCredential(credential).await().user
            if (user != null) {
                val userResponse =  userRemoteService.get(token=token,id= user.uid)
                if (userResponse.code() == 204) {
                    val data = gson.toJson(
                        UserData(
                            id = user.uid,
                            email = user.email,
                            profilePictureUrl = user.photoUrl?.toString(),
                            username = user.displayName
                        )
                    )
                    userRemoteService.create(token = token, body = data)
                }
            }
            SignInResult(
                data = user?.run {
                    UserData(
                        id = uid,
                        username = displayName,
                        profilePictureUrl = photoUrl?.toString(),
                        email = email,
                    )
                },
                errorMessage = null
            )
        } catch(e: Exception) {
            e.printStackTrace()
            if(e is CancellationException) throw e
            SignInResult(
                data = null,
                errorMessage = e.message
            )
        }
    }

    suspend fun firebaseSignUpWithEmailAndPassword(
        email: String, password: String, username: String
    ) = try {
            loadingText("Try to create your profile...")
            loginToogle()
            val user  = auth.createUserWithEmailAndPassword(email, password).await().user
            if (user != null) {
                val userResponse =  userRemoteService.get(token=token,id= user.uid)
                if (userResponse.code() == 204) {
                    val data = gson.toJson(
                        UserData(
                            id = user.uid,
                            email = user.email,
                            profilePictureUrl = user.photoUrl?.toString(),
                            username = username
                        )
                    )
                    userRemoteService.create(token = token, body = data)
                }
            }
            SignInResult(
                data = user?.run {
                    UserData(
                        id = uid,
                        username = username,
                        profilePictureUrl = photoUrl?.toString(),
                        email = email
                    )
                },
                errorMessage = null
            )
        } catch(e: Exception) {
            e.printStackTrace()
            if(e is CancellationException) throw e
            val error: String = when(e) {
                is FirebaseAuthWeakPasswordException -> "The password is too weak. Please choose a stronger password."
                is FirebaseAuthInvalidCredentialsException -> "Invalid email format. Please enter a valid email address."
                is FirebaseAuthUserCollisionException -> "An account with this email address already exists. Please use a different email."
                else -> "Registration failed. Please try again."
            }
            SignInResult(
                data = null,
                errorMessage = error
            )
        }

    suspend fun signInWithIntent(intent: Intent): SignInResult {
        val credential = oneTapClient.getSignInCredentialFromIntent(intent)
        val googleIdToken = credential.googleIdToken
        val googleCredentials = GoogleAuthProvider.getCredential(googleIdToken, null)
        return try {
            loadingText("Try to login with google...")
            loginToogle()
            val user = auth.signInWithCredential(googleCredentials).await().user
            if (user != null) {
                val userResponse =  userRemoteService.get(token=token,id= user.uid)
                if (userResponse.code() == 204) {
                    val data = gson.toJson(UserData(
                        id = user.uid,
                        email = user.email,
                        profilePictureUrl = user.photoUrl?.toString(),
                        username = user.displayName
                        )
                    )
                    userRemoteService.create(token=token, body = data)
                }
            }
            SignInResult(
                data = user?.run {
                    UserData(
                        id = uid,
                        email = email,
                        profilePictureUrl = photoUrl?.toString(),
                        username = displayName,
                    )
                },
                errorMessage = null
            )
        } catch(e: Exception) {
            e.printStackTrace()
            if(e is CancellationException) throw e
            SignInResult(
                data = null,
                errorMessage = e.message
            )
        }
    }

    suspend fun signOut() : SignInResult = try {
            oneTapClient.signOut().await()
            auth.signOut()
            SignInResult(
                data = null,
                errorMessage = null
            )
        } catch(e: Exception) {
            e.printStackTrace()
            if(e is CancellationException) throw e
            SignInResult(
                data = null,
                errorMessage = e.message
            )
        }


    suspend fun getSignedInUser(): UserData? = auth.currentUser?.run {
        try {
            val userResponse =  userRemoteService.get(token = token,id = uid)
            if (userResponse.code() == 200) {
                userResponse.body()
            } else {
                Toast.makeText(
                    context,
                    userResponse.message(),
                    Toast.LENGTH_LONG
                ).show()
                null
            }
        } catch (e: Exception) {
            e.printStackTrace()
            if (e is CancellationException) throw e
            null
        }
    }

    private fun buildSignInRequest(): BeginSignInRequest {
        return BeginSignInRequest.Builder()
            .setGoogleIdTokenRequestOptions(
                BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                    .setSupported(true)
                    .setFilterByAuthorizedAccounts(false)
                    .setServerClientId(BuildConfig.WEB_CLIENT_ID)
                    .build()
            )
            .setAutoSelectEnabled(true)
            .build()
    }

    suspend fun sendPasswordResetEmail(email: String) = try {
        auth.sendPasswordResetEmail(email).await()
        Toast.makeText(
            context,
            "Password reset email sent!",
            Toast.LENGTH_LONG
        ).show()
    } catch (e: Exception) {
        e.printStackTrace()
        if(e is CancellationException) throw e
        Toast.makeText(
            context,
            e.message,
            Toast.LENGTH_LONG
        ).show()
    }

    suspend fun confirmEdits(userId: String?, newEmail: String?, profilePictureUrl: String?, newUsername: String?, country: String?, newAvatarFile: File?): Boolean {
        val userData = UserData(
            id = userId.toString(),
            email = newEmail,
            profilePictureUrl = profilePictureUrl,
            username = newUsername,
            country = country
        )
        try {
            val data = gson.toJson(userData)
            val jsonRequestBody = data.toRequestBody("application/json".toMediaTypeOrNull())

            if (newAvatarFile.toString() != "") {
                val fileRequestBody = newAvatarFile!!.asRequestBody("image/*".toMediaTypeOrNull())
                val filePart = MultipartBody.Part.createFormData("file", newAvatarFile.name, fileRequestBody)
                userRemoteService.updateWithAvatar(token=BuildConfig.TOKEN, id= userId.toString(), body = jsonRequestBody, image= filePart)
            } else {
                userRemoteService.update(token=BuildConfig.TOKEN, id= userId.toString(), body = jsonRequestBody)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            if (e is CancellationException) throw e
        }
        return signInViewModel.setUserData(SignInResult(data = userData, errorMessage = null))
    }
}
