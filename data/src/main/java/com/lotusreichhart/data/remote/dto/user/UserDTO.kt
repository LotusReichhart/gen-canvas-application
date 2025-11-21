package com.lotusreichhart.data.remote.dto.user

import com.google.gson.annotations.SerializedName

data class UserDTO(
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("email")
    val email: String,
    @SerializedName("avatar")
    val avatarUrl: String? = null,
    @SerializedName("status")
    val status: String,
    @SerializedName("tier")
    val tier: String,
    @SerializedName("auth_provider")
    val authProvider: String
)
