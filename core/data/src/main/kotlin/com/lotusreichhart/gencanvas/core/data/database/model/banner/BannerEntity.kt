package com.lotusreichhart.gencanvas.core.data.database.model.banner

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "banners")
data class BannerEntity(
    @PrimaryKey
    val id: Int,
    val title: String?,
    val imageUrl: String,
    val actionUrl: String?,
    val displayOrder: Int
)
