package com.lotusreichhart.gencanvas.core.data.network.model.auth

import com.google.gson.annotations.SerializedName

data class EmailSignInRequest(
    @SerializedName("email")
    val email: String,
    @SerializedName("password")
    val password: String
)
