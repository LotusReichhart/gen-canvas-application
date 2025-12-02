package com.lotusreichhart.gencanvas.core.data.network.model

import com.google.gson.annotations.SerializedName

data class ResponseWrapper<T>(
    @SerializedName("status")
    val status: Int,
    @SerializedName("message")
    val message: String? = null,
    @SerializedName("data")
    val data: T? = null
)

data class ErrorResponseWrapper(
    @SerializedName("status")
    val status: Int,
    @SerializedName("message")
    val message: String? = null,
    @SerializedName("data")
    val data: ErrorDataPayload? = null
)

data class ErrorDataPayload(
    @SerializedName("error")
    val error: Map<String, String>? = null
)