package com.example.chessmate.multiplayer

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.core.content.ContextCompat.getString
import com.example.chessmate.BuildConfig
import com.example.chessmate.R
import com.example.chessmate.sign_in.UserData
import com.example.chessmate.ui.utils.HelperClassOnline
import com.example.chessmate.ui.utils.OnlineAPI
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.gson.Gson
import com.google.gson.JsonObject
import kotlin.coroutines.cancellation.CancellationException

class OnlineUIClient(
    private val context: Context,
    db : FirebaseFirestore,
    private val onlineViewModel: OnlineViewModel,
    private val userData : UserData
) {
    private val roomRemoteService : OnlineAPI = HelperClassOnline.getIstance()
    private val token = BuildConfig.TOKEN
    private val gson = Gson()
    private val roomDbReference = getString(context,R.string.roomDbReference)
    private val dbRooms: CollectionReference = db.collection(roomDbReference)
    private var roomDataListener: ListenerRegistration? = null

    private fun saveRoomData(model : RoomData) {
        onlineViewModel.setRoomData(model)
        if(model.roomId != "-1") {
            fetchRoomData()
        }
    }

    fun updateRoomData(model : RoomData) {
         try {
            if(model.roomId != "-1") {
                Log.d("TAG",model.lastMove!!)
                dbRooms.document(model.roomId).set(model)
            }
        } catch(e: Exception) {
            e.printStackTrace()
            if(e is CancellationException) throw e
        } finally {}
    }

    fun deleteRoomData(model: RoomData)  {
        stopListeningToRoomData()
        dbRooms.document(model.roomId).delete()
        onlineViewModel.setRoomData(RoomData())
    }

        fun fetchRoomData() {
        onlineViewModel.roomData.value.apply {
            if (roomId != "-1") {
                stopListeningToRoomData()
                roomDataListener = dbRooms.document(roomId).addSnapshotListener { value, e ->
                    if (e != null) {
                        Log.w("Game fetch", "Listen failed.", e)
                        return@addSnapshotListener
                    }

                    val source = if (value != null && value.metadata.hasPendingWrites()) {
                        "Local"
                    } else {
                        "Server"
                    }

                    if (value != null && value.exists() && source != "Local") {
                        Log.d("Game fetch", "$source data: ${value.data}")
                        val model = value.toObject(RoomData::class.java)!!
                        onlineViewModel.setRoomData(model)
                    } else {
                        Log.d("Game fetch","$source data: null")
                    }
                }
            }
        }
    }
    private fun stopListeningToRoomData() {
        roomDataListener?.remove()
    }

    suspend fun getRoom() : RoomData? {
        return try {
            val roomResponse =  roomRemoteService.get(token=token, id = userData.id)
            val responseBody = roomResponse.body()
            if (responseBody?.roomId != "-1") {
                saveRoomData(responseBody!!)
            }
            responseBody
        } catch(e: Exception) {
            e.printStackTrace()
            if(e is CancellationException) throw e
            null
        }
    }

    suspend fun startGame() : JsonObject? {
        return try {

            val data = gson.toJson(
                RoomData(
                    playerOneId = userData.id,
                    playerOneUsername = userData.username!!,
                    gameState = RoomStatus.CREATED,
                    rankPlayerOne = userData.eloRank,
                    pictureUrlOne = userData.profilePictureUrl ?: "default.jpg"
                )
            )
            val roomResponse = roomRemoteService.create(token = token, id = userData.id, body = data)
            val responseBody = roomResponse.body()
            if (responseBody?.get("roomId") != null && responseBody.get("roomId").asString != "-1") {
                saveRoomData(gson.fromJson(responseBody, RoomData::class.java))
            }
            Log.d("Online Client",responseBody.toString())
            return roomResponse.body()
        } catch (e: Exception) {
            e.printStackTrace()
            if (e is CancellationException) throw e
            Toast.makeText(
                context,
                "Could not start the game!",
                Toast.LENGTH_LONG
            ).show()
            saveRoomData(RoomData())
            null
        }
    }


}