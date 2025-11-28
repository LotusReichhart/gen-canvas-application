package com.lotusreichhart.gencanvas.core.data.network.model.auth

import com.google.gson.annotations.SerializedName

data class EmailRequest(
    @SerializedName("email")
    val email: String,
)
