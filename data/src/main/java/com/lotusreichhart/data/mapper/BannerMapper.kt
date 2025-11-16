package com.lotusreichhart.data.mapper

import com.lotusreichhart.data.remote.dto.banner.BannerDTO
import com.lotusreichhart.domain.entity.BannerEntity

fun BannerDTO.toDomain(): BannerEntity {
    return BannerEntity(
        id = this.id,
        title = this.title,
        imageUrl = this.imageUrl,
        actionUrl = this.actionUrl,
        displayOrder = this.displayOrder
    )
}

fun List<BannerDTO>.toDomain(): List<BannerEntity> {
    return this.map { it.toDomain() }
}