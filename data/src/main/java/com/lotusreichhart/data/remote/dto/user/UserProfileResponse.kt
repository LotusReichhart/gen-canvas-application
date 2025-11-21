package com.lotusreichhart.data.remote.dto.user

import com.google.gson.annotations.SerializedName

data class UserProfileResponse(
    @SerializedName("user")
    val user: UserDTO,

    @SerializedName("credit")
    val credit: CreditDTO
)
