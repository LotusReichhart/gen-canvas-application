package com.lotusreichhart.gencanvas.core.data.network.model.legalinfo

import com.google.gson.annotations.SerializedName

data class LegalDocDto(
    @SerializedName("version")
    val version: String,
    @SerializedName("url")
    val url: String
)