package com.piwew.mynews.ui.list


import androidx.lifecycle.ViewModel
import com.piwew.mynews.data.NewsRepository

class NewsViewModel(private val newsRepository: NewsRepository) : ViewModel() {
    fun getHeadlineNews() = newsRepository.getHeadlineNews()

    fun getBookmarkedNews() = newsRepository.getBookmarkedNews()
}