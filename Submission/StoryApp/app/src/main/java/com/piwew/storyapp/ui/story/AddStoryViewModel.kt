package com.piwew.storyapp.ui.story

import androidx.lifecycle.ViewModel
import com.piwew.storyapp.data.repo.StoryRepository
import java.io.File

class AddStoryViewModel(
    private val storyRepository: StoryRepository
) : ViewModel() {

    fun uploadStory(file: File, description: String) =
        storyRepository.uploadStory(file, description)
}