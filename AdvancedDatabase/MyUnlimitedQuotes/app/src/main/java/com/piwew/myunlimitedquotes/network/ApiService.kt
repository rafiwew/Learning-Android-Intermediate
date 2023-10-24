package com.piwew.myunlimitedquotes.network

import retrofit2.http.*

interface ApiService {
    @GET("list")
    suspend fun getQuote(
        @Query("page") page: Int,
        @Query("size") size: Int
    ): List<QuoteResponseItem>
}