package com.lotusreichhart.data.remote.dto.banner

import com.google.gson.annotations.SerializedName

data class BannerDTO(
    @SerializedName("id")
    val id: Int? = null,
    @SerializedName("title")
    val title: String? = null,
    @SerializedName("image_url")
    val imageUrl: String,
    @SerializedName("action_url")
    val actionUrl: String? = null,
    @SerializedName("display_order")
    val displayOrder: Int
)
