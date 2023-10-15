package com.piwew.storyapp.di

import android.content.Context
import com.piwew.storyapp.data.UserRepository
import com.piwew.storyapp.data.api.retrofit.ApiConfig
import com.piwew.storyapp.data.pref.UserPreference
import com.piwew.storyapp.data.pref.dataStore

object Injection {
    fun provideRepository(context: Context): UserRepository {
        val apiService = ApiConfig.getApiService()
        val pref = UserPreference.getInstance(context.dataStore)
        return UserRepository.getInstance(apiService, pref)
    }
}