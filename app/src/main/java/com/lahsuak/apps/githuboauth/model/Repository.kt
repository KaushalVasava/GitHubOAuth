package com.lahsuak.apps.githuboauth.model

import com.google.gson.annotations.SerializedName

data class Repository(
    val id: Long? = null,
    val name: String? = null,
    val language: String,
    val private:Boolean,
    @SerializedName("full_name")val fullName: String? = null,
    val owner: Owner? = null
)
