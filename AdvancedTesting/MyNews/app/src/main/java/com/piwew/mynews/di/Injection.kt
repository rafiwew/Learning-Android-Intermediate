package com.piwew.mynews.di

import android.content.Context
import com.piwew.mynews.data.NewsRepository
import com.piwew.mynews.data.local.room.NewsDatabase
import com.piwew.mynews.data.remote.retrofit.ApiConfig

object Injection {
    fun provideRepository(context: Context): NewsRepository {
        val apiService = ApiConfig.getApiService()
        val database = NewsDatabase.getInstance(context)
        val dao = database.newsDao()
        return NewsRepository.getInstance(apiService, dao)
    }
}