package com.piwew.storyapp.data.api.response

import com.google.gson.annotations.SerializedName

data class LoginResponse(

    @field:SerializedName("message")
    val message: String,

    @field:SerializedName("loginResult")
    val loginResult: LoginResult
)

data class LoginResult(

    @field:SerializedName("name")
    val name: String,

    @field:SerializedName("token")
    val token: String
)
