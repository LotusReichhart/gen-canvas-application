package com.lotusreichhart.domain.entities

data class BannerEntity(
    val id: Int? = null,
    val title: String? = null,
    val imageUrl: String,
    val actionUrl: String? = null,
    val displayOrder: Int
)
