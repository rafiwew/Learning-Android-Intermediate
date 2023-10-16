package com.piwew.storyapp.data.repo

import androidx.lifecycle.liveData
import com.google.gson.Gson
import com.piwew.storyapp.data.ResultState
import com.piwew.storyapp.data.api.response.StoryResponse
import com.piwew.storyapp.data.api.retrofit.ApiService
import retrofit2.HttpException

class StoryRepository private constructor(
    private val apiService: ApiService,
) {
    fun getStories() = liveData {
        emit(ResultState.Loading)
        try {
            val successGetStories = apiService.getStories()
            emit(ResultState.Success(successGetStories))
        } catch (e: HttpException) {
            val jsonInString = e.response()?.errorBody()?.string()
            val errorBody = Gson().fromJson(jsonInString, StoryResponse::class.java)
            errorBody?.message?.let { ResultState.Error(it) }?.let { emit(it) }
        }
    }

    companion object {
        @Volatile
        private var instance: StoryRepository? = null
        fun getInstance(
            apiService: ApiService
        ): StoryRepository =
            instance ?: synchronized(this) {
                instance ?: StoryRepository(apiService)
            }.also { instance = it }
    }
}