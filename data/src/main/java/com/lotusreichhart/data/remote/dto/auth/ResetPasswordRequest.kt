package com.lotusreichhart.data.remote.dto.auth

import com.google.gson.annotations.SerializedName

data class ResetPasswordRequest(
    @SerializedName("reset_token")
    val resetToken: String,
    @SerializedName("new_password")
    val newPassword: String,
    @SerializedName("confirm_password")
    val confirmPassword: String
)
