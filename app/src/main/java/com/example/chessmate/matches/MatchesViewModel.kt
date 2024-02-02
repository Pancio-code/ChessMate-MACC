package com.example.chessmate.matches

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class MatchesViewModel: ViewModel() {
    private val _matchList = MutableStateFlow<List<Match>>(emptyList())
    val matchList: StateFlow<List<Match>> = _matchList.asStateFlow()

    var matchQuantity = 0

    fun getMatches():List<Match> {
        matchQuantity = matchList.value.size
        return matchList.value
    }

    fun setMatches(newValue : List<Match>) {
        _matchList.update { newValue }
    }

}