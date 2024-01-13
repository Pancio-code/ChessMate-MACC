package com.example.chessmate.sign_in

data class SignInResult(
    val data: UserData?,
    val errorMessage: String?
)

data class UserData(
    val id: String? = null,
    val email: String? = null,
    val profilePictureUrl: String? = null,
    val username: String? = null,
    val matchesPlayed: Int = 0,
    val matchesWon: Int = 0,
    val eloRank: Float = 0f,
    val emailVerified: Boolean = false,
    val provider : String? = null
)
