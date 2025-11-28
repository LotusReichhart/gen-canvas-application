package com.lotusreichhart.gencanvas.core.data.network.model.auth

import com.google.gson.annotations.SerializedName

data class GoogleSignInRequest(
    @SerializedName("user_id_token")
    val idToken: String
)
