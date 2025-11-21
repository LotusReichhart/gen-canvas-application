package com.lotusreichhart.data.mapper

import com.lotusreichhart.data.local.database.model.BannerLocal
import com.lotusreichhart.data.remote.dto.banner.BannerDTO
import com.lotusreichhart.domain.entity.BannerEntity

fun BannerDTO.toLocal(): BannerLocal {
    return BannerLocal(
        id = this.id ?: 0,
        title = this.title,
        imageUrl = this.imageUrl,
        actionUrl = this.actionUrl,
        displayOrder = this.displayOrder
    )
}

fun BannerLocal.toDomain(): BannerEntity {
    return BannerEntity(
        id = this.id,
        title = this.title,
        imageUrl = this.imageUrl,
        actionUrl = this.actionUrl,
        displayOrder = this.displayOrder
    )
}

fun List<BannerDTO>.toLocal(): List<BannerLocal> {
    return this.map { it.toLocal() }
}

fun List<BannerLocal>.toDomain(): List<BannerEntity> {
    return this.map { it.toDomain() }
}