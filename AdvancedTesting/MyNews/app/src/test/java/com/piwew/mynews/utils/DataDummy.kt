package com.piwew.mynews.utils

import com.piwew.mynews.data.local.entity.NewsEntity
import com.piwew.mynews.data.remote.response.ArticlesItem
import com.piwew.mynews.data.remote.response.NewsResponse
import com.piwew.mynews.data.remote.response.Source

object DataDummy {
    fun generateDummyNewsEntity(): List<NewsEntity> {
        val newsList = ArrayList<NewsEntity>()
        for (i in 0..10) {
            val news = NewsEntity(
                "Title $i",
                "2022-02-22T22:22:22Z",
                "https://dicoding-web-img.sgp1.cdn.digitaloceanspaces.com/original/commons/feature-1-kurikulum-global-3.png",
                "https://www.dicoding.com/"
            )
            newsList.add(news)
        }
        return newsList
    }

    fun generatedDummyNewsResponse(): NewsResponse {
        val newsList = ArrayList<ArticlesItem>()
        for (i in 0..10) {
            val news = ArticlesItem(
                "2022-02-22T22:22:22Z",
                "author $i",
                "https://dicoding-web-img.sgp1.cdn.digitaloceanspaces.com/original/commons/feature-1-kurikulum-global-3.png",
                "description $i",
                Source("name", "id"),
                "Title $i",
                "https://www.dicoding.com/",
                "content $i",
            )
            newsList.add(news)
        }
        return NewsResponse(newsList.size, newsList, "Success")
    }
}