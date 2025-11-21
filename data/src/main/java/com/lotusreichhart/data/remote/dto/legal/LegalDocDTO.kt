package com.lotusreichhart.data.remote.dto.legal

import com.google.gson.annotations.SerializedName

data class LegalDocDTO(
    @SerializedName("version")
    val version: String,
    @SerializedName("url")
    val url: String
)
