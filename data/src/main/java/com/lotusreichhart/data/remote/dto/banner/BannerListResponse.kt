package com.lotusreichhart.data.remote.dto.banner

import com.google.gson.annotations.SerializedName

data class BannerListResponse(
    @SerializedName("banners")
    val banners: List<BannerDTO>
)
