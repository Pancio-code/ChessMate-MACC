package com.example.chessmate.multiplayer

import com.example.chessmate.BuildConfig
import com.example.chessmate.ui.utils.HelperClassParseChessBoard
import com.example.chessmate.ui.utils.ParseChessBoardAPI
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import kotlin.coroutines.cancellation.CancellationException

class ParseChessBoardUIClient {
    private val parseChessBoardAPI : ParseChessBoardAPI = HelperClassParseChessBoard.getIstance()

    suspend fun uploadScreenshot(newAvatarFile: File?): Boolean {

        try {
            val fileRequestBody = newAvatarFile!!.asRequestBody("image/*".toMediaTypeOrNull())
            val filePart = MultipartBody.Part.createFormData("file", newAvatarFile.name, fileRequestBody)
            val ret = parseChessBoardAPI.parseChessBoard(token= BuildConfig.TOKEN, image= filePart)
        } catch (e: Exception) {
            e.printStackTrace()
            if (e is CancellationException) throw e
        }
        return true
    }

}