package com.example.chessmate.matches

import com.example.chessmate.BuildConfig
import com.example.chessmate.sign_in.SignInResult
import com.example.chessmate.sign_in.SignInViewModel
import com.example.chessmate.sign_in.UserData
import com.example.chessmate.ui.utils.HelperClassMatches
import com.example.chessmate.ui.utils.MatchesApi
import com.google.gson.Gson
import kotlin.coroutines.cancellation.CancellationException

class MatchesUIClient(
    private val userData : UserData,
    private val matchesViewModel: MatchesViewModel,
) {
    private val matchRemoteService : MatchesApi = HelperClassMatches.getIstance()
    private val token = BuildConfig.TOKEN
    private val gson = Gson()

    suspend fun getMatches() {
        var matchList = emptyList<Match>()
        try{
            val matchResponse = matchRemoteService.get(token=token, id= userData.id)
            if (matchResponse.code() == 200){
                for (match in matchResponse.body()!!){
                    val newMatch = Match(match.roomId, match.matchType, match.userIdOne, match.userIdTwo, match.usernameUserTwo, match.profilePictureUrlUserTwo, match.results)
                    matchList = matchList + newMatch
                }
                matchesViewModel.setMatches(matchList)
            } else {
                matchesViewModel.setMatches(emptyList())
            }
        } catch(e: Exception) {
            e.printStackTrace()
            if(e is CancellationException) throw e
            matchesViewModel.setMatches(emptyList())
        }
    }

    suspend fun insertMatch(match: Match, signInViewModel: SignInViewModel?, userDataNew: UserData): Boolean {
        try{
            val data = gson.toJson(match)
            matchRemoteService.create(token=BuildConfig.TOKEN, body = data)
        } catch(e: Exception) {
            e.printStackTrace()
            if(e is CancellationException) throw e
            return false
        }
        getMatches()
        return signInViewModel!!.setUserData(newValue = SignInResult(data = userDataNew, errorMessage = null))
    }
}