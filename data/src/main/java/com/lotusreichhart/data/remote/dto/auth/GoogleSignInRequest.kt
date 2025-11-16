package com.lotusreichhart.data.remote.dto.auth

import com.google.gson.annotations.SerializedName

data class GoogleSignInRequest(
    @SerializedName("user_id_token")
    val idToken: String
)
