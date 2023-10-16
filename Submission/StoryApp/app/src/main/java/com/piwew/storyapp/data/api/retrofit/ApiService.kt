package com.piwew.storyapp.data.api.retrofit

import com.piwew.storyapp.data.api.response.LoginResponse
import com.piwew.storyapp.data.api.response.RegisterResponse
import com.piwew.storyapp.data.api.response.StoryResponse
import retrofit2.http.*

interface ApiService {
    @FormUrlEncoded
    @POST("register")
    suspend fun register(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String,
    ): RegisterResponse

    @FormUrlEncoded
    @POST("login")
    suspend fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): LoginResponse

    @GET("stories")
    suspend fun getStories(): StoryResponse

    @GET("stories/{id}")
    suspend fun detailStory(
        @Path("id") id: String
    ): StoryResponse
}