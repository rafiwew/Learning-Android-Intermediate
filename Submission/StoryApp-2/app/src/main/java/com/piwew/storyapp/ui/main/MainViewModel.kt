package com.piwew.storyapp.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.piwew.storyapp.data.database.entities.StoryEntity
import com.piwew.storyapp.data.repo.UserRepository
import com.piwew.storyapp.data.pref.UserModel
import com.piwew.storyapp.data.repo.StoryRepository
import kotlinx.coroutines.launch

class MainViewModel(
    private val userRepository: UserRepository,
    storyRepository: StoryRepository
) : ViewModel() {

    fun getSession(): LiveData<UserModel> {
        return userRepository.getSession().asLiveData()
    }

    fun logout() {
        viewModelScope.launch {
            userRepository.logout()
        }
    }

    val stories: LiveData<PagingData<StoryEntity>> =
        storyRepository.getStoriesPaging().cachedIn(viewModelScope)
}