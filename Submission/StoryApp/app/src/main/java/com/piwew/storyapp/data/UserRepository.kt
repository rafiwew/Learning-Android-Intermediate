package com.piwew.storyapp.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.google.gson.Gson
import com.piwew.storyapp.data.api.response.ErrorResponse
import com.piwew.storyapp.data.api.retrofit.ApiService
import retrofit2.HttpException

class UserRepository private constructor(
    private val apiService: ApiService
) {
    fun register(username: String, email: String, password: String): LiveData<ResultState<Any>> {
        return liveData {
            emit(ResultState.Loading)
            try {
                val successResponse = apiService.register(username, email, password).message
                emit(ResultState.Success(successResponse!!))
            } catch (e: HttpException) {
                val jsonInString = e.response()?.errorBody()?.string()
                val errorBody = Gson().fromJson(jsonInString, ErrorResponse::class.java)
                errorBody.message?.let { ResultState.Error(it) }?.let { emit(it) }
            }
        }
    }

    companion object {
        @Volatile
        private var instance: UserRepository? = null
        fun getInstance(apiService: ApiService): UserRepository =
            instance ?: synchronized(this) {
                instance ?: UserRepository(apiService)
            }.also { instance = it }
    }
}