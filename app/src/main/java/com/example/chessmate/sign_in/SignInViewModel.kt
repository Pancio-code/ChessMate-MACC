package com.example.chessmate.sign_in

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SignInViewModel: ViewModel() {

    private val _signInState = MutableStateFlow(SignInState())
    val signInState = _signInState.asStateFlow()

    private val _isAuthenticated = MutableStateFlow(UserAuthState(loading = true))
    val isAuthenticated : StateFlow<UserAuthState> = _isAuthenticated

    private val _userData = MutableStateFlow(SignInResult(data = null,errorMessage = null))
    private val userData : StateFlow<SignInResult> = _userData

    fun onSignInResult(result: SignInResult) {
        _signInState.update { it.copy(
            isSignInSuccessful = result.data != null,
            signInError = result.errorMessage
        ) }
    }


    fun getAuthenticationState(handler: AuthUIClient) {
        viewModelScope.launch {
            val userData = handler.getSignedInUser()
            if (userData != null) {
                _isAuthenticated.value = UserAuthState( state = UserAuthStateType.AUTHENTICATED,loading = false)
                _userData.value = SignInResult(data = userData, errorMessage = null)
                _signInState.value = SignInState(
                    isSignInSuccessful = true,
                    signInError = null
                )
            } else {
                _isAuthenticated.value = UserAuthState( state = UserAuthStateType.UNAUTHENTICATED,loading = false)
                _userData.value = SignInResult(data = null, errorMessage = null)
                _signInState.value = SignInState(
                    isSignInSuccessful = false,
                    signInError = null
                )
            }
        }
    }

    fun resetState() {
        _signInState.update { SignInState() }
    }

    fun getUserData(): UserData? {
        return userData.value.data
    }
}