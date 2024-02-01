package com.example.chessmate.matches

data class Match(
    val roomId: String = "",
    val matchType: String = "",
    val userIdOne: String = "",
    val userIdTwo: String = "",
    val usernameUserTwo: String? = null,
    val profilePictureUrlUserTwo: String? = null,
    val results: Int = 0
)

