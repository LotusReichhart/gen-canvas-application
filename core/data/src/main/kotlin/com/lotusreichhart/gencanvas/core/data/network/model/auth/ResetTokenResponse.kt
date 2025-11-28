package com.lotusreichhart.gencanvas.core.data.network.model.auth

import com.google.gson.annotations.SerializedName

data class ResetTokenResponse(
    @SerializedName("reset_token")
    val resetToken: String
)
