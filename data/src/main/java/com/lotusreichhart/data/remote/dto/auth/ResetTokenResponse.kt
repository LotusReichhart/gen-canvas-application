package com.lotusreichhart.data.remote.dto.auth

import com.google.gson.annotations.SerializedName

data class ResetTokenResponse(
    @SerializedName("reset_token")
    val resetToken: String
)
