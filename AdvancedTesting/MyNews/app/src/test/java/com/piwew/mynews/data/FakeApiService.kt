package com.piwew.mynews.data

import com.piwew.mynews.data.remote.response.NewsResponse
import com.piwew.mynews.data.remote.retrofit.ApiService
import com.piwew.mynews.utils.DataDummy

class FakeApiService : ApiService {
    private val dummyResponse = DataDummy.generatedDummyNewsResponse()
    override suspend fun getNews(apiKey: String): NewsResponse {
        return dummyResponse
    }
}