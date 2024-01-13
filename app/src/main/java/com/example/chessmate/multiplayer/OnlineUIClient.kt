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
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import com.google.gson.JsonObject
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.coroutines.cancellation.CancellationException

class OnlineUIClient(
    private val context: Context,
    db : FirebaseFirestore,
    private val onlineViewModel: OnlineViewModel,
    private val userData : UserData
) {
    private val auth = Firebase.auth
    private val roomRemoteService : OnlineAPI = HelperClassOnline.getIstance()
    private val token = BuildConfig.TOKEN
    private val gson = Gson()
    private val roomDbReference = getString(context,R.string.roomDbReference)
    private val dbRooms: CollectionReference = db.collection(roomDbReference)

    suspend fun getRoom() : RoomData? {
        return try {
            val roomResponse =  roomRemoteService.get(token=token, userId = userData.id!!)
            roomResponse.body()
        } catch(e: Exception) {
            e.printStackTrace()
            if(e is CancellationException) throw e
            null
        }
    }

    suspend fun startGame() : JsonObject? {
        return try {

            val currentDateTime = LocalDateTime.now()
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
            val formattedDateTime = currentDateTime.format(formatter)

            val data = gson.toJson(RoomData(
                    playerOneId = userData.id,
                    rankPlayerOne =  userData.eloRank,
                    dataCreation = formattedDateTime
                )
            )
            Log.d("API",data)
            val roomResponse =  roomRemoteService.create(token=token, id = userData.id!!, body = data)
            val responseBody = roomResponse.body()
            val roomId = responseBody?.get("roomId")?.asString
            if (roomId != null) {
                listenForGameChanges(roomId = roomId , onGameUpdate = { newData ->
                    onlineViewModel.setRoomData(newData)
                })
            }
            return responseBody
        } catch(e: Exception) {
            e.printStackTrace()
            if(e is CancellationException) throw e
            Toast.makeText(
                context,
                "Could not start the game!",
                Toast.LENGTH_LONG
            ).show()
            null
        }
    }

    fun listenForGameChanges(roomId: String, onGameUpdate: (RoomData) -> Unit) {
        dbRooms.document(roomId).addSnapshotListener { snapshot, e ->
            if (e != null) {
                Log.w("Listen", "Listen failed.", e)
                return@addSnapshotListener
            }

            if (snapshot != null && snapshot.exists()) {
                Log.d("Snapshot", "Current data: ${snapshot.data}")
                if (snapshot.data?.get("gameState") == "Start") {
                        val roomData = gson.fromJson(gson.toJson(snapshot.data), RoomData::class.java)
                        onGameUpdate(roomData)
                }
            } else {
                Log.d("Snapshot", "Current data: null")
            }
        }
    }

    suspend fun makeMove(roomId: String, move: String): Int? {
        return try {
            val moveData = mapOf("move" to move)
            val moveResponse = roomRemoteService.move(token=token, id = userData.id!!, body = gson.toJson(moveData))
            moveResponse.code()
        } catch(e: Exception) {
            e.printStackTrace()
            if(e is CancellationException) throw e
            Toast.makeText(context, "Failed to make a move!", Toast.LENGTH_LONG).show()
            null
        }
    }

    //TODO: Offline Handling
    /*private fun sendHeartbeat(roomId: String) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = roomRemoteService.sendHeartbeat(token, roomId, userData.id)
                if (!response.isSuccessful) {
                    Log.e("Heartbeat", "Failed to send heartbeat")
                }
            } catch (e: Exception) {
                Log.e("Heartbeat", "Error sending heartbeat", e)
            }
        }
    }

    private fun scheduleHeartbeat(roomId: String) {
        CoroutineScope(Dispatchers.IO).launch {
            while (isActive) {
                sendHeartbeat(roomId)
                delay(30000)  // Invia l'heartbeat ogni 30 secondi
            }
        }
    }*/
}