package com.lotusreichhart.gencanvas.core.data.mapper

import com.lotusreichhart.gencanvas.core.data.database.model.banner.BannerEntity
import com.lotusreichhart.gencanvas.core.data.network.model.banner.BannerDto
import com.lotusreichhart.gencanvas.core.model.banner.Banner

fun BannerDto.toEntity(): BannerEntity {
    return BannerEntity(
        id = this.id ?: 0,
        title = this.title,
        imageUrl = this.imageUrl,
        actionUrl = this.actionUrl,
        displayOrder = this.displayOrder
    )
}

fun BannerEntity.toModel(): Banner {
    return Banner(
        id = this.id,
        title = this.title,
        imageUrl = this.imageUrl,
        actionUrl = this.actionUrl,
        displayOrder = this.displayOrder
    )
}

fun List<BannerDto>.toEntity(): List<BannerEntity> {
    return this.map { it.toEntity() }
}

fun List<BannerEntity>.toModel(): List<Banner> {
    return this.map { it.toModel() }
}