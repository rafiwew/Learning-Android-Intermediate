package com.piwew.myunlimitedquotes.di

import android.content.Context
import com.piwew.myunlimitedquotes.data.QuoteRepository
import com.piwew.myunlimitedquotes.database.QuoteDatabase
import com.piwew.myunlimitedquotes.network.ApiConfig

object Injection {
    fun provideRepository(context: Context): QuoteRepository {
        val database = QuoteDatabase.getDatabase(context)
        val apiService = ApiConfig.getApiService()
        return QuoteRepository(database, apiService)
    }
}