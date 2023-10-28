package com.piwew.storyapp.data.repo

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.google.gson.Gson
import com.piwew.storyapp.data.ResultState
import com.piwew.storyapp.data.api.response.StoryResponse
import com.piwew.storyapp.data.api.retrofit.ApiConfig
import com.piwew.storyapp.data.database.StoryDatabase
import com.piwew.storyapp.data.database.entities.StoryEntity
import com.piwew.storyapp.data.paging.StoryRemoteMediator
import com.piwew.storyapp.data.pref.UserPreference
import com.piwew.storyapp.helper.wrapEspressoIdlingResource
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.HttpException
import java.io.File

class StoryRepository private constructor(
    private val userPreference: UserPreference,
    private val storyDatabase: StoryDatabase,
) {
    fun getStoriesPaging(): LiveData<PagingData<StoryEntity>> {
        @OptIn(ExperimentalPagingApi::class)
        wrapEspressoIdlingResource {
            return Pager(
                config = PagingConfig(
                    pageSize = 5
                ),
                remoteMediator = StoryRemoteMediator(userPreference, storyDatabase),
                pagingSourceFactory = {
                    storyDatabase.storyDao().getAllStories()
                }
            ).liveData
        }
    }

    fun detailStory(id: String) = liveData {
        emit(ResultState.Loading)
        try {
            val user = runBlocking { userPreference.getSession().first() }
            val apiService = ApiConfig.getApiService(user.token)
            val successDetailStory = apiService.detailStory(id)
            emit(ResultState.Success(successDetailStory))
        } catch (e: HttpException) {
            val jsonInString = e.response()?.errorBody()?.string()
            val errorBody = Gson().fromJson(jsonInString, StoryResponse::class.java)
            errorBody?.message?.let { ResultState.Error(it) }?.let { emit(it) }
        }
    }

    fun uploadStory(
        imageFile: File,
        description: String,
        lat: Double? = null,
        lon: Double? = null
    ) = liveData {
        emit(ResultState.Loading)
        wrapEspressoIdlingResource {
            val requestBody = description.toRequestBody("text/plain".toMediaType())
            val requestImageFile = imageFile.asRequestBody("image/jpeg".toMediaType())
            val multipartBody = MultipartBody.Part.createFormData(
                "photo",
                imageFile.name,
                requestImageFile
            )
            try {
                val user = runBlocking { userPreference.getSession().first() }
                val apiService = ApiConfig.getApiService(user.token)
                val successUploadStory =
                    apiService.uploadStory(multipartBody, requestBody, lat, lon)
                emit(ResultState.Success(successUploadStory))
            } catch (e: HttpException) {
                val jsonInString = e.response()?.errorBody()?.string()
                val errorBody = Gson().fromJson(jsonInString, StoryResponse::class.java)
                errorBody?.message?.let { ResultState.Error(it) }?.let { emit(it) }
            }
        }
    }

    companion object {
        @Volatile
        private var instance: StoryRepository? = null
        fun getInstance(
            userPreference: UserPreference,
            storyDatabase: StoryDatabase
        ): StoryRepository =
            instance ?: synchronized(this) {
                instance ?: StoryRepository(userPreference, storyDatabase)
            }.also { instance = it }
    }
}