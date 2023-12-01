package com.example.chessmate.sign_in

data class SignInResult(
    val data: UserData?,
    val errorMessage: String?
)

data class UserData(
    val id: String? = null,
    val email: String? = null,
    val emailVerified: Boolean = false,
    val profilePictureUrl: String? = null,
    val provider: String? = null,
    val username: String? = null,
)
