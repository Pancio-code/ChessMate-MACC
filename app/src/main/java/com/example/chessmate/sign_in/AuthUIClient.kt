package com.example.chessmate.sign_in

import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.util.Log
import android.widget.Toast
import com.example.chessmate.BuildConfig
import com.example.chessmate.ui.utils.HelperClass
import com.example.chessmate.ui.utils.UserAPI
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import com.google.gson.JsonObject
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import okhttp3.ResponseBody

class AuthUIClient(
    private val context: Context,
    private val oneTapClient: SignInClient,
    //private val db : FirebaseFirestore
    ) {
    private val auth = Firebase.auth
    private val userRemoteService : UserAPI = HelperClass.getIstance()
    private val token = BuildConfig.TOKEN
    private val gson = Gson()
    //private val dbUsers: CollectionReference = db.collection("Users_Info")

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
        val user = auth.signInWithEmailAndPassword(email,password).await().user
        SignInResult(
            data = user?.run {
                UserData(
                    id = uid,
                    username = displayName,
                    profilePictureUrl = photoUrl?.toString(),
                    email = email,
                    emailVerified = isEmailVerified,
                    provider = EmailAuthProvider.PROVIDER_ID
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
            val user = Firebase.auth.signInWithCredential(credential).await().user;
            if (user != null) {
                var userResponse =  userRemoteService.get(token=token,id= user.uid)
                if (userResponse.code() == 290) {
                    val data = gson.toJson(
                        UserData(
                            id = user.uid,
                            email = user.email,
                            emailVerified = user.isEmailVerified,
                            profilePictureUrl = user.photoUrl?.toString(),
                            provider = user.providerId,
                            username = user.displayName
                        )
                    );
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
                        emailVerified = isEmailVerified,
                        provider = providerId
                    )
                },
                errorMessage = null
            )
        } catch(e: Exception) {
            e.printStackTrace()
            if(e is CancellationException) throw e
            Toast.makeText(
                context,
                e.message,
                Toast.LENGTH_LONG
            ).show()
            SignInResult(
                data = null,
                errorMessage = null
            )
        }
    }

    suspend fun firebaseSignUpWithEmailAndPassword(
        email: String, password: String, username: String
    ) = try {
            val user  = auth.createUserWithEmailAndPassword(email, password).await().user
            if (user != null) {
                var userResponse =  userRemoteService.get(token=token,id= user.uid)
                if (userResponse.code() == 290) {
                    val data = gson.toJson(
                        UserData(
                            id = user.uid,
                            email = user.email,
                            emailVerified = user.isEmailVerified,
                            profilePictureUrl = user.photoUrl?.toString(),
                            provider = user.providerId,
                            username = username
                        )
                    );
                    userRemoteService.create(token = token, body = data)
                }
            }
            SignInResult(
                data = user?.run {
                    UserData(
                        id = uid,
                        username = username,
                        profilePictureUrl = photoUrl?.toString(),
                        email = email,
                        emailVerified = isEmailVerified,
                        provider = EmailAuthProvider.PROVIDER_ID
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
            val user = auth.signInWithCredential(googleCredentials).await().user
            if (user != null) {
                var userResponse =  userRemoteService.get(token=token,id= user.uid)
                if (userResponse.code() == 290) {
                    val data = gson.toJson(UserData(
                        id = user.uid,
                        email = user.email,
                        emailVerified = user.isEmailVerified,
                        profilePictureUrl = user.photoUrl?.toString(),
                        provider = user.providerId,
                        username = user.displayName
                        )
                    );
                    userRemoteService.create(token=token, body = data)
                }
            }
            SignInResult(
                data = user?.run {
                    UserData(
                        id = uid,
                        email = email,
                        emailVerified = isEmailVerified,
                        profilePictureUrl = photoUrl?.toString(),
                        provider = providerId,
                        username = displayName,
                    )
                },
                errorMessage = null
            )
        } catch(e: Exception) {
            e.printStackTrace()
            if(e is CancellationException) throw e
            Toast.makeText(
                context,
                e.message,
                Toast.LENGTH_LONG
            ).show()
            SignInResult(
                data = null,
                errorMessage = null
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

    suspend fun reloadFirebaseUser() = try {

        auth.currentUser?.reload()?.await()
        val user = auth.currentUser
        SignInResult(
            data = user?.run {
                UserData(
                    id = uid,
                    username = displayName,
                    profilePictureUrl = photoUrl?.toString(),
                    email = email,
                    emailVerified = isEmailVerified,
                    provider = providerId
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


    suspend fun getSignedInUser(): UserData? = auth.currentUser?.run {
        try {
            var userResponse =  userRemoteService.get(token = token,id = uid)
            if (userResponse.code() == 200) {
                Log.d("test",gson.toJson(userResponse.body()))
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

   suspend fun sendEmailVerification() = try {
        auth.currentUser?.sendEmailVerification()?.await()
       Toast.makeText(
           context,
           "Email verification sent!",
           Toast.LENGTH_LONG
       ).show()
    } catch (e: Exception) {
       e.printStackTrace()
       if(e is CancellationException) throw e
       SignInResult(
           data = null,
           errorMessage = e.message
       )
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
}
