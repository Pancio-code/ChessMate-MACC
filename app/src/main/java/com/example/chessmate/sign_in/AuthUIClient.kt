package com.example.chessmate.sign_in

import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.widget.Toast
import com.example.chessmate.R
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuthEmailException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.tasks.await

class AuthUIClient(
    private val context: Context,
    private val oneTapClient: SignInClient,
    private val db : FirebaseFirestore
    ) {
    private val auth = Firebase.auth
    private val dbUsers: CollectionReference = db.collection("Users_Info")

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
                    userId = uid,
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
            val user = Firebase.auth.signInWithCredential(credential).await().user
            if (user != null) {
                dbUsers.add(
                    UserData(
                        userId = user.uid,
                        username = user.displayName,
                        profilePictureUrl = user.photoUrl?.toString(),
                        email = user.email,
                        emailVerified = user.isEmailVerified,
                        provider = user.providerId
                    )
                )
            }
            SignInResult(
                data = user?.run {
                    UserData(
                        userId = uid,
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
    }

    suspend fun firebaseSignUpWithEmailAndPassword(
        email: String, password: String, username: String
    ) = try {
            val user  = auth.createUserWithEmailAndPassword(email, password).await().user
            if (user != null) {
                dbUsers.add(
                    UserData(
                        userId = user.uid,
                        username = username,
                        profilePictureUrl = user.photoUrl?.toString(),
                        email = email,
                        emailVerified = false,
                        provider = EmailAuthProvider.PROVIDER_ID
                    )
                )
            }
            SignInResult(
                data = user?.run {
                    UserData(
                        userId = uid,
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
                dbUsers.add(
                    UserData(
                        userId = user.uid,
                        username = user.displayName,
                        profilePictureUrl = user.photoUrl?.toString(),
                        email = user.email,
                        emailVerified = user.isEmailVerified,
                        provider = user.providerId
                    )
                )
            }
            SignInResult(
                data = user?.run {
                    UserData(
                        userId = uid,
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
    }

    suspend fun signOut() {
        try {
            oneTapClient.signOut().await()
            auth.signOut()
        } catch(e: Exception) {
            e.printStackTrace()
            if(e is CancellationException) throw e
        }
    }

    suspend fun reloadFirebaseUser() = try {
        auth.currentUser?.reload()?.await()
        val user = auth.currentUser
        SignInResult(
            data = user?.run {
                UserData(
                    userId = uid,
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


    fun getSignedInUser(): UserData? = auth.currentUser?.run {
        UserData(
            userId = uid,
            username = displayName,
            profilePictureUrl = photoUrl?.toString(),
            email = email,
            emailVerified = isEmailVerified,
            provider = providerId
        )
    }

    private fun buildSignInRequest(): BeginSignInRequest {
        return BeginSignInRequest.Builder()
            .setGoogleIdTokenRequestOptions(
                BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                    .setSupported(true)
                    .setFilterByAuthorizedAccounts(false)
                    .setServerClientId(context.getString(R.string.web_client_id))
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
