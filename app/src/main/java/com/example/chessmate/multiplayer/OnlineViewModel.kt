package com.example.chessmate.multiplayer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class OnlineViewModel: ViewModel() {
    private val _roomData = MutableStateFlow(RoomData())
    val roomData : StateFlow<RoomData> = _roomData.asStateFlow()

    private val _fullViewPage = MutableStateFlow("")
    val fullViewPage = _fullViewPage.asStateFlow()

    fun setFullViewPage(newValue : String) : Unit {
        _fullViewPage.update { newValue }
    }

    fun getRoomDataState(handler: OnlineUIClient) {
        viewModelScope.launch {
            val roomData = handler.getRoom()
            if (roomData != null) {
                _roomData.value = roomData
            } else {
                _roomData.value = RoomData()
            }
        }
    }

    fun getRoomData(): RoomData {
        return roomData.value
    }

    fun setRoomData(newValue : RoomData) : Unit {
        _roomData.update { newValue }
    }


    fun listenForGameChanges(handler: OnlineUIClient) {
        viewModelScope.launch {
            val currentRoomData = roomData.value
            if (currentRoomData != RoomData() && currentRoomData.roomId != null) {
                handler.listenForGameChanges(currentRoomData.roomId) { updatedRoomData ->
                    _roomData.value = updatedRoomData
                }
            }
        }
    }
}