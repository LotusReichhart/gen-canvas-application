package com.lotusreichhart.data.remote.dto.auth

import com.google.gson.annotations.SerializedName

data class EmailRequest(
    @SerializedName("email")
    val email: String,
)
