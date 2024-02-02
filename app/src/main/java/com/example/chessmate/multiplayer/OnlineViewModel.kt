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
    val startColor : StateFlow<Set> = _startColor.asStateFlow()

    private val _depth = MutableStateFlow(5)
    private val depth : StateFlow<Int> = _depth.asStateFlow()

    private val _fullViewPage = MutableStateFlow("")
    val fullViewPage = _fullViewPage.asStateFlow()

    private val _importedFen = MutableStateFlow("")
    val importedFen = _importedFen.asStateFlow()

    fun setFullViewPage(newValue : String) {
        _fullViewPage.update { newValue }
    }

    fun getRoomData(): RoomData {
        return roomData.value
    }

    fun setRoomData(newValue : RoomData) {
        _roomData.update { newValue }
    }

    fun getStartColor(): Set {
        return startColor.value
    }

    fun setStartColor(newValue : Set) {
        _startColor.update { newValue }
    }

    fun getDepth(): Int {
        return depth.value
    }

    fun setDepth(newValue : Int) {
        if (newValue in 5..13) {
            _depth.update { newValue }
        }
    }

    fun getImportedFen(): String {
        return importedFen.value
    }

    fun setImportedFen(newValue : String) {
        _importedFen.update {newValue}
    }

}