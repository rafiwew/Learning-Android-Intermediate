package com.piwew.storyapp.data

import com.piwew.storyapp.data.api.response.RegisterResponse
import com.piwew.storyapp.data.api.retrofit.ApiService

class UserRepository private constructor(
    private val apiService: ApiService
) {

    suspend fun register(username: String, email: String, password: String): RegisterResponse {
        return apiService.register(username, email, password)
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