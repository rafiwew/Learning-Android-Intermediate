package com.piwew.storyapp.di

import android.content.Context
import com.piwew.storyapp.data.repo.UserRepository
import com.piwew.storyapp.data.api.retrofit.ApiConfig
import com.piwew.storyapp.data.pref.UserPreference
import com.piwew.storyapp.data.pref.dataStore
import com.piwew.storyapp.data.repo.StoryRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

object Injection {
    fun provideRepository(context: Context): UserRepository {
        val pref = UserPreference.getInstance(context.dataStore)
        val user = runBlocking { pref.getSession().first() }
        val apiService = ApiConfig.getApiService(user.token)
        return UserRepository.getInstance(apiService, pref)
    }

    fun provideStoryRepository(context: Context): StoryRepository {
        val pref = UserPreference.getInstance(context.dataStore)
        return StoryRepository.getInstance(pref)
    }
}