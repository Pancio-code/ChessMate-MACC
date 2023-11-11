package com.example.chessmate.sign_in

data class UserAuthState(
    val state: UserAuthStateType = UserAuthStateType.UNAUTHENTICATED,
    val loading: Boolean = false,
    val error: String? = null
)

enum class UserAuthStateType {
    UNAUTHENTICATED,
    AUTHENTICATED
}