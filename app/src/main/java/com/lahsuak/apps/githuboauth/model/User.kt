package com.lahsuak.apps.githuboauth.model

import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("login")
    val username: String,
    val name: String,
    @SerializedName("avatar_url") val imageUrl: String
)