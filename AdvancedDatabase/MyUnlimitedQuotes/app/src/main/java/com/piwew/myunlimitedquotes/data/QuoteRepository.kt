package com.piwew.myunlimitedquotes.data

import com.piwew.myunlimitedquotes.database.QuoteDatabase
import com.piwew.myunlimitedquotes.network.ApiService
import com.piwew.myunlimitedquotes.network.QuoteResponseItem

class QuoteRepository(
    private val quoteDatabase: QuoteDatabase,
    private val apiService: ApiService
) {
    suspend fun getQuote(): List<QuoteResponseItem> {
        return apiService.getQuote(1, 5)
    }
}