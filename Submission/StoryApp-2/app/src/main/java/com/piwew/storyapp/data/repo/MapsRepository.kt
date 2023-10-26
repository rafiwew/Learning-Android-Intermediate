package com.piwew.storyapp.data.repo

import androidx.lifecycle.liveData
import com.google.gson.Gson
import com.piwew.storyapp.data.ResultState
import com.piwew.storyapp.data.api.response.StoryResponse
import com.piwew.storyapp.data.api.retrofit.ApiConfig
import com.piwew.storyapp.data.pref.UserPreference
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import retrofit2.HttpException

class MapsRepository private constructor(
    private val userPreference: UserPreference
) {
    fun getStoriesWithLocation() = liveData {
        emit(ResultState.Loading)
        try {
            val user = runBlocking { userPreference.getSession().first() }
            val apiService = ApiConfig.getApiService(user.token)
            val successGetStoriesWithLocation = apiService.getStoriesWithLocation()
            emit(ResultState.Success(successGetStoriesWithLocation))
        } catch (e: HttpException) {
            val jsonInString = e.response()?.errorBody()?.string()
            val errorBody = Gson().fromJson(jsonInString, StoryResponse::class.java)
            errorBody?.message?.let { ResultState.Error(it) }?.let { emit(it) }
        }
    }

    companion object {
        @Volatile
        private var instance: MapsRepository? = null

        fun getInstance(
            userPreference: UserPreference
        ): MapsRepository =
            instance ?: synchronized(this) {
                instance ?: MapsRepository(userPreference)
            }.also { instance = it }
    }
}