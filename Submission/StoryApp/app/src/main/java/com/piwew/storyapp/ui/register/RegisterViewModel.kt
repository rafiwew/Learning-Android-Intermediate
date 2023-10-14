package com.piwew.storyapp.ui.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.piwew.storyapp.data.UserRepository
import com.piwew.storyapp.data.api.response.ErrorResponse
import kotlinx.coroutines.launch
import retrofit2.HttpException

class RegisterViewModel(private val userRepository: UserRepository) : ViewModel() {

    private val _messageResponse = MutableLiveData<String?>()
    val messageResponse: LiveData<String?> = _messageResponse

    private val _registerStatusResponse: MutableLiveData<Boolean> = MutableLiveData<Boolean>()
    val registerStatusResponse: LiveData<Boolean> = _registerStatusResponse

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun register(username: String, email: String, password: String) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                val message = userRepository.register(username, email, password).message
                _messageResponse.value = message
                _registerStatusResponse.value = true
                _isLoading.value = false
            } catch (e: HttpException) {
                val jsonInString = e.response()?.errorBody()?.string()
                val errorBody = Gson().fromJson(jsonInString, ErrorResponse::class.java)
                _messageResponse.value = errorBody.message
                _registerStatusResponse.value = false
            }
        }
    }
}