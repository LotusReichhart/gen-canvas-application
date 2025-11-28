package com.lotusreichhart.gencanvas.core.data.network.model.user

import com.google.gson.annotations.SerializedName

data class UserDto(
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
