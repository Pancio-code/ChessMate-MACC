package com.example.chessmate.sign_in

data class SignInResult(
    val data: UserData?,
    val errorMessage: String?
)

data class UserData(
    val userId: String? = null,
    val username: String? = null,
    val profilePictureUrl: String? = null,
    val email: String? = null,
    val emailVerified: Boolean = false,
    val provider: String? = null
)
