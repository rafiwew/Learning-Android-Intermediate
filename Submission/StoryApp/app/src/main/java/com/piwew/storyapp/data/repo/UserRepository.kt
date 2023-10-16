package com.piwew.storyapp.data.repo

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.google.gson.Gson
import com.piwew.storyapp.data.ResultState
import com.piwew.storyapp.data.api.response.ErrorResponse
import com.piwew.storyapp.data.api.response.LoginResponse
import com.piwew.storyapp.data.api.retrofit.ApiService
import com.piwew.storyapp.data.pref.UserModel
import com.piwew.storyapp.data.pref.UserPreference
import kotlinx.coroutines.flow.Flow
import retrofit2.HttpException

class UserRepository private constructor(
    private val apiService: ApiService,
    private val userPreference: UserPreference
) {

    fun getSession(): Flow<UserModel> {
        return userPreference.getSession()
    }

    suspend fun saveSession(user: UserModel) {
        userPreference.saveSession(user)
    }

    suspend fun logout() {
        userPreference.logout()
    }

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

    fun login(email: String, password: String) = liveData {
        emit(ResultState.Loading)
        try {
            val successResponse = apiService.login(email, password)
            emit(ResultState.Success(successResponse))
        } catch (e: HttpException) {
            val jsonInString = e.response()?.errorBody()?.string()
            val errorBody = Gson().fromJson(jsonInString, LoginResponse::class.java)
            errorBody?.message?.let { ResultState.Error(it) }?.let { emit(it) }
        }
    }

    companion object {
        @Volatile
        private var instance: UserRepository? = null
        fun getInstance(
            apiService: ApiService,
            userPreference: UserPreference
        ): UserRepository =
            instance ?: synchronized(this) {
                instance ?: UserRepository(apiService, userPreference)
            }.also { instance = it }
    }
}