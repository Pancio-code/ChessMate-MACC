package com.example.chessmate.sign_in

import com.example.chessmate.BuildConfig

data class SignInResult(
    val data: UserData?,
    val errorMessage: String?
)

data class UserData(
    val id: String = "-1",
    val email: String? = null,
    val profilePictureUrl: String? = "default.jpg",
    val username: String? =  "",
    val matchesPlayed: Int = 0,
    val matchesWon: Int = 0,
    val eloRank: Float = 400.00f,
    val emailVerified: Boolean = false,
    val provider: String? = null,
    val country: String? = null,
    val signupDate: String? = null,
)

object UserDataHelper {
    const val AVATAR_URL : String = "${BuildConfig.API_URL}/api/v1/user/avatar"
}