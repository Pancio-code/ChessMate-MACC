package com.example.chessmate.sign_in

data class SignInResult(
    val data: UserData?,
    val errorMessage: String?
)

data class UserData(
    val userId: String,
    val username: String?,
    val profilePictureUrl: String?,
    val email: String?,
    val emailVerified: Boolean = false,
    val provider: String?
)
