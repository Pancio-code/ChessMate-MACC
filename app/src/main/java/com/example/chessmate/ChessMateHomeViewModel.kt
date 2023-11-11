package com.example.chessmate

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chessmate.sign_in.AuthUIClient
import com.example.chessmate.sign_in.SignInState
import com.example.chessmate.sign_in.UserAuthState
import com.example.chessmate.sign_in.UserAuthStateType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ChessMateHomeViewModel() :
    ViewModel() {

    // UI state exposed to the UI
    private val _uiState = MutableStateFlow(ChessMateHomeUIState(loading = true))
    val uiState: StateFlow<ChessMateHomeUIState> = _uiState

    private val _state = MutableStateFlow(SignInState())
    val state = _state.asStateFlow()

    private val _isAuthenticated = MutableStateFlow(UserAuthState(loading = true))
    val isAuthenticated: StateFlow<UserAuthState> = _isAuthenticated

    fun getAuthenticationState(handler: AuthUIClient) {
        viewModelScope.launch {
            val newState = if (handler.getSignedInUser() != null) UserAuthState( state = UserAuthStateType.AUTHENTICATED,loading = false)
                    else UserAuthState( state = UserAuthStateType.UNAUTHENTICATED,loading = false)
            _isAuthenticated.value = newState
        }
    }

}

data class ChessMateHomeUIState(
    val isDetailOnlyOpen: Boolean = false,
    val loading: Boolean = false,
    val error: String? = null
)
