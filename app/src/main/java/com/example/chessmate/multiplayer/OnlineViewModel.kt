package com.example.chessmate.multiplayer

import androidx.lifecycle.ViewModel
import com.example.chessmate.game.model.piece.Set
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class OnlineViewModel: ViewModel() {
    private val _roomData = MutableStateFlow(RoomData())
    val roomData : StateFlow<RoomData> = _roomData.asStateFlow()

    private val _startColor = MutableStateFlow(Set.WHITE)
    private val startColor : StateFlow<Set> = _startColor.asStateFlow()

    private val _fullViewPage = MutableStateFlow("")
    val fullViewPage = _fullViewPage.asStateFlow()

    fun setFullViewPage(newValue : String) : Unit {
        _fullViewPage.update { newValue }
    }

    fun getRoomData(): RoomData {
        return roomData.value
    }

    fun setRoomData(newValue : RoomData) : Unit {
        _roomData.update { newValue }
    }

    fun getStartColor(): Set {
        return startColor.value
    }

    fun setStartColor(newValue : Set) : Unit {
        _startColor.update { newValue }
    }

}