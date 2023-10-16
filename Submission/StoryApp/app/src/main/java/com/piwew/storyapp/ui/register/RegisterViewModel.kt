package com.piwew.storyapp.ui.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.piwew.storyapp.data.ResultState
import com.piwew.storyapp.data.repo.UserRepository

class RegisterViewModel(private val userRepository: UserRepository) : ViewModel() {
    fun register(username: String, email: String, password: String): LiveData<ResultState<Any>> {
        return userRepository.register(username, email, password)
    }
}