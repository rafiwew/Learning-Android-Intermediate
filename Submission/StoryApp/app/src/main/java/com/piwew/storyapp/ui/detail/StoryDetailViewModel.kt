package com.piwew.storyapp.ui.detail

import androidx.lifecycle.ViewModel
import com.piwew.storyapp.data.repo.StoryRepository

class StoryDetailViewModel(private val storyRepository: StoryRepository) : ViewModel() {

    fun detailStory(id: String) = storyRepository.detailStory(id)
}