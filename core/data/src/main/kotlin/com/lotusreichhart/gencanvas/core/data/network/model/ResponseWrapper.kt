package com.lotusreichhart.gencanvas.core.data.network.model

import com.google.gson.annotations.SerializedName

data class ResponseWrapper<T>(
    @SerializedName("status")
    val status: Int,
    @SerializedName("message")
    val message: String? = null,
    @SerializedName("data")
    val data: T? = null,
    @SerializedName("errors")
    val errors: Map<String, String>? = null
)
