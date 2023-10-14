package com.piwew.storyapp.di

import com.piwew.storyapp.data.UserRepository
import com.piwew.storyapp.data.api.retrofit.ApiConfig

object Injection {
    fun provideRepository(): UserRepository {
        val apiService = ApiConfig.getApiService()
        return UserRepository.getInstance(apiService)
    }
}