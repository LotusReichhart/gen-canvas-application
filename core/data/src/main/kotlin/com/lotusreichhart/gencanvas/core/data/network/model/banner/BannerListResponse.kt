package com.lotusreichhart.gencanvas.core.data.network.model.banner

import com.google.gson.annotations.SerializedName

data class BannerListResponse(
    @SerializedName("banners")
    val banners: List<BannerDto>
)
