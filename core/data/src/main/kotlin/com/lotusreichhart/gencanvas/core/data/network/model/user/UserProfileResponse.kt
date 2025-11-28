package com.lotusreichhart.gencanvas.core.data.network.model.user

import com.google.gson.annotations.SerializedName

data class UserProfileResponse(
    @SerializedName("user")
    val user: UserDto,

    @SerializedName("credit")
    val credit: UserCreditDto
)
